package com.fury.network.packet.impl;

import com.fury.core.model.node.entity.actor.figure.player.Player;
import com.fury.game.world.World;
import com.fury.network.packet.Packet;
import com.fury.network.packet.PacketConstants;
import com.fury.network.packet.PacketListener;
import com.fury.util.Misc;
import com.fury.util.NameUtils;

/**
 * This packet listener is called when a player is doing something relative
 * to their friends or ignore list, such as adding or deleting a player from said list.
 * 
 * @author relex lawl
 */

public class PlayerRelationPacketListener implements PacketListener {

	@Override
	public void handleMessage(Player player, Packet packet) {
		try {
			long username = packet.readLong();
			switch (packet.getOpcode()) {
			case PacketConstants.ADD_FRIEND_OPCODE:
				player.getRelations().addFriend(username);
				break;
			case PacketConstants.ADD_IGNORE_OPCODE:
				player.getRelations().addIgnore(username);
				break;
			case PacketConstants.REMOVE_FRIEND_OPCODE:
				player.getRelations().deleteFriend(username);
				break;
			case PacketConstants.REMOVE_IGNORE_OPCODE:
				player.getRelations().deleteIgnore(username);
				break;
			case PacketConstants.PRIVATE_MESSAGE_OPCODE:
				Player friend = World.getPlayerByName(Misc.formatText(NameUtils.longToString(username)).replaceAll("_", " "));
				int size = packet.getSize();
				byte[] message = packet.readBytes(size);
				player.getRelations().message(friend, message, size);
				break;
			}
		} catch (Exception e) {

		}
	}
}
