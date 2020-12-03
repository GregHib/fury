package com.fury.game.content.skill.free.dungeoneering.skills;


import com.fury.core.model.node.entity.actor.figure.player.Player;
import com.fury.game.content.skill.Skill;
import com.fury.game.content.skill.free.mining.MiningBase;
import com.fury.game.content.skill.free.mining.data.Pickaxe;
import com.fury.core.model.item.Item;
import com.fury.game.entity.object.GameObject;
import com.fury.game.node.entity.actor.object.ObjectManager;
import com.fury.game.world.update.flag.block.Animation;
import com.fury.util.Misc;

public class DungeoneeringMining extends MiningBase {

	public static enum DungeoneeringRocks {
		NOVITE_ORE(1, 15, new Item(17630), 10, 1),
		BATHUS_ORE(10, 27.5, new Item(17632), 15, 1),
		MARMAROS_ORE(20, 41, new Item(17634), 25, 1),
		KRATONIUM_ORE(30, 56.5, new Item(17636), 50, 5),
		FRACTITE_ORE(40, 71, new Item(17638), 80, 10),
		ZEPHYRIUM_ORE(50, 85, new Item(17640), 95, 10),
		AGRONITE_ORE(60, 100.5, new Item(17642), 100, 15),
		KATAGON_ORE(70, 117, new Item(17644), 110, 20),
		GORGONITE_ORE(80, 131, new Item(17646), 123, 22),
		PROMETHIUM_ORE(90, 148, new Item(17648), 130, 25);

		private int level;
		private double xp;
		private Item ore;
		private int oreBaseTime;
		private int oreRandomTime;

		private DungeoneeringRocks(int level, double xp, Item ore, int oreBaseTime, int oreRandomTime) {
			this.level = level;
			this.xp = xp;
			this.ore = ore;
			this.oreBaseTime = oreBaseTime;
			this.oreRandomTime = oreRandomTime;
		}

		public int getLevel() {
			return level;
		}

		public double getXp() {
			return xp;
		}

		public Item getOre() {
			return ore;
		}

		public int getOreBaseTime() {
			return oreBaseTime;
		}

		public int getOreRandomTime() {
			return oreRandomTime;
		}

	}

	private GameObject rock;
	private DungeoneeringRocks definitions;
	private Pickaxe axeDefinitions;

	public DungeoneeringMining(GameObject rock, DungeoneeringRocks definitions) {
		this.rock = rock;
		this.definitions = definitions;
	}

	@Override
	public boolean start(Player player) {
		axeDefinitions = getPickaxe(player, true);
		if (!checkAll(player))
			return false;
		player.message("You swing your pickaxe at the rock.", true);
		setActionDelay(player, getMiningDelay(player));
		return true;
	}

	private int getMiningDelay(Player player) {
		int mineTimer = definitions.getOreBaseTime() - player.getSkills().getLevel(Skill.MINING) - Misc.getRandom(axeDefinitions.getPickAxeTime());
		if (mineTimer < 1 + definitions.getOreRandomTime())
			mineTimer = 1 + Misc.getRandom(definitions.getOreRandomTime());
		mineTimer /= player.getAuraManager().getMininingAccurayMultiplier();
		return mineTimer;
	}

	private boolean checkAll(Player player) {
		if (axeDefinitions == null) {
			player.message("You do not have a pickaxe or do not have the required level to use the pickaxe.");
			return false;
		}
		if (!hasMiningLevel(player))
			return false;
		if (player.getInventory().getSpaces() <= 0) {
			player.message("Not enough space in your inventory.");
			return false;
		}
		return true;
	}

	private boolean hasMiningLevel(Player player) {
		if (definitions.getLevel() > player.getSkills().getLevel(Skill.MINING)) {
			player.message("You need a mining level of " + definitions.getLevel() + " to mine this rock.");
			return false;
		}
		return true;
	}

	@Override
	public boolean process(Player player) {
		player.perform(new Animation(axeDefinitions.getAnimationId()));
		return true;//checkRock(player);//TODO?
	}

	@Override
	public int processWithDelay(Player player) {
		addOre(player);
		if (Misc.random(5) == 0) {
			ObjectManager.spawnObject(new GameObject(rock.getId() + 1, rock));
			player.message("You have depleted this resource.");
			player.animate(-1);
			return -1;
		}
		if (player.getInventory().getSpaces() <= 0 && definitions.getOre().getId() != -1) {
			player.animate(-1);
			player.message("Not enough space in your inventory.");
			return -1;
		}
		return getMiningDelay(player);
	}

	private void addOre(Player player) {
		double xpBoost = 1.0;
		player.getSkills().addExperience(Skill.MINING, definitions.getXp() * xpBoost);
		player.getInventory().add(definitions.getOre());
		String oreName = definitions.getOre().getDefinition().getName().toLowerCase();
		player.message("You mine some " + oreName + ".", true);
	}

	private boolean checkRock(Player player) {
		return ObjectManager.containsObjectWithId(rock, rock.getId());
	}
}
