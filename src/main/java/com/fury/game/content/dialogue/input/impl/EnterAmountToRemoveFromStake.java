package com.fury.game.content.dialogue.input.impl;

import com.fury.game.content.dialogue.input.EnterAmount;
import com.fury.core.model.node.entity.actor.figure.player.Player;
import com.fury.core.model.item.Item;

public class EnterAmountToRemoveFromStake extends EnterAmount {

	public EnterAmountToRemoveFromStake(int item) {
		super(item);
	}
	
	@Override
	public void handleAmount(Player player, int amount) {
		if (player.getDuelConfigurations() != null) {
			player.getDuelConfigurations().removeStake(player, getSlot(), player.getInventory().getAmount(new Item(getItem())));
		} else
			player.getPacketSender().sendInterfaceRemoval();
	}

}
