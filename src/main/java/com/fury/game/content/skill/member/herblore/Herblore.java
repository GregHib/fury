package com.fury.game.content.skill.member.herblore;

import com.fury.cache.Revision;
import com.fury.game.content.misc.items.StrangeRocks;
import com.fury.game.content.dialogue.impl.misc.SimpleMessageD;
import com.fury.game.content.dialogue.impl.skills.herblore.HerbloreD;
import com.fury.game.content.global.Achievements;
import com.fury.game.content.global.Achievements.AchievementData;
import com.fury.game.content.global.action.Action;
import com.fury.game.content.global.events.christmas.ChristmasEvent;
import com.fury.game.content.skill.Skill;
import com.fury.core.model.node.entity.actor.figure.player.Player;
import com.fury.core.model.item.Item;
import com.fury.game.world.update.flag.block.Animation;

public class Herblore extends Action {

	public static final Item VIAL_OF_WATER = new Item(227);
	public static final Item DUNGEONEERING_VIAL = new Item(17490);
	public static final Item EMPTY_VIAL = new Item(229);
	public static final Item CUP_OF_HOT_WATER = new Item(4460);
	public static final Item COCONUT_MILK = new Item(5935);
	public static final Item PESTLE_AND_MORTAR = new Item(233);
	public static final Item SWAMP_TAR = new Item(1939);
	public static final Animation ANIMATION = new Animation(363);
	public static final Item POTION_FLASK = new Item(23191, Revision.PRE_RS3);

	private Item node;
	private Item otherItem;
	private Ingredients ingredients;
	private RawIngredients rawIngredients;
	private int ticks;
	private byte slot;

	public Herblore(Item node, Item otherNode, int amount) {
		int nodeId = node.getId();
		int otherNodeId = otherNode.getId();

		this.node = new Item(nodeId);
		this.otherItem = new Item(otherNodeId);
		this.ticks = amount;
		if (nodeId == PESTLE_AND_MORTAR.getId() || otherNodeId == PESTLE_AND_MORTAR.getId()) {
			this.rawIngredients = RawIngredients.forId(nodeId);
			if (rawIngredients == null) {
				rawIngredients = RawIngredients.forId(otherNodeId);
				this.node = new Item(otherNodeId);
				this.otherItem = new Item(nodeId);
			}
		} else {
			this.ingredients = Ingredients.forId(nodeId);
			if (ingredients == null) {
				ingredients = Ingredients.forId(otherNodeId);
				this.node = new Item(otherNodeId);
				this.otherItem = new Item(nodeId);
			}
		}
	}

	@Override
	public boolean start(Player player) {
		if (player == null || node == null)
			return false;

		if ((ingredients == null && rawIngredients == null) || otherItem == null)
			return false;

		if (ingredients != null) {
			this.slot = ingredients.getSlot(otherItem.getId());
			if (slot == -1)
				this.slot = ingredients.getSlot(node.getId());
			if (!player.getSkills().hasRequirement(Skill.HERBLORE, ingredients.getLevels()[slot], "combine these ingredients"))
				return false;
			return true;
		}
		return true;
	}

	@Override
	public boolean process(Player player) {
		if (ingredients == Ingredients.TORSTOL && !otherItem.isEqual(VIAL_OF_WATER))
			if (!player.getInventory().contains(new Item(15309)) || !player.getInventory().contains(new Item(15313)) || !player.getInventory().contains(new Item(15317)) || !player.getInventory().contains(new Item(15321)) || !player.getInventory().contains(new Item(15325)))
				return false;

        return !(player.getInventory().getAmount(node) < getRequiredAmount(node.getId()) || player.getInventory().getAmount(otherItem) < getRequiredAmount(otherItem.getId()));
    }

	private int getRequiredAmount(int id) {
		if (id == 12539)
			return 5;
		else if (id == 1939 || (id >= 10142 && id <= 10145))
			return 15;
		return 1;
	}

	@Override
	public int processWithDelay(Player player) {
		ticks--;
		Item nodeItem = new Item(node.getId(), getRequiredAmount(node.getId()));
		Item other = rawIngredients == null ? new Item(otherItem.getId(), getRequiredAmount(otherItem.getId())) : null;
		player.getInventory().delete(nodeItem, other);

		Item newNode = rawIngredients != null ? rawIngredients.getCrushedItem() : new Item(ingredients.getRewards()[slot]);
		newNode.setAmount(rawIngredients != null ? rawIngredients.getCrushedItem().getAmount() : getRequiredAmount(newNode.getId()));
		player.getInventory().add(newNode);

		player.getSkills().addExperience(Skill.HERBLORE, rawIngredients != null ? 0 : ingredients.getExperience()[slot]);

		StrangeRocks.handleStrangeRocks(player, Skill.HERBLORE);
		ChristmasEvent.giveSnowflake(player);

		if (node.isEqual(PESTLE_AND_MORTAR) || otherItem.isEqual(PESTLE_AND_MORTAR))
			player.animate(364);
		else
			player.animate(363);

		if (otherItem.isEqual(VIAL_OF_WATER) || otherItem.isEqual(COCONUT_MILK) || node.isEqual(VIAL_OF_WATER) || node.isEqual(COCONUT_MILK)) {
			player.message("You add the " + node.getName().toLowerCase().replace("clean", "") + " into the vial of " + (otherItem.isEqual(VIAL_OF_WATER) ? "water." : "milk."), true);
		} else if (otherItem.isEqual(SWAMP_TAR) || node.isEqual(SWAMP_TAR)) {
			player.message("You add the " + node.getName().toLowerCase().replace("clean ", "") + " on the swamp tar.", true);
		} else if (otherItem.isEqual(PESTLE_AND_MORTAR) || node.isEqual(PESTLE_AND_MORTAR)) {
			player.message("You crush the " + node.getName().toLowerCase() + " with your pestle and mortar.", true);
			if(rawIngredients == RawIngredients.BIRDS_NEST)
				Achievements.finishAchievement(player, AchievementData.CRUSH_A_BIRDS_NEST);
			else if(rawIngredients == RawIngredients.BLUE_DRAGON_SCALES)
				Achievements.finishAchievement(player, AchievementData.GRIND_BLUE_SCALE_DUST);
		} else if (ingredients == Ingredients.TORSTOL && !otherItem.isEqual(VIAL_OF_WATER)) {
			player.message("You combine the torstol with the potions and get an overload.", true);
			for (int id = 15325; id >= 15309; id -= 4) {
				if (id == node.getId() || id == otherItem.getId())
					continue;
				player.getInventory().delete(new Item(id));
			}
			Achievements.doProgress(player, AchievementData.MAKE_100_OVERLOADS);
		} else
			player.message("You mix the " + node.getName().toLowerCase() + " into your potion.", true);

		if (ticks > 0)
			return 1;
		return -1;
	}

	@Override
	public void stop(final Player player) {
		setActionDelay(player, 3);
	}

	public static boolean isIngredient(Item item) {
		return Ingredients.forId(item.getId()) != null;
	}

	public static Item isHerbloreSkill(Item first, Item other) {
		Item swap = first;
		Ingredients ingredient = Ingredients.forId(first.getId());
		if (ingredient == null) {
			ingredient = Ingredients.forId(other.getId());
			first = other;
			other = swap;
		}
		if (ingredient != null) {
			int slot = ingredient.getSlot(other.getId());
			return slot > -1 ? new Item(ingredient.getRewards()[slot]) : null;
		}
		swap = first;
		RawIngredients raw = RawIngredients.forId(first.getId());
		if (raw == null) {
			raw = RawIngredients.forId(other.getId());
			first = other;
			other = swap;
		}
		if (raw != null)
			return raw.getCrushedItem();
		return null;
	}

	public static boolean isRawIngredient(Player player, Item item) {
		RawIngredients ing = RawIngredients.forId(item.getId());
		if (ing == null)
			return false;
		if (!player.getInventory().contains(PESTLE_AND_MORTAR)) {
			player.getDialogueManager().startDialogue(new SimpleMessageD(), "You need a pestle and mortar in order to crush a raw ingredient.");
			return false;
		}
		if(player.getInventory().getAmount(item) == 1) {
			player.getActionManager().setAction(new Herblore(item, PESTLE_AND_MORTAR, 1));
		} else
			player.getDialogueManager().startDialogue(new HerbloreD(), ing.getCrushedItem(), item, PESTLE_AND_MORTAR, true);
		return true;
	}
}
