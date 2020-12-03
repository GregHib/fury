package com.fury.game.content.dialogue.input.impl;

import com.fury.game.container.action.impl.TradeInventoryActions;
import com.fury.game.content.dialogue.input.EnterAmount;
import com.fury.core.model.node.entity.actor.figure.player.Player;

public class EnterAmountToTrade extends EnterAmount {

	public EnterAmountToTrade(int item, int slot) {
		super(item, slot);
	}
	
	@Override
	public void handleAmount(Player player, int amount) {
		if (player.getTrade().inTrade() && player.getInterfaceId() == 3323)
			TradeInventoryActions.trade(player, getItem(), getSlot(), amount, false);
	}

}
