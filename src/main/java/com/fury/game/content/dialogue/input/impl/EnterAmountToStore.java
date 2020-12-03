package com.fury.game.content.dialogue.input.impl;

import com.fury.game.container.action.impl.BeastOfBurdenInventoryActions;
import com.fury.game.content.dialogue.input.EnterAmount;
import com.fury.core.model.node.entity.actor.figure.player.Player;

public class EnterAmountToStore extends EnterAmount {

	public EnterAmountToStore(int item, int slot) {
		super(item, slot);
	}

	@Override
	public void handleAmount(Player player, int amount) {
		BeastOfBurdenInventoryActions.deposit(player, getItem(), getSlot(), amount, false);
	}
}
