package com.fury.game.content.dialogue.input.impl;

import com.fury.game.content.controller.impl.JadinkoLair;
import com.fury.game.content.dialogue.input.EnterAmount;
import com.fury.core.model.node.entity.actor.figure.player.Player;
import com.fury.core.model.item.Item;
import com.fury.game.node.entity.actor.figure.player.Points;
import com.fury.util.Misc;

public class EnterRewardsToWithdraw extends EnterAmount {

	private int type;
	public EnterRewardsToWithdraw(int type) {
		super();
		this.type = type;
	}
	
	@Override
	public void handleAmount(Player player, int amount) {
		if (player.getInventory().getSpaces() < amount) {
			player.getInventory().full();
			return;
		}

		for(int i = 0; i < amount; i++) {
			int cost = type == 1 ? 13 : type == 2 ? 15 : 10;
			if (player.getPoints().has(Points.FAVOUR, cost)) {
				int[] reward = type == 2 || (type == 0 && Misc.random(2) == 0) ? getRandomSeed() : getRandomFruit(type == 1);
				JadinkoLair.removePoints(player, cost);
				player.getInventory().addSafe(new Item(reward[0], reward[1]));
			} else {
				player.getDialogueManager().sendDialogue("The stone wont let you take that item without more favours.");
				return;
			}
		}
		player.message("You withdraw your rewards from the offering stone.");
	}

	private int[] getRandomFruit(boolean better) {
		if(Misc.random(better ? 5 : 8) == 0)
			return new int[] {Misc.inclusiveRandom(21376, 21389), 1};
		else
			return new int[] {21376, 1};
	}

	private static final int[] seeds = new int[] {
			5321,
			5100,
			5291,
			5292,
			5293,
			5294,
			5295,
			12176,
			5296,
			5297,
			14870,
			5298,
			5299,
			5300,
            5301,
			5302,
			5303,
			5304,
			21621,
			5106,
			5313,
			5314,
			5315,
			5316,
			5283,
			5284,
			5285,
			5286,
			5287,
			5288,
			5289,
			21620,
			5282,
			5317
	};

	private int[] getRandomSeed() {
		return new int[] {seeds[Misc.random(seeds.length - 1)], Misc.random(1, 10)};
	}
}
