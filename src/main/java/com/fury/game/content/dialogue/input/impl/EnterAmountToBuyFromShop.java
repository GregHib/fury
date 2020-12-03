package com.fury.game.content.dialogue.input.impl;

import com.fury.game.container.action.impl.ShopActions;
import com.fury.game.content.dialogue.input.EnterAmount;
import com.fury.core.model.node.entity.actor.figure.player.Player;

public class EnterAmountToBuyFromShop extends EnterAmount {

	public EnterAmountToBuyFromShop(int item, int slot) {
		super(item, slot);
	}
	
	@Override
	public void handleAmount(Player player, int amount) {
		ShopActions.buy(player, getItem(), getSlot(), amount);
	}

}
