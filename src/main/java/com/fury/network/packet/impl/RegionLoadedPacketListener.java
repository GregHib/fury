package com.fury.network.packet.impl;

import com.fury.core.model.node.entity.actor.figure.player.Player;
import com.fury.game.entity.character.player.content.Music;
import com.fury.network.packet.Packet;
import com.fury.network.packet.PacketListener;


public class RegionLoadedPacketListener implements PacketListener {

	@Override
	public void handleMessage(Player player, Packet packet) {
		player.loadRegion();
		if(player.isAllowRegionChangePacket()) {
			//player.loadMapRegions();
			//player.refreshSpawnedObjects();
			//player.refreshSpawnedItems();
			//player.getPacketSender().sendMapRegion();
			//CustomObjects.handleRegionChange(player);
//            player.getRegion().region.sendGameObjects().handleRegionEntry(player);//TODO remove, not how it's supposed to be handled
			Music.handleRegionChange(player);
			//if(player.getDungManager() != null)player.getDungManager().handleRegionChange(player);
//			player.getNpcFacesUpdated().clear();
			player.setRegionChange(false);
			player.setAllowRegionChangePacket(false);
		}
	}
}
