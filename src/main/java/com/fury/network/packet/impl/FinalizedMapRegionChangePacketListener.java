package com.fury.network.packet.impl;

import com.fury.core.model.node.entity.actor.figure.player.Player;
import com.fury.network.packet.Packet;
import com.fury.network.packet.PacketListener;

/**
 * This packet listener is called when a player's region has been loaded.
 * 
 * @author relex lawl
 */

public class FinalizedMapRegionChangePacketListener implements PacketListener {

	@Override
	public void handleMessage(Player player, Packet packet) {
		if (!player.clientHasLoadedMapRegion()) {
			// load objects and items here
			player.setClientLoadedMapRegion(true);
//			player.refreshSpawnedObjects();
//			PrivateObjectManager.refreshObjects(player);
//			player.refreshSpawnedItems();
		}
	}
}