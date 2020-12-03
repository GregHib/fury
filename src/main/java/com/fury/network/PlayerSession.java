package com.fury.network;

import com.fury.core.model.node.entity.actor.figure.player.Player;
import com.fury.game.world.GameWorld;
import com.fury.network.login.LoginDetailsMessage;
import com.fury.network.login.LoginResponsePacket;
import com.fury.network.login.LoginResponses;
import com.fury.network.packet.Packet;
import com.fury.network.packet.PacketBuilder;
import com.fury.network.packet.PacketConstants;
import com.fury.network.packet.codec.PacketDecoder;
import com.fury.network.packet.codec.PacketEncoder;
import com.fury.network.packet.impl.SilencedPacketListener;
import com.fury.util.NameUtils;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.socket.SocketChannel;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * The session handler dedicated to a player that will handle input and output
 * operations.
 *
 * @author lare96 <http://github.com/lare96>
 * @author blakeman8192
 */
public final class PlayerSession {

	/**
	 * The queue of messages that will be handled on the next sequence.
	 */
	private final Queue<Packet> prioritizedPacketsQueue = new ConcurrentLinkedQueue<>();
	private final Queue<Packet> packetsQueue = new ConcurrentLinkedQueue<>();

	/**
	 * The channel that will manage the connection for this player.
	 */
	private final Channel channel;

	/**
	 * The player I/O operations will be executed for.
	 */
	private Player player;

	/**
	 * The current state of this I/O session.
	 */
	private SessionState state = SessionState.LOGGING_IN;

	public PlayerSession(SocketChannel channel) {
		this.channel = channel;
		this.player = new Player(this);
	}

	/**
	 * Uses state-machine to handle upstream messages from Netty.
	 *
	 * @param msg
	 *            the message to handle.
	 */
	public void queuePacket(Packet msg) {
		int total_size = (packetsQueue.size() + prioritizedPacketsQueue.size());
		if(total_size >= NetworkConstants.PACKET_PROCESS_LIMIT)
			return;

		//Add the packet to the queue.
		//If it's prioritized, add it to the prioritized queue instead.
		if(msg.getOpcode() == PacketConstants.EQUIP_ITEM_OPCODE ||
				msg.getOpcode() == PacketConstants.FIRST_ITEM_ACTION_OPCODE)
			prioritizedPacketsQueue.add(msg);
		else
			packetsQueue.add(msg);
	}

	/**
	 * Processes all of the queued messages from the {@link PacketDecoder} by
	 * polling the internal queue, and then handling them via the handleInputMessage.
	 */
	public void handleQueuedMessages() {
		handleQueuedMessages(true);
	}

	public void handleQueuedMessages(boolean priorityOnly) {
		Packet msg;
		int processed = 0;

		while ((msg = prioritizedPacketsQueue.poll()) != null && ++processed < NetworkConstants.PACKET_PROCESS_LIMIT)
			processPacket(msg);

		if(priorityOnly)
			return;

		while ((msg = packetsQueue.poll()) != null && ++processed < NetworkConstants.PACKET_PROCESS_LIMIT)
			processPacket(msg);
	}

	/**
	 * Handles an incoming message.
	 * @param msg	The message to handle.
	 */
	public void processPacket(Packet msg) {
		if(PacketConstants.PACKETS[msg.getOpcode()] instanceof SilencedPacketListener)
			System.out.println("Unhandled packet: " + msg.getOpcode());
		PacketConstants.PACKETS[msg.getOpcode()].handleMessage(player, msg);
	}

	/**
	 * Queues the {@code msg} for this session to be encoded and sent to the
	 * client.
	 *
	 * @param msg
	 *            the message to queue.
	 */
	public void write(PacketBuilder msg) {
		try {
			if (!channel.isOpen())
				return;
			Packet packet = msg.toPacket();
			channel.write(packet);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}


	public void writeAndFlush(PacketBuilder builder) {
		try {
			if (!channel.isOpen())
				return;
			Packet packet = builder.toPacket();
			channel.writeAndFlush(packet);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}


	public void flush() {
		try {
			channel.flush();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	public void clearMessages() {
		prioritizedPacketsQueue.clear();
		packetsQueue.clear();
	}

	/**
	 * Gets the player I/O operations will be executed for.
	 *
	 * @return the player I/O operations.
	 */
	public Player getPlayer() {
		return player;
	}

	/**
	 * Gets the current state of this I/O session.
	 *
	 * @return the current state.
	 */
	public SessionState getState() {
		return state;
	}

	/**
	 * Sets the value for {@link PlayerSession#state}.
	 *
	 * @param state
	 *            the new value to set.
	 */
	public void setState(SessionState state) {
		this.state = state;
	}

	public Channel getChannel() {
		return channel;
	}

	public void setPlayer(Player player) {
		this.player = player;
	}

	public void finalizeLogin(LoginDetailsMessage msg) {
		SocketChannel channel = (SocketChannel) msg.getContext().channel();

		//Update the player
		player.setUsername(msg.getUsername());
		player.setLongUsername(NameUtils.stringToLong(msg.getUsername()));
		player.getLogger().setIpAddress(msg.getHost());
		player.getLogger().setHardwareId(msg.getHardwareId());
		player.getLogger().setMacAddress(msg.getSerialNumber());

		//Get the response code
		int response = LoginResponses.getResponse(player, msg);

		//Write the response and flush the channel
		ChannelFuture future = channel.writeAndFlush(new LoginResponsePacket(response, player.getRightsId(), false));

		//Close the channel after sending the response if it wasn't a successful login
		if(response != LoginResponses.LOGIN_SUCCESSFUL) {
			future.addListener(ChannelFutureListener.CLOSE);
			return;
		}

		//Wait...
		future.awaitUninterruptibly();

		//Replace decoder/encoder to packets
		channel.pipeline().replace("encoder", "encoder", new PacketEncoder(msg.getEncryptor()));

		channel.pipeline().replace("decoder", "decoder", new PacketDecoder(msg.getDecryptor()));


		GameWorld.queueLogin(player);
	}
}
