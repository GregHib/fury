package com.fury.game.entity.character.player.content;

/**
 * Represents a player's privilege rights.
 * 
 * @author relex lawl
 */

public enum PlayerInteractingOption {

	NONE,
	CHALLENGE,
	INVITE,
	ATTACK,
	PELT;

	public static PlayerInteractingOption forName(String name) {
		if(name.toLowerCase().contains("null"))
			return NONE;
		for(PlayerInteractingOption option : PlayerInteractingOption.values())
			if(option.toString().equalsIgnoreCase(name))
				return option;
		return null;
	}
}
