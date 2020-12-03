package com.fury.network.packet.impl;

import com.fury.game.entity.character.player.info.PlayerRights;
import com.fury.network.packet.Packet;
import com.fury.network.packet.PacketListener;
import com.fury.game.system.communication.clans.ClanChatManager;
import com.fury.core.model.node.entity.actor.figure.player.Player;

public class ClickTextMenuPacketListener implements PacketListener {

	@Override
	public void handleMessage(Player player, Packet packet) {
		int widget = packet.readInt();
		int menu = packet.readByte();

		if(player.getRights() == PlayerRights.DEVELOPER)
			player.getPacketSender().sendConsoleMessage("Clicked text menu: " + widget + ", menuId: " + menu);

		if(widget >= 29344 && widget <= 29443) { // Clan chat list
			int index = widget - 29344;
			ClanChatManager.handleMemberOption(player, index, menu);
		}

		if(widget >= 55213 && widget <= 55302) {//Notes tab click
			switch(menu) {
			case 4://Select
				break;
			case 3://Edit
				break;
			case 2://Colour
				break;
			case 1://Delete
				break;
			}
		}

		if(widget >= 27015 && widget <= 27022) {
			player.getBank().collapse(widget - 27014);
			player.getBank().refresh();
		}

		if(ClanChatManager.handleClanChatSetupButton(player, widget, menu))
			return;
		
	}

	public static final int OPCODE = 222;
}
