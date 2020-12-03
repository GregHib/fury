package com.fury.game.content.dialogue.input.impl;

import com.fury.game.container.action.impl.PriceCheckerActions;
import com.fury.game.content.dialogue.input.EnterAmount;
import com.fury.core.model.node.entity.actor.figure.player.Player;

public class EnterAmountToRemoveFromPriceCheck extends EnterAmount {

	public EnterAmountToRemoveFromPriceCheck(int item, int slot) {
		super(item, slot);
	}

	@Override
	public void handleAmount(Player player, int amount) {
		PriceCheckerActions.remove(player, getItem(), getSlot(), amount, false);
	}
}
