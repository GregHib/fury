package com.fury.game.content.dialogue.input.impl;

import com.fury.game.content.dialogue.input.EnterAmount;
import com.fury.core.model.node.entity.actor.figure.player.Player;

public class EnterAmountToSellToShop extends EnterAmount {

	public EnterAmountToSellToShop(int item, int slot) {
		super(item, slot);
	}
	
	@Override
	public void handleAmount(Player player, int amount) {
		if (player.isShopping())
			player.getShop().sellItem(player, getSlot(), amount);
	}

}
