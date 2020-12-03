package com.fury.game.content.skill.member.fletching;

import com.fury.game.content.misc.items.StrangeRocks;
import com.fury.game.content.global.Achievements;
import com.fury.game.content.global.action.Action;
import com.fury.game.content.global.events.christmas.ChristmasEvent;
import com.fury.game.content.skill.Skill;
import com.fury.core.model.node.entity.actor.figure.player.Player;
import com.fury.core.model.item.Item;

/**
 * Handles the Fletching skill
 * @author Gabriel Hannason
 *
 */
public class Fletching extends Action {

	public static final Item KNIFE = new Item(946), CHISLE = new Item(1755), BOW_STRING = new Item(1777), CROSSBOW_STRING = new Item(9438);
	public static final Item DUNGEONEERING_KNIFE = new Item(17754), DUNGEEONEERING_BOW_STRING = new Item(17752), DUNGEONEERING_HEADLESS = new Item(17747);

	private FletchingData fletchingData;
	private int option, ticks;
	private boolean doubleItem;

	public Fletching(FletchingData fletchingData, int option, int ticks) {
		this.fletchingData = fletchingData;
		this.option = option;
		this.ticks = ticks;
		doubleItem = !fletchingData.getSelected().isEqual(KNIFE) && !fletchingData.getSelected().isEqual(DUNGEONEERING_KNIFE) && !fletchingData.getSelected().isEqual(CHISLE);
	}

	public static FletchingData isFletchingCombination(Item usedWith, Item itemUsed) {
		for(FletchingData f : FletchingData.values()) {
			if((usedWith.isEqual(f.getItem()) && itemUsed.isEqual(f.getSelected()))
					|| (itemUsed.isEqual(f.getItem()) && usedWith.isEqual(f.getSelected())))
				return f;
		}
		return null;
	}

	@Override
	public boolean start(Player player) {
		if (option >= fletchingData.getProducts().length)
			return false;
		if (!process(player))
			return false;

		if(doubleItem && fletchingData.getProducts()[option].getDefinition().isStackable() && !player.getInventory().hasRoom() && !player.getInventory().contains(fletchingData.getProducts()[option])) {
			player.getInventory().full();
			return false;
		}

		if (!player.getSlayerManager().getLearnt()[0]) {
			if (fletchingData == FletchingData.BROAD_ARROWS || fletchingData == FletchingData.BROAD_BOLTS) {
				player.message("You lack the knowledge to create a broad accessory, perhaps a Slayer Master could assist you.");
				return false;
			}
		}

		if (!player.getInventory().containsAmount(fletchingData.getSelected()) || !player.getInventory().containsAmount(fletchingData.getItem())) {
			player.message("You do not have enough resources to fletch this.");
			return false;
		}

		player.message("You attempt to create a " + fletchingData.getProducts()[option].getName().replace("(u)", "") + "...", true);
		return true;
	}

	@Override
	public boolean process(Player player) {
		if (ticks <= 0)
			return false;

		if (!player.getInventory().containsAmount(fletchingData.getSelected()) || !player.getInventory().containsAmount(fletchingData.getItem())) {
			player.message("You have run out of resources.");
			return false;
		}

		if (!player.getSkills().hasRequirement(Skill.FLETCHING, fletchingData.getLevels()[option], "fletch this."))
			return false;

		return true;
	}

	@Override
	public int processWithDelay(Player player) {
		ticks--;

		if(fletchingData.getAnimation() != null)
			player.perform(fletchingData.getAnimation());

		if(fletchingData.getGraphic() != null)
			player.perform(fletchingData.getGraphic());

		player.getInventory().delete(fletchingData.getItem());
		if (doubleItem)
			player.getInventory().delete(fletchingData.getSelected());

		player.getInventory().addSafe(fletchingData.getProducts()[option]);

		StrangeRocks.handleStrangeRocks(player, Skill.FLETCHING);

		player.getSkills().addExperience(Skill.FLETCHING, fletchingData.getXp()[option] * fletchingData.getProducts()[option].getAmount());
		player.message("You successfully create a " + fletchingData.getProducts()[option].getName().replace("(u)", "") + ".", true);


		if(fletchingData == FletchingData.REGULAR_BOW && fletchingData.getProducts()[option].getId() == 52)
			Achievements.finishAchievement(player, Achievements.AchievementData.FLETCH_ARROW_SHAFTS);
		else if(fletchingData == FletchingData.BRONZE_ARROWS || fletchingData == FletchingData.IRON_ARROWS || fletchingData == FletchingData.STEEL_ARROWS || fletchingData == FletchingData.MITHRIL_ARROWS || fletchingData == FletchingData.ADAMANT_ARROWS || fletchingData == FletchingData.RUNITE_ARROWS)
			Achievements.doProgress(player, Achievements.AchievementData.FLETCH_100_ARROWS, fletchingData.getProducts()[option].getAmount());

		ChristmasEvent.giveSnowflake(player);
		return fletchingData == FletchingData.BOLAS || fletchingData == FletchingData.SAGIE ? 5 : 3;
	}

	@Override
	public void stop(final Player player) {
		setActionDelay(player, 3);
	}
}
