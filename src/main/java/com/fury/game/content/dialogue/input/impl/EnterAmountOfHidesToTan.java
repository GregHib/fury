package com.fury.game.content.dialogue.input.impl;

import com.fury.game.content.dialogue.input.EnterAmount;
import com.fury.game.content.skill.free.crafting.leather.tanning.Tanning;
import com.fury.core.model.node.entity.actor.figure.player.Player;

public class EnterAmountOfHidesToTan extends EnterAmount {

	private int button;
	public EnterAmountOfHidesToTan(int button) {
		this.button = button;
	}
	
	@Override
	public void handleAmount(Player player, int amount) {
		Tanning.tanHide(player, button, amount);
	}

}
