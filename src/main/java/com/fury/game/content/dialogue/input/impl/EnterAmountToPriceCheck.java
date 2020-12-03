package com.fury.game.content.dialogue.input.impl;

import com.fury.game.container.action.impl.PriceCheckerInventoryActions;
import com.fury.game.content.dialogue.input.EnterAmount;
import com.fury.core.model.node.entity.actor.figure.player.Player;

public class EnterAmountToPriceCheck extends EnterAmount {

	public EnterAmountToPriceCheck(int item, int slot) {
		super(item, slot);
	}

	@Override
	public void handleAmount(Player player, int amount) {
		PriceCheckerInventoryActions.check(player, getItem(), getSlot(), amount, false);
	}
}
