package com.fury.game.content.dialogue.input.impl;

import com.fury.game.content.dialogue.input.EnterAmount;
import com.fury.game.content.eco.ge.GrandExchange;
import com.fury.core.model.node.entity.actor.figure.player.Player;

public class EnterGeQuantity extends EnterAmount {

	@Override
	public void handleAmount(Player player, int amount) {
		GrandExchange.setQuantity(player, amount);
	}

}
