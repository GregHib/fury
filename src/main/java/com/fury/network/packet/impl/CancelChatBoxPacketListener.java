package com.fury.network.packet.impl;

import com.fury.core.model.node.entity.actor.figure.player.Player;
import com.fury.network.packet.Packet;
import com.fury.network.packet.PacketListener;

public class CancelChatBoxPacketListener implements PacketListener {
	
	@Override
	public void handleMessage(Player player, Packet packet) {
		if(player.getBank() != null && player.getBank().banking()) {
			player.getBank().setSearching(false);
			player.setInputHandling(null);
			player.getPacketSender().sendConfig(117, 0);
		}
	}
}
