package com.fury.game.content.dialogue.input.impl;

import com.fury.game.content.controller.impl.duel.DuelController;
import com.fury.game.content.dialogue.input.EnterAmount;
import com.fury.core.model.node.entity.actor.figure.player.Player;

public class EnterAmountToStake extends EnterAmount {

	public EnterAmountToStake(int item, int slot) {
		super(item, slot);
	}
	
	@Override
	public void handleAmount(Player player, int amount) {
		if(player.getControllerManager().getController() instanceof DuelController){
			player.getDuelConfigurations().offerStake(player, getSlot(), getItem());
		} else
			player.getPacketSender().sendInterfaceRemoval();
	}

}
