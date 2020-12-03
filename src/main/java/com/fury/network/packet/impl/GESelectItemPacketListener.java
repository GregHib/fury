package com.fury.network.packet.impl;

import com.fury.cache.Revision;
import com.fury.cache.def.item.ItemDefinition;
import com.fury.game.content.eco.ge.GrandExchange;
import com.fury.core.model.node.entity.actor.figure.player.Player;
import com.fury.core.model.item.Item;
import com.fury.network.packet.Packet;
import com.fury.network.packet.PacketListener;

public class GESelectItemPacketListener implements PacketListener {

	@Override
	public void handleMessage(Player player, Packet packet) {
		int id = packet.readInt();
		Revision revision = Revision.values()[packet.readByte()];
		Item item = new Item(id, revision);
		ItemDefinition def = item.getDefinition();
		if(def != null) {
			if(def.getValue() <= 0 || !item.tradeable() || item.getId() == 995) {
				player.message("This item can currently not be purchased or sold in the Grand Exchange.");
				return;
			}
			GrandExchange.setSelectedItem(player, item);
		}
	}

}
