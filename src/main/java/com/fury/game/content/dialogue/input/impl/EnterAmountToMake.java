package com.fury.game.content.dialogue.input.impl;

import com.fury.game.content.dialogue.input.EnterAmount;
import com.fury.core.model.node.entity.actor.figure.player.Player;

/**
 * Created by Greg on 16/11/2016.
 */
public class EnterAmountToMake extends EnterAmount {

	public EnterAmountToMake() {

	}

	@Override
	public void handleAmount(Player player, int amount) {
		Object[] params = player.getDialogueManager().getParameters();
		if(params != null && params.length > 0) {
			params[0] = amount;
			player.getDialogueManager().setParameters(params);
			player.getDialogueManager().runDialogue((int) params[1]);
		}
	}
}
