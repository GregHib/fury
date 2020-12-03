package com.fury.game.content.skill.free.woodcutting;

import com.fury.cache.Revision;
import com.fury.game.content.global.Achievements;
import com.fury.core.model.node.entity.actor.figure.player.Player;
import com.fury.core.model.item.Item;
import com.fury.util.Misc;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


/**
 * @author Greg
 */

public class BirdNests {

	public enum RingRewards {
		GOLD_RING(new Item(1635), 34, 35),
		SAPPHIRE_RING(new Item(1637), 40, 41),
		EMERALD_RING(new Item(1639), 15, 16),
		RUBY_RING(new Item(1641), 8, 9),
		DIAMOND_RING(new Item(1643), 1)
		;

		public Item getItem() {
			return item;
		}

		public double[] getPercentages() {
			return percentages;
		}

		Item item;
		double[] percentages;
		RingRewards(Item item, double percentage) {
			this.item = item;
			percentages = new double[] {percentage};
		}
		static List<RingRewards> rings = new ArrayList<>();

		static {
			for(RingRewards ring : values())
				rings.add(ring);
		}

		RingRewards(Item item, double lowestPercentage, double highestPercentage) {
			this.item = item;
			percentages = new double[] {lowestPercentage, highestPercentage};
		}

		public static RingRewards getRandom() {
			Collections.shuffle(rings);
			for(RingRewards ring : rings) {
				double random = 0;
				if(ring.getPercentages().length > 1)
					random = ring.getPercentages()[Misc.random(ring.getPercentages().length)];
				else
					random = ring.getPercentages()[0];

				if(Misc.random(100) <= random) {
					return ring;
				}
			}
			return null;
		}
	}

	public enum Seed1Rewards {
		ACORN(new Item(5312), 3),
		WILLOW_SEED(new Item(5313), 3),
		MAPLE_SEED(new Item(5314), 1, 2),
		YEW_SEED(new Item(5315), 1, 2),
		MAGIC_SEED(new Item(5316), 0.1, 0.3),
		APPLE_TREE_SEED(new Item(5283), 17),
		BANANA_TREE_SEED(new Item(5284), 10, 11),
		ORANGE_TREE_SEED(new Item(5285), 8, 9),
		CURRY_TREE_SEED(new Item(5286), 6, 7),
		PINEAPPLE_SEED(new Item(5287), 4, 5),
		PAPAYA_TREE_SEED(new Item(5288), 3, 4),
		PALM_TREE_SEED(new Item(5289), 2),
		CALQUAT_TREE_SEED(new Item(5290), 2),
		SPIRIT_SEED(new Item(5317), 2);

		public Item getItem() {
			return item;
		}

		public double[] getPercentages() {
			return percentages;
		}

		Item item;
		double[] percentages;
		Seed1Rewards(Item item, double percentage) {
			this.item = item;
			percentages = new double[] {percentage};
		}
		static List<Seed1Rewards> seeds = new ArrayList<>();

		static {
			for(Seed1Rewards seed : values())
				seeds.add(seed);
		}

		Seed1Rewards(Item item, double lowestPercentage, double highestPercentage) {
			this.item = item;
			percentages = new double[] {lowestPercentage, highestPercentage};
		}

		public static Seed1Rewards getRandom() {
			Collections.shuffle(seeds);
			for(Seed1Rewards seed : seeds) {
				double random = 0;
				if(seed.getPercentages().length > 1)
					random = seed.getPercentages()[Misc.random(seed.getPercentages().length)];
				else
					random = seed.getPercentages()[0];

				if(Misc.random(100) <= random) {
					return seed;
				}
			}
			return null;
		}
	}

	public enum Seed2Rewards {
		SWEETCORN_SEED(new Item(5320, 3), 15, 16),
		TOMATO_SEED(new Item(5322, 6), 12, 13),
		LIMPWURT_SEED(new Item(5100, 2), 12, 14),
		CABBAGE_SEED(new Item(5324, 9), 11, 12),
		WATERMELON_SEED(new Item(5321, 2), 11, 13),
		EVIL_TURNIP_SEED(new Item(12148, 2), 10, 12),
		STRAWBERRY_SEED(new Item(5323, 3), 10, 11),
		ACORN(new Item(5312), 3),
		RANARR_SEED(new Item(5295), 3),
		WILLOW_SEED(new Item(5313), 3),
		MAPLE_SEED(new Item(5314), 1, 2),
		YEW_SEED(new Item(5315), 1, 2),
		MORCHELLA_MUSHROOM_SPORE(new Item(21620, 2), 0.8, 1.2),
		MAGIC_SEED(new Item(5316), 0.1, 0.3),
		SPIRIT_SEED(new Item(5317), 0, 0.1)
		;

		public Item getItem() {
			return item;
		}

		public double[] getPercentages() {
			return percentages;
		}

		Item item;
		double[] percentages;
		Seed2Rewards(Item item, double percentage) {
			this.item = item;
			percentages = new double[] {percentage};
		}
		static List<Seed2Rewards> seeds = new ArrayList<>();

		static {
			for(Seed2Rewards seed : values())
				seeds.add(seed);
		}

		Seed2Rewards(Item item, double lowestPercentage, double highestPercentage) {
			this.item = item;
			percentages = new double[] {lowestPercentage, highestPercentage};
		}

		public static Seed2Rewards getRandom() {
			Collections.shuffle(seeds);
			for(Seed2Rewards seed : seeds) {
				double random = 0;
				if(seed.getPercentages().length > 1)
					random = seed.getPercentages()[Misc.random(seed.getPercentages().length)];
				else
					random = seed.getPercentages()[0];
				if(Misc.random(100) <= random)
					return seed;
			}
			return null;
		}
	}

	public static final int[] BIRD_NEST_IDS = {5070, 5071, 5072, 5073, 5074, 7413, 11966};
	public static final Item EMPTY = new Item(5075);
	public static final Item RED = new Item(5076);
	public static final Item BLUE = new Item(5077);
	public static final Item GREEN = new Item(5078);


	/**
	 * Check if the item is a nest
	 *
	 */
	public static boolean isNest(final Item item) {
		if(item.getRevision() != Revision.RS2)
			return false;

		for(int nest : BIRD_NEST_IDS) {
			if(nest == item.getId()) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 
	 * Searches the nest.
	 * 
	 */

	public static final void searchNest(Player player, Item item) {
		if(player.getInventory().getSpaces() <= 0) {
			player.message("Your inventory is too full to take anything out of the bird's nest.");
			return;
		}
		player.getInventory().delete(new Item(item, 1));
		eggNest(player, item.getId());
		seedNest(player, item.getId());
		ringNest(player, item.getId());
		player.getInventory().add(EMPTY);
		Achievements.doProgress(player, Achievements.AchievementData.SEARCH_5_BIRDS_NESTS);
	}

	/**
	 * 
	 * Determines what loot you get
	 *  from ring bird nests
	 *  
	 */
	public static final void ringNest(Player p, int itemId){
		if(itemId == 5074) {
			RingRewards reward = RingRewards.getRandom();
			if(reward != null)
				p.getInventory().add(reward.getItem());
		}
	}

	/**
	 * 
	 * Determines what loot you get
	 *  from seed bird nests
	 *  
	 */

	private static final void seedNest(Player p, int itemId){
		if(itemId == 5073) {
			Seed1Rewards reward = Seed1Rewards.getRandom();
			if(reward != null)
				p.getInventory().add(reward.getItem());
		}
		if(itemId == 7413) {
			Seed2Rewards reward = Seed2Rewards.getRandom();
			if(reward != null)
				p.getInventory().add(reward.getItem());
		}
	}

	/**
	 * 
	 * Egg nests
	 * 
	 */

	public static final void eggNest(Player p, int itemId){
		if(itemId == 5070){
			p.getInventory().add(RED);
		}
		if(itemId == 5071){
			p.getInventory().add(GREEN);
		}
		if(itemId == 5072){
			p.getInventory().add(BLUE);
		}
	}

}