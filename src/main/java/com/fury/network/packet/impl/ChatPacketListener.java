package com.fury.network.packet.impl;

import com.fury.cache.ByteBuffer;
import com.fury.core.model.node.entity.actor.figure.player.Player;
import com.fury.game.system.communication.punishment.Punishment;
import com.fury.game.world.update.flag.Flag;
import com.fury.game.world.update.flag.block.ChatMessage.Message;
import com.fury.network.packet.Packet;
import com.fury.network.packet.PacketListener;
import com.fury.util.Misc;
import com.fury.util.NameUtils;

/**
 * This packet listener manages the spoken text by a player.
 * 
 * @author relex lawl
 */

public class ChatPacketListener implements PacketListener {

	@Override
	public void handleMessage(Player player, Packet packet) {
		int effects = packet.readUnsignedByte();
		int color = packet.readUnsignedByte();
		int size = packet.getSize();
		byte[] text = packet.readReversedBytesA(size);
		if(Punishment.isMuted(player) || Punishment.isHardwareMuted(player)) {
			player.message("You are muted and cannot chat.");
			return;
		}
		String message = NameUtils.decode(new ByteBuffer(text), size);
		if(Misc.blockedWord(message.toLowerCase().replaceAll(";", "."))) {
			player.getDialogueManager().sendStatement("A word was blocked in your sentence. Please do not repeat it!");
			return;
		}
		player.getLogger().addMessage(message);
		player.getChatMessages().set(new Message(color, effects, text));
		player.getUpdateFlags().add(Flag.CHAT);
	}

}
