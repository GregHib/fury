package com.fury.game.content.skill.free.dungeoneering.skills;

import com.fury.core.model.node.entity.actor.figure.player.Player;
import com.fury.game.content.skill.free.dungeoneering.DungeonConstants;
import com.fury.game.content.skill.free.dungeoneering.DungeonManager;
import com.fury.core.model.item.Item;
import com.fury.game.entity.object.GameObject;
import com.fury.game.node.entity.actor.object.ObjectManager;
import com.fury.game.world.GameWorld;

public class DungeoneeringFarming {

	public enum Harvest {

		POTATO(17823, 1, 1.3, 17817),

		GISSEL(17824, 34, 3.5, 17819),

		EDICAP(17825, 68, 5.7, 17821),

		SAGEWORT(17826, 7, 15, 17494),

		VALERIAN(17827, 18, 18.1, 17496),

		ALOE(17828, 29, 21.9, 17498),

		WORMWOOD(17829, 40, 27.1, 17500),

		MAGEBANE(17830, 51, 34.4, 17502),

		FEATHERFOIL(17831, 62, 44.5, 17504),

		WINTERS_GRIP(17832, 73, 58.1, 17506),

		LYCOPUS(17833, 84, 75.9, 17508),

		BUCKTHORN(17834, 95, 98.6, 17510),

		SALVE_NETTLES(1, 6.1, 17448),

		WILDERCRESS(10, 9.2, 17450),

		BLIGHTLEAF(20, 12.8, 17452),

		ROSEBLOOD(30, 17.4, 17454),

		BRYLL(40, 23.5, 17456),

		DUSKWEED(50, 31.6, 17458),

		SOULBELL(60, 42.2, 17460),

		ECTOGRASS(70, 55.8, 17462),

		RUNELEAF(80, 72.9, 17464),

		SPIRITBLOOM(90, 94, 17466);

		private final int seed, lvl, product;
		private final double exp;

		Harvest(int seed, int lvl, double exp, int product) {
			this.seed = seed;
			this.lvl = lvl;
			this.exp = exp;
			this.product = product;
		}

		Harvest(int lvl, double exp, int product) {
			this(-1, lvl, exp, product);
		}

		public int getSeed() {
			return seed;
		}

		public int getLvl() {
			return lvl;
		}

		public double getExp() {
			return exp;
		}

		public int getProduct() {
			return product;
		}

		public boolean isTextile() {
			return seed == -1;
		}

		public static Harvest forSeed(int id) {
			for (Harvest harvest : Harvest.values()) {
				if (harvest.seed == id)
					return harvest;
			}
			return null;
		}
	}

	public static int getHerbForLevel(int level) {
		for (int i = 10; i < Harvest.values().length; i++)
			if (Harvest.values()[i].lvl == level)
				return Harvest.values()[i].product;
		return 17448;
	}

	public static void initHarvest(final Player player, final Harvest harvest, final GameObject object) {
		/*Integer harvestCount = (Integer) player.getTemporaryAttributes().get(Key.HARVEST_COUNT);
		final boolean isTextile = harvest.isTextile();
		final String productName = ItemDefinition.getItemDefinitions(harvest.product).getName().toLowerCase();

		if (isTextile) {
			if (player.getSkills().getLevel(Skills.FARMING) < harvest.lvl) {
				player.getPackets().sendMessage("You need a Farming level of " + harvest.lvl + " in order to pick " + productName + ".");
				return;
			}
		}

		if (harvestCount == null)
			harvestCount = Utils.random(2, 5);
		harvestCount--;
		if (!isTextile && !PrivateObjectManager.isPlayerObject(player, object)) {
			player.getPackets().sendMessage("This is not your farming patch.");
			return;
		}
		if (harvestCount == 0) {
			player.getTemporaryAttributes().remove(Key.HARVEST_COUNT);
			if (!isTextile)
				PrivateObjectManager.removeObject(player, object);
			else
				player.getPackets().sendMessage("You have depleted this resource.");
			WorldTasksManager.schedule(new WorldTask() {

				@Override
				public void run() {
					World.spawnObject(new WorldObject(isTextile ? object.getScrollId() + 1 : DungeonConstants.EMPTY_FARMING_PATCH, object.getInfo(), object.getRotation(), object));
				}
			});
			return;
		}
		player.getTemporaryAttributes().put(Key.HARVEST_COUNT, harvestCount);
		player.setNextAnimation(new Animation(3659));
		player.getMovement().lock(2);
		WorldTasksManager.schedule(new WorldTask() {
			@Override
			public void run() {
				if (player.getInventory().addItemDrop(harvest.product, 1)) {
					player.getPackets().sendMessage("You pick a " + productName + ".");
					player.getSkills().addXp(Skills.FARMING, harvest.exp);
				}
			}
		}, 2);*/
	}

	public static void plantHarvest(Item item, final Player player, GameObject object, DungeonManager dungeon) {
		/*final Harvest harvest = Harvest.forSeed(item.getScrollId());
		if (harvest == null)
			return;
		else if (player.getSkills().getLevel(Skills.FARMING) < harvest.lvl) {
			player.getPackets().sendGameMessage("You need a Farming level of " + harvest.lvl + " in order to plant a " + ItemDefinition.getItemDefinitions(harvest.seed).getName().toLowerCase() + ".");
			return;
		}

		object.setId(DungeonConstants.EMPTY_FARMING_PATCH + 1);
		WorldObject[] objects = new WorldObject[4];
		for (int i = 0; i < objects.length; i++)
			objects[i] = new WorldObject(object.getScrollId() + i + (harvest.ordinal() * 3), object.getInfo(), object.getRotation(), new WorldTile(object));
		dungeon.getFarmKeyList().add(PrivateObjectManager.spawnPrivateObject(player, objects, DungeonConstants.FARMING_CYCLES));

		player.getMovement().lock(2);
		player.setNextAnimation(new Animation(3659));
		player.getInventory().deleteItem(harvest.seed, 1);*/
	}

	public static void clearHarvest(final Player player, final GameObject object) {
		player.animate(3659);
		player.getMovement().lock(2);

		GameWorld.schedule(2, () -> {
			ObjectManager.spawnObject(new GameObject(DungeonConstants.EMPTY_FARMING_PATCH, object, object.getType(), object.getDirection()));
			player.message("You empty the harvest patch.", true);
		});
	}
}
