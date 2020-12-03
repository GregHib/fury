package com.fury.game.content.dialogue.input.impl;

import com.fury.game.container.action.impl.BankActions;
import com.fury.game.content.dialogue.input.EnterAmount;
import com.fury.core.model.node.entity.actor.figure.player.Player;

public class EnterAmountToRemoveFromBank extends EnterAmount {


	public EnterAmountToRemoveFromBank(int item, int slot) {
		super(item, slot);
	}

	@Override
	public void handleAmount(Player player, int amount) {
		if(amount <= 0)
			return;

		if(BankActions.withdrawAmount(player, getItem(), getSlot(), amount, false, false))
			player.getBank().setWithdrawAmount(amount);
	}
}
