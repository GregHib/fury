package com.fury.game.content.skill.free.smithing;

import com.fury.game.container.impl.equip.Equipment;
import com.fury.game.container.impl.equip.Slot;
import com.fury.game.content.dialogue.impl.skills.dungeoneering.DungSmeltingD;
import com.fury.game.content.dialogue.impl.skills.smithing.SmeltingD;
import com.fury.game.content.global.Achievements;
import com.fury.game.content.global.Achievements.AchievementData;
import com.fury.game.content.global.action.Action;
import com.fury.game.content.skill.Skill;
import com.fury.core.model.node.entity.actor.figure.player.Player;
import com.fury.game.entity.character.player.content.Sounds;
import com.fury.game.entity.character.player.content.Sounds.Sound;
import com.fury.core.model.item.Item;
import com.fury.game.node.entity.actor.figure.player.Variables;
import com.fury.util.Misc;

/*
 * Rewrote by Greg
 */
public class Smelting extends Action {

	private SmeltingData bar;
	private int amountMade = 0;
	private int amount;

	public Smelting(SmeltingData bar, int amount) {
		this.bar = bar;
		this.amount = amount;
	}

	public static void openInterface(Player player) {
		openInterface(player, false);
	}

	public static void openInterface(Player player, boolean dungeoneering) {
		player.stopAll();
		if(!dungeoneering) {
			player.getDialogueManager().startDialogue(new SmeltingD());
		} else
			player.getDialogueManager().startDialogue(new DungSmeltingD());
	}

	public static void smeltBar(final Player player, Item barItem, final int amount) {
		SmeltingData bar = SmeltingData.forId(barItem.getId());
		if(bar == null)
			return;
		
		player.getActionManager().setAction(new Smelting(bar, amount));
	}

	public static boolean checkVarrockArmour(Player player, SmeltingData bar) {
		if(Equipment.wearingVarrockArmour(player, 1) && bar.ordinal() <= 3)
			return Misc.random(10) == 0;
		if(Equipment.wearingVarrockArmour(player, 2) && bar.ordinal() <= 5)
			return Misc.random(10) == 0;
		if(Equipment.wearingVarrockArmour(player, 3) && bar.ordinal() <= 6)
			return Misc.random(10) == 0;
		if(Equipment.wearingVarrockArmour(player, 4) && bar.ordinal() <= 7)
			return Misc.random(10) == 0;
		return false;
	}

	public static void handleBarCreation(Player player, SmeltingData bar) {
		if(player.getOres()[0] > 0) {
			player.getInventory().delete(new Item(player.getOres()[0]));
			if(player.getOres()[1] > 0 && player.getOres()[1] != 453) {
				player.getInventory().delete(new Item(player.getOres()[1]));
			} else if(player.getOres()[1] == 453) {
			    int amount = bar.getCoalAmount();
				if(player.getInventory().contains(new Item(18339))) {
                    int bag = player.getVars().getInt(Variables.COAL_BAG_STORAGE);
                    if(bag > 0) {
                        if(bag < amount) {
                            player.getVars().remove(Variables.COAL_BAG_STORAGE, bag);
                            player.getInventory().delete(new Item(player.getOres()[1], amount - bag));
                        } else
                            player.getVars().remove(Variables.COAL_BAG_STORAGE, amount);
                    } else
                        player.getInventory().delete(new Item(player.getOres()[1], amount - bag));
                } else
					player.getInventory().delete(new Item(player.getOres()[1], amount));
			}
			if(bar != SmeltingData.IRON_BAR) { //Iron bar - 50% success rate
				player.getInventory().add(bar.getBar());
				player.getSkills().addExperience(Skill.SMITHING, bar.getExperience(), player.getEquipment().get(Slot.HANDS).getId() == 776 && bar == SmeltingData.GOLD_BAR ? 2.5 : 1.0);
				if(bar == SmeltingData.BRONZE_BAR)
					Achievements.finishAchievement(player, AchievementData.SMELT_A_BRONZE_BAR);
				else if(bar == SmeltingData.RUNITE_BAR)
					Achievements.doProgress(player, AchievementData.SMELT_1000_RUNE_BARS);
			} else if(SmithingData.ironOreSuccess(player)) {
				player.getInventory().add(bar.getBar());
				player.getSkills().addExperience(Skill.SMITHING, bar.getExperience());
			} else
				player.message("The Iron ore burns too quickly and you're unable to make an Iron bar.");
			Sounds.sendSound(player, Sound.SMELT_ITEM);
		}
	}

	@Override
	public boolean start(Player player) {
		if(!bar.canSmelt(player))
			return false;
		return true;
	}

	@Override
	public boolean process(Player player) {
		return bar.canSmelt(player);
	}

	@Override
	public int processWithDelay(Player player) {
		player.animate(896);

		boolean dbl = checkVarrockArmour(player, bar);
		handleBarCreation(player, bar);
		amountMade++;
		if(amountMade >= amount)
			return -1;

		if(dbl) {
			handleBarCreation(player, bar);
			player.message("You armour allows you to smelt two bars at the same time!");
			amountMade++;
			if(amountMade >= amount)
				return -1;
		}

		return 3;
	}

	@Override
	public void stop(Player player) {
		setActionDelay(player, 3);
	}
}
