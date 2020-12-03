package com.fury.game.content.dialogue.input.impl;

import com.fury.game.container.action.impl.BeastOfBurdenActions;
import com.fury.game.content.dialogue.input.EnterAmount;
import com.fury.core.model.node.entity.actor.figure.player.Player;

public class EnterAmountToRemoveFromBob extends EnterAmount {

	public EnterAmountToRemoveFromBob(int item, int slot) {
		super(item, slot);
	}

	@Override
	public void handleAmount(Player player, int amount) {
		BeastOfBurdenActions.withdraw(player, getItem(), getSlot(), amount, false);
	}
}
