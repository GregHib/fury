package com.fury.game.content.dialogue.input.impl;

import com.fury.core.model.item.Item;
import com.fury.game.content.dialogue.input.EnterAmount;
import com.fury.core.model.node.entity.actor.figure.player.Player;

public class EnterAmountToBank extends EnterAmount {

	public EnterAmountToBank(int item, int slot) {
		super(item, slot);
	}

	@Override
	public void handleAmount(Player player, int amount) {
		if (!player.getBank().banking() || player.isCommandViewing())
			return;
		if (player.getInventory().validate(getItem(), getSlot())) {
			Item item = player.getInventory().get(getSlot());
			int currentAmount = player.getInventory().getAmount(item);
			player.getBank().setWithdrawAmount(amount);
			if(amount > currentAmount)
				amount = currentAmount;
			if(amount <= 0)
				return;
			item.setAmount(amount);
			player.getBank().deposit(item, player.getInventory());
			player.getBank().refresh();
			player.getInventory().refresh();
		}
	}
}
