package com.fury.network.packet.impl;

import com.fury.network.packet.Packet;
import com.fury.network.packet.PacketListener;
import com.fury.core.model.node.entity.actor.figure.player.Player;

public class WithdrawMoneyFromPouchPacketListener implements PacketListener {

	@Override
	public void handleMessage(Player player, Packet packet) {
		int amount = packet.readInt();
		player.getMoneyPouch().withdraw(amount);
	}

}
