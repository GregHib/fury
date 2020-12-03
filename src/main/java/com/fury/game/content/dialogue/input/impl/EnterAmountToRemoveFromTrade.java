package com.fury.game.content.dialogue.input.impl;

import com.fury.game.container.action.impl.TradeActions;
import com.fury.game.content.dialogue.input.EnterAmount;
import com.fury.core.model.node.entity.actor.figure.player.Player;

public class EnterAmountToRemoveFromTrade extends EnterAmount {

	public EnterAmountToRemoveFromTrade(int item, int slot) {
		super(item, slot);
	}
	
	@Override
	public void handleAmount(Player player, int amount) {
		TradeActions.remove(player, getItem(), getSlot(), amount, false);
	}
}
