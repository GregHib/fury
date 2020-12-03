package com.fury.game.content.global.treasuretrails;

import com.fury.game.world.map.Position;

/**
 * Enumerator holding all possible Map treasuretrails
 * @author Stan
 *
 */
public enum TreasureLocations {

	CRAFTING_GUILD(4305, new Position(2906, 3296)),
	VARROCK_EAST_MINE(7045, new Position(3250, 3423)),
	SOUTH_DRAYNOR_BANK(7113, new Position(3093, 3244));
	
	private int interfaceID;
	private Position digPosition;
	
	/**
	 * Constructor for treasure trail map ids and their locations
	 * @param interfaceID
	 * @param digPosition
	 */
	TreasureLocations(int interfaceID, Position digPosition){
		this.setInterfaceID(interfaceID);
		this.setDigPosition(digPosition);
	}

	/**
	 * @return the digPosition
	 */
	public Position getDigPosition() {
		return digPosition;
	}

	/**
	 * @param digPosition the digPosition to set
	 */
	public void setDigPosition(Position digPosition) {
		this.digPosition = digPosition;
	}

	/**
	 * @return the interfaceID
	 */
	public int getInterfaceID() {
		return interfaceID;
	}

	/**
	 * @param interfaceID the interfaceID to set
	 */
	public void setInterfaceID(int interfaceID) {
		this.interfaceID = interfaceID;
	}
}
