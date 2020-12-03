package com.fury.network.packet.impl;

import com.fury.core.model.node.entity.actor.figure.player.Player;
import com.fury.game.world.GameWorld;
import com.fury.network.packet.Packet;
import com.fury.network.packet.PacketListener;

//CALLED EVERY 10 MINUTES OF INACTIVITY

public class IdleLogoutPacketListener implements PacketListener {

	@Override
	public void handleMessage(Player player, Packet packet) {
		if(true)
			return;
		if(player.getRights().isStaff())
			return;
		if(player.isInactive()) {
			if(player.canLogout())
				GameWorld.getPlayers().remove(player);
		} else {
			player.setInactive(true);
		}
	}
}
