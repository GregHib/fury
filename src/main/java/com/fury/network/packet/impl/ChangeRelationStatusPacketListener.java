package com.fury.network.packet.impl;

import com.fury.game.entity.character.player.info.PlayerRelations.PrivateChatStatus;
import com.fury.network.packet.Packet;
import com.fury.network.packet.PacketListener;
import com.fury.core.model.node.entity.actor.figure.player.Player;

public class ChangeRelationStatusPacketListener implements PacketListener {

	@Override
	public void handleMessage(Player player, Packet packet) {
		int actionId = packet.readInt();
		player.getRelations().setStatus(PrivateChatStatus.forActionId(actionId), true);
	}

}
