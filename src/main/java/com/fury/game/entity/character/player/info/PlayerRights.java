package com.fury.game.entity.character.player.info;

import com.fury.Config;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;


/**
 * Represents a player's privilege rights.
 * @author Gabriel Hannason
 */

public enum PlayerRights {

	PLAYER(-1, 0),
	MODERATOR(2, 0xe0e0e0),
	ADMINISTRATOR(2, 0xffff64),
	OWNER(-1, 0xb40404),
	DEVELOPER(-1, 0xb40404),
	SUPPORT(3, 0xff0000),
	COMMUNITY_MANAGER(3, 0x00e700),
	VETERAN(6, 0xcd661d),
	LEGEND(3, 0x02a7c4),
	DESIGNER(3, 0x8ed4e1),
	YOUTUBER(6, 0xce1312);

	PlayerRights(int yellDelaySeconds, int yellColour) {
		this.yellDelay = yellDelaySeconds;
		this.yellColour = yellColour;
	}
	
	private static final ImmutableSet<PlayerRights> STAFF = Sets.immutableEnumSet(SUPPORT, COMMUNITY_MANAGER, DESIGNER, MODERATOR, ADMINISTRATOR, OWNER, DEVELOPER);

	private static final ImmutableSet<PlayerRights> UPPER_STAFF = Sets.immutableEnumSet(MODERATOR, ADMINISTRATOR, OWNER, DEVELOPER);

	private static final ImmutableSet<PlayerRights> PRIVILEDGED = Sets.immutableEnumSet(ADMINISTRATOR, OWNER, DEVELOPER);

	private int yellDelay;
	private int yellColour;
	
	public int getYellDelay() {
		return yellDelay;
	}

	public int getYellColour() {
		return yellColour;
	}
	
	public boolean isStaff() {
		return STAFF.contains(this);
	}

	public boolean isUpperStaff() { return UPPER_STAFF.contains(this); }

	public boolean isPriviledged() { return PRIVILEDGED.contains(this); }

	/**
	 * Gets the rank for a certain id.
	 * 
	 * @param id	The id (ordinal()) of the rank.
	 * @return		rights.
	 */
	public static PlayerRights forId(int id) {
		for (PlayerRights rights : PlayerRights.values()) {
			if (rights.ordinal() == id) {
				return rights;
			}
		}
		return null;
	}

	private int getId() {
		switch (this) {
			case PLAYER:
				return 0;
			case VETERAN:
				return 1;
			case LEGEND:
				return 2;
			case YOUTUBER:
				return 3;
			case DESIGNER:
				return 4;
			case SUPPORT:
				return 5;
			case COMMUNITY_MANAGER:
				return 6;
			case MODERATOR:
				return 7;
			case ADMINISTRATOR:
				return 8;
			case OWNER:
				return 9;
			case DEVELOPER:
				return 10;
		}
		return 0;
	}

	public boolean isOrHigher(PlayerRights rights) {
		return Config.TEST || getId() >= rights.getId();
	}
}
