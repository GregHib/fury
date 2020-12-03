package com.fury.network.packet.impl;

import com.fury.core.model.node.entity.actor.figure.player.Player;
import com.fury.game.system.communication.clans.ClanChatManager;
import com.fury.game.system.communication.punishment.Punishment;
import com.fury.network.packet.Packet;
import com.fury.network.packet.PacketListener;
import com.fury.util.Misc;

public class SendClanChatMessagePacketListener implements PacketListener {

	@Override
	public void handleMessage(Player player, Packet packet) {
		String clanMessage = packet.readString();
		if(clanMessage == null || clanMessage.length() < 1)
			return;
		if(Punishment.isMuted(player) || Punishment.isHardwareMuted(player)) {
			player.message("You are muted and cannot chat.");
			return;
		}
		if(Misc.blockedWord(clanMessage)) {
			player.getDialogueManager().sendStatement("A word was blocked in your sentence. Please do not repeat it!");
			return;
		}
		ClanChatManager.sendMessage(player, clanMessage);
	}

}
