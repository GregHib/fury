package com.fury.game.content.dialogue.input.impl;

import com.fury.game.content.dialogue.input.EnterAmount;
import com.fury.game.content.skill.free.smithing.Smelting;
import com.fury.game.content.skill.free.smithing.SmeltingData;
import com.fury.core.model.node.entity.actor.figure.player.Player;

public class EnterAmountOfBarsToSmelt extends EnterAmount {

	public EnterAmountOfBarsToSmelt(int bar) {
		this.bars = bar;
	}
	
	@Override
	public void handleAmount(Player player, int amount) {
		SmeltingData bar = SmeltingData.forId(bars);
		if(bar != null)
		Smelting.smeltBar(player, bar.getBar(), amount);
	}
	
	private int bars;
	
	public int getBar() {
		return bars;
	}
	
}
