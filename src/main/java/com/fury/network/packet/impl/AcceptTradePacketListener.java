package com.fury.network.packet.impl;

import com.fury.core.model.node.entity.actor.figure.player.Player;
import com.fury.game.content.controller.impl.FirstAdventureController;
import com.fury.game.world.GameWorld;
import com.fury.game.world.map.path.RouteEvent;
import com.fury.network.packet.Packet;
import com.fury.network.packet.PacketListener;

/**
 * This packet listener is called when a player accepts a trade offer,
 * whether it's from the chat box or through the trading player menu.
 * 
 * @author relex lawl
 */

public class AcceptTradePacketListener implements PacketListener {

	@Override
	public void handleMessage(Player player, Packet packet) {
		if (player.getHealth().getHitpoints() <= 0)
			return;
		if(player.getMovement().getTeleporting() || player.getControllerManager().getController() instanceof FirstAdventureController)
			return;
		if(player.getGameMode().isIronMan()) {
			player.message("You cannot trade other players because you are an Ironman");
			return;
		}

		if(!player.getControllerManager().canTrade()) {
			player.message("You cannot trade other players here.");
			return;
		}

		int index = packet.readShort();
		if(index < 0 || index > GameWorld.getPlayers().capacity())
			return;

		Player target = GameWorld.getPlayers().get(index);
		if (target == null || !player.isWithinDistance(target, 13))
			return;

		player.stopAll(false);
		player.setRouteEvent(new RouteEvent(target, () -> {
			if(target.getIndex() != player.getIndex())
				player.getTrade().requestTrade(target);
		}));
	}

}
