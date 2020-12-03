package com.fury.game.content.dialogue.input.impl;

import com.fury.game.content.dialogue.input.Input;
import com.fury.core.model.node.entity.actor.figure.player.Player;

public class EnterSyntaxToBankSearchFor extends Input {

	@Override
	public void handleSyntax(Player player, String syntax) {
		boolean search = player.isBanking() && player.getBank().isSearching();
		if(search)
			player.getBank().getSearch().search(syntax);
	}
}
