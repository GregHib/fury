package com.fury.network.packet.impl;

import com.fury.core.model.node.entity.actor.figure.player.Player;
import com.fury.game.world.GameWorld;
import com.fury.game.world.map.path.RouteEvent;
import com.fury.network.packet.Packet;
import com.fury.network.packet.PacketListener;

public class AcceptChallengePacketListener implements PacketListener {
	
	@Override
	public void handleMessage(Player player, Packet packet) {
		if (player.getHealth().getHitpoints() <= 0)
			return;
		int index = packet.readShort();

		if(index > GameWorld.getPlayers().capacity())
			return;
		Player target = GameWorld.getPlayers().get(index);
		if (target == null) 
			return;

		player.stopAll(false);
		player.setRouteEvent(new RouteEvent(target, () -> {
            if(target.getIndex() != player.getIndex())
            	player.getControllerManager().canPlayerOption1(target);
        }));
	}
}
