package com.fury.game.content.dialogue.input.impl;

import com.fury.game.content.dialogue.input.Input;
import com.fury.game.system.communication.clans.ClanChatManager;
import com.fury.core.model.node.entity.actor.figure.player.Player;

public class EnterClanChatToJoin extends Input {

	@Override
	public void handleSyntax(Player player, String syntax) {
		if(syntax.length() <= 1) {
			player.message("Invalid syntax entered.");
			return;
		}
		ClanChatManager.join(player, syntax);
	}
}
