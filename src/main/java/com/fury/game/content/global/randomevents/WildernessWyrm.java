package com.fury.game.content.global.randomevents;

import com.fury.Stopwatch;
import com.fury.core.model.node.entity.actor.figure.player.Player;
import com.fury.core.model.node.entity.actor.figure.mob.Mob;
import com.fury.game.world.GameWorld;
import com.fury.game.world.map.Position;
import com.fury.util.Colours;
import com.fury.util.FontUtils;
import com.fury.util.Misc;

public class WildernessWyrm {

	private static final int TIME = 2700000;
	
	public static Stopwatch timer = new Stopwatch().reset();
	public static WyrmSpawned SPAWNED_WYRM = null;
	private static LocationData LAST_LOCATION = null;
	private static boolean IS_DEAD = true;
	
	public static boolean isSpawned() {
		return SPAWNED_WYRM != null;
	}
	public static class WyrmSpawned {

		public WyrmSpawned(Mob wyrmMob, LocationData starLocation) {
			this.wyrmMob = wyrmMob;
			this.wyrmLocation = starLocation;
		}

		private Mob wyrmMob;
		private LocationData wyrmLocation;

		public Mob getStarObject() {
			return wyrmMob;
		}

		public LocationData getWyrmLocation() {
			return wyrmLocation;
		}
	}

	public enum LocationData {
		LOCATION_1(new Position(3348, 3749), "north-east of Clan Wars", "Clan Wars"),
		LOCATION_2(new Position(3052, 3650), "west of Bounty Hunter", "Bounty Hunter"),
		LOCATION_3(new Position(2970, 3841), "south of Frost Dragons", "Frost Dragons"),
		LOCATION_4(new Position(3200, 3876), "north of the Lava Maze", "Lava Maze"),
		LOCATION_5(new Position(3029, 3832), "north of The Forgotten Cemetery", "Forgot. Cemetery"),
		LOCATION_6(new Position(3055, 3665), "south-east of Bandit Camp", "Bandit Camp"),
		LOCATION_7(new Position(3203, 3680), "west of Graveyard of Shadows", "Graveyard"),
		LOCATION_8(new Position(3269, 3649), "south of Clan Wars", "Clan Wars");

		LocationData(Position spawnPos, String clue, String playerPanelFrame) {
			this.spawnPos = spawnPos;
			this.clue = clue;
			this.playerPanelFrame = playerPanelFrame;
		}

		public Position spawnPos;
		private String clue;
		public String playerPanelFrame;
	}

	public static LocationData getRandom() {
		LocationData wyrm = LocationData.values()[Misc.getRandom(LocationData.values().length - 1)];
		return wyrm;
	}

	public static void sequence() {
		if(SPAWNED_WYRM == null) {
			if(timer.elapsed(TIME)) {
				LocationData locationData = getRandom();
				if(LAST_LOCATION != null) {
					if(locationData == LAST_LOCATION) {
						locationData = getRandom();
					}
				}
				LAST_LOCATION = locationData;
				SPAWNED_WYRM = new WyrmSpawned(new Mob(3334, locationData.spawnPos), locationData);
				SPAWNED_WYRM.wyrmMob.getMovement().lock();
				//CustomObjects.spawnGlobalObject(SPAWNED_WYRM.starObject);
				IS_DEAD = false;
				GameWorld.sendBroadcast(FontUtils.imageTags(535) + " " + FontUtils.RED + "The Wildywyrm has just spawned in the wilderness!" + FontUtils.COL_END);//"+locationData.clue+"!");
				GameWorld.getPlayers().forEach(p -> p.getPacketSender().sendString(39163, "WildyWyrm: " + FontUtils.YELLOW + SPAWNED_WYRM.getWyrmLocation().playerPanelFrame + FontUtils.COL_END, Colours.ORANGE_2));
				timer.reset();
			}
		} else {
			if(IS_DEAD) {
				despawn(false);
				timer.reset();
			}
		}
	}

	public static void despawn(boolean respawn) {
		if(respawn) {
			timer.reset();
		} else {
			timer.reset();
		}
		if(SPAWNED_WYRM != null) {
			for(Player p : GameWorld.getPlayers()) {
				if(p == null) {
					continue;
				}
				p.getPacketSender().sendString(39163, "WildyWyrm: [ " + FontUtils.YELLOW + "N/A" + FontUtils.COL_END + " ]", Colours.ORANGE_2);
			}
			GameWorld.getMobs().remove(SPAWNED_WYRM.wyrmMob);
			SPAWNED_WYRM = null;
		}
	}
	
	public static void death() {
		IS_DEAD = true;
	}
}
