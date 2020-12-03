package com.fury.game.content.skill.free.dungeoneering.skills;

import com.fury.core.model.item.Item;
import com.fury.game.content.skill.Skill;
import com.fury.core.model.node.entity.actor.figure.player.Player;

public class DungeoneeringStaves {

	private static final Item[] EMPTY_STAVES =
	{ new Item(16977), new Item(16979), new Item(16981), new Item(16983), new Item(16985), new Item(16987), new Item(16989), new Item(16991), new Item(16993), new Item(16995 )};
	private static final int[] LEVELS =
	{ 10, 20, 30, 40, 50, 60, 70, 80, 90, 99 };
	private static final double[] EXPERIENCE =
	{ 5.5, 12, 20.5, 29, 39.5, 51, 63.5, 76, 90.5, 106 };

	private final int index;
	private int cycles;

	public DungeoneeringStaves(int index, int cycles) {
		this.index = index;
		this.cycles = cycles;
	}

	//@Override
	public boolean start(Player player) {
		int levelReq = LEVELS[index];
		if (player.getSkills().getLevel(Skill.RUNECRAFTING) < levelReq) {
			//player.getDialogueManager().startDialogue(new SimpleMessageD", "You need a Runecrafting level of " + levelReq + " in order to imbue this stave.");
			return false;
		}
		int staves = getUsableStaves(player, index);
		if (staves == 0)
			return false;
		if (cycles < staves)
			cycles = staves;
		if (cycles > 28)
			cycles = 28;
		return true;
	}

	//@Override
	public boolean process(Player player) {
		return cycles > 0;
	}

	//@Override
	public int processWithDelay(Player player) {
		cycles--;

		Item stave = getNextStave(player, index);
		if (stave == null)
			return -1;

		player.animate(13662);

		player.getInventory().delete(stave);
		//player.getInventory().add(new Item(DungRunecraftingD.RUNES[3][index]));

		double experience = EXPERIENCE[index];
		player.getSkills().addExperience(Skill.RUNECRAFTING, experience);
		player.getSkills().addExperience(Skill.MAGIC, experience);
		return 3;
	}

	//@Override
	public void stop(Player player) {
		//setActionDelay(player, 3);
	}

	private int getUsableStaves(Player player, int beginningIndex) {
		int staves = 0;
		for (int i = beginningIndex; i < EMPTY_STAVES.length; i++) {
			staves += player.getInventory().getAmount(EMPTY_STAVES[i]);
		}
		return staves;
	}

	private Item getNextStave(Player player, int beginningIndex) {
		for (int i = beginningIndex; i < EMPTY_STAVES.length; i++) {
			Item stave = EMPTY_STAVES[i];
			if (player.getInventory().contains(stave))
				return stave;
		}
		return null;
	}
}
