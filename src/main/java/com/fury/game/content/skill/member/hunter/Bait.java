package com.fury.game.content.skill.member.hunter;

/**
 * 
 * @author Faris
 */
public enum Bait {
	PAPAYA(5972),
	RAW_PAWYA_MEAT(12535),
	/**
	 * Standard bait type for box traps, will increase the catch rate of
	 * Chinchompa hunter NPCs
	 */
	SPICY_MINCED_MEAT(9996),

	/**
	 * Standard bait type for snare traps, will increase the catch rate of bird
	 * type hunter NPCs
	 */
	TOMATO(1982);

	private int baitId;

	Bait(int baitId) {
		this.baitId = baitId;
	}

	public int getBaitId() {
		return baitId;
	}
	public static Bait forId(int itemId) {
		for(Bait b : Bait.values()) {
			if(b.getBaitId() == itemId) {
				return b;
			}
		}
		return null;
	}
	public static boolean isBait(int itemId) {
		for(Bait b : Bait.values()) {
			if(b.getBaitId() == itemId) {
				return true;
			}
		}
		return false;
	}
}
