package com.fury.game.content.dialogue.input.impl;

import com.fury.game.content.dialogue.input.EnterAmount;
import com.fury.game.content.skill.free.crafting.leather.CraftLeather;
import com.fury.game.content.skill.free.crafting.leather.LeatherData;
import com.fury.core.model.node.entity.actor.figure.player.Player;

public class EnterAmountOfLeatherToCraft extends EnterAmount {
	
	@Override
	public void handleAmount(Player player, int amount) {
		for (final LeatherData l : LeatherData.values()) {
			if (player.getSelectedSkillingItem().isEqual(l.getLeather())) {
				player.getActionManager().setAction(new CraftLeather(l, amount));
				break;
			}
		}
	}
}
