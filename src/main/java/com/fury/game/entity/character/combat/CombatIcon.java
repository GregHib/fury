package com.fury.game.entity.character.combat;

/**
 * Represents a damage's combat icon.
 * 
 * @author relex lawl
 */

public enum CombatIcon {
	BLOCK,
	MELEE,
	RANGED,
	MAGIC,
	DEFLECT,
	CANNON,
	BLUE_SHIELD,
	NONE;

	/**
	 * Gets the id that will be sent to client for said CombatIcon.
	 * @return	The index that will be sent to client.
	 */
	public int getId() {
		return ordinal() - 1;
	}
	
	/**
	 * Gets the CombatIcon object for said id, being compared
	 * to it's ordinal (so ORDER IS CRUCIAL).
	 * @param id	The ordinal index of the combat icon.
	 * @return		The CombatIcon who's ordinal equals id.
	 */
	public static CombatIcon forId(int id) {
		for (CombatIcon icon : CombatIcon.values()) {
			if (icon.getId() == id)
				return icon;
		}
		return CombatIcon.BLOCK;
	}
}