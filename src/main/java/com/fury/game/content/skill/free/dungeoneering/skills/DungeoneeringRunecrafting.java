package com.fury.game.content.skill.free.dungeoneering.skills;

import com.fury.game.content.dialogue.impl.misc.SimpleMessageD;
import com.fury.game.content.skill.free.dungeoneering.DungeonConstants;
import com.fury.game.content.skill.Skill;
import com.fury.game.content.global.action.Action;
import com.fury.core.model.node.entity.actor.figure.player.Player;
import com.fury.core.model.item.Item;

public class DungeoneeringRunecrafting extends Action {

	private final Item rune;
	private final int levelRequirement;
	private final double experience;
	private final int[] multipliers;
	private int cycles;

	public DungeoneeringRunecrafting(int cycles, Item rune, int levelRequirement, double experience, int... multipliers) {
		this.cycles = cycles;
		this.rune = rune;
		this.levelRequirement = levelRequirement;
		this.experience = experience;
		this.multipliers = multipliers;
	}

	@Override
	public boolean start(Player player) {
		player.getPacketSender().sendInterfaceRemoval();
		int actualLevel = player.getSkills().getLevel(Skill.RUNECRAFTING);
		if (actualLevel < levelRequirement) {
			player.getDialogueManager().startDialogue(new SimpleMessageD(), "You need a runecrafting level of " + levelRequirement + " to craft this rune.");
			return false;
		}
		int essence = player.getInventory().getAmount(DungeonConstants.ESSENCE);
		if (essence == 0)
			return false;
		if (cycles > essence)
			cycles = essence;
		return true;
	}

	@Override
	public boolean process(Player player) {
		return cycles > 0;
	}

	@Override
	public int processWithDelay(Player player) {
		boolean incompleteCycle = cycles < 10;

		int cycleCount = incompleteCycle ? cycles : 10;
		cycles -= cycleCount;

		player.animate(13659);
		player.graphic(2571);

		player.getSkills().addExperience(Skill.RUNECRAFTING, cycleCount * experience);
		player.getInventory().delete(new Item(DungeonConstants.ESSENCE, cycleCount));
		for (int i = multipliers.length - 2; i >= 0; i -= 2) {
			if (player.getSkills().getLevel(Skill.RUNECRAFTING) >= multipliers[i]) {
				cycleCount *= multipliers[i + 1];
				break;
			}
		}

		player.getInventory().add(new Item(rune.getId(), cycleCount));
		return 0;
	}

	@Override
	public void stop(Player player) {
		setActionDelay(player, 3);
	}
}
