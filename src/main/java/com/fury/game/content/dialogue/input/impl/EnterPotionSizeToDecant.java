package com.fury.game.content.dialogue.input.impl;

import com.fury.game.content.dialogue.impl.items.DecantD;
import com.fury.game.content.dialogue.input.EnterAmount;
import com.fury.core.model.node.entity.actor.figure.player.Player;

public class EnterPotionSizeToDecant extends EnterAmount {
	
	@Override
	public void handleAmount(Player player, int amount) {
		if(amount > 0 && amount < 7)
			player.getDialogueManager().startDialogue(new DecantD(), amount);
		else
			player.message("Invalid value.");
	}
	
}
