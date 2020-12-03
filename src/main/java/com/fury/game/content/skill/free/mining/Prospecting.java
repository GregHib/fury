package com.fury.game.content.skill.free.mining;

import com.fury.cache.def.Loader;
import com.fury.game.content.global.dnd.star.ShootingStar;
import com.fury.core.model.node.entity.actor.figure.player.Player;

public class Prospecting {
	public enum Ores {
		RUNE_ESSENCE(new int[]{2491}, 1, 5, 1436, 2, -1),
		PURE_ESSENCE(new int[]{16684}, 17, 5, 7936, 3, -1),

		CLAY(new int[]{9711, 9712, 9713, 15503, 15504, 15505, 5766, 5767}, 1, 5, 434, 3, 2),
		COPPER(new int[]{3042, 9708, 9709, 9710, 11936, 11960, 11961, 11962, 11189, 11190, 11191, 29231, 29230, 2090, 5781, 5780, 5779}, 1, 17.5, 436, 4, 4),
		TIN(new int[]{3043, 9714, 9715, 9716, 11933, 11957, 11958, 11959, 11186, 11187, 11188, 29227, 29229, 2094, 5776, 5777, 5778}, 1, 17.5, 438, 4, 4),
		IRON(new int[]{9717, 9718, 9719, 2093, 2092, 11954, 11955, 11956, 29221, 29222, 29223, 14856, 14857, 14858, 5773, 5774, 5775}, 15, 35, 440, 5, 5),
		SILVER(new int[]{2100, 2101, 29226, 29225, 11948, 11949, 11950}, 20, 40, 442, 5, 7),
		COAL(new int[]{2097, 5770, 29216, 29215, 29217, 11965, 11964, 11963, 11930, 11931, 11932, 14850, 14851, 14852, 5770, 5771, 5772}, 30, 50, 453, 5, 7),
		GOLD(new int[]{9720, 9721, 9722, 11951, 11183, 11184, 11185, 2099, 5768, 5769}, 40, 65, 444, 5, 10),
		MITHRIL(new int[]{25370, 25368, 11942, 11943, 11944, 11945, 11946, 29236, 11947, 14853, 14854, 14855, 5784, 5785, 5786}, 50, 80, 447, 6, 11),
		ADAMANITE(new int[]{11941, 11939, 29233, 29235, 14862, 14863, 14864, 5782, 5783}, 70, 95, 449, 7, 14),
		RUNITE(new int[]{14859, 14860, 14861, 2106, 2107}, 85, 125, 451, 7, 45),

		CRASHED_STAR(new int[]{38660, 38661, 38662, 38663, 38664, 38665, 38666, 38667, 38668}, 100, 1, 13727, 7, -1);


		private int objIds[];
		private int itemId, req, ticks, respawnTimer;
		private double xp;

		Ores(int[] objIds, int req, double xp, int itemId, int ticks, int respawnTimer) {
			this.objIds = objIds;
			this.req = req;
			this.xp = xp;
			this.itemId = itemId;
			this.ticks = ticks;
			this.respawnTimer = respawnTimer;
		}

		public int[] getObjIds() {
			return objIds;
		}

		public int getReq() {
			return req;
		}

		public int getRespawnTimer() {
			return respawnTimer;
		}

		public double getXp() {
			return xp;
		}

		public int getRespawn() {
			return respawnTimer;
		}

		public int getLevelReq(){
			return req;
		}

		public double getXpAmount(){
			return xp;
		}

		public void setXpAmount(double amount){
			this.xp = amount;
		}

		public int getItemId(){
			return itemId;
		}

		public int getTicks() {
			return ticks;
		}
	}

	public static Ores forRock(int id) {
		for (Ores ore : Ores.values()) {
			for (int obj : ore.getObjIds()) {
				if (obj == id) {
					return ore;
				}
			}
		}
		return null;
	}

	public static boolean prospectOre(final Player player, int id) {
		final Ores oreData = forRock(id);
		if(oreData != null) {
			if(oreData == Ores.CRASHED_STAR) {
				ShootingStar.prospect(player);
				return true;
			} else {
				if(!player.getTimers().getClickDelay().elapsed(4000))
					return true;
				player.stopAll();
				MiningBase.prospect(player, "This rock contains " + Loader.getItem(oreData.getItemId()).getName().toLowerCase().replaceAll("_", " ") + ".");
				player.getTimers().getClickDelay().reset();
			}
		}
		return false;
	}
}
