package com.fury.game.world.update.flag.block;

import com.fury.game.world.update.flag.Flag;
import com.fury.core.model.node.entity.actor.figure.player.Player;

/**
 * This file manages a player's appearance and properties, such as head hints, gender, prayer head hints, etc.
 * 
 * @author relex lawl
 */

public class Appearance {
	
	/**
	 * Player's current gender.
	 */
	private Gender gender = Gender.MALE;
	
	/**
	 * Can the player change appearance right now?
	 */
	private boolean canChangeAppearance = false;
	
	/**
	 * The player's head icon hint.
	 */
	private int headHint = -1;
	
	/**
	 * The player's bounty hunter skull.
	 */
	private int bountyHunterSkull = -1;
	
	/**
	 * Gets the player's gender.
	 * @return	gender.
	 */
	public Gender getGender() {
		return gender;
	}
	
	/**
	 * Sets the player's gender.
	 * @param gender	Gender to set to.
	 */
	public Appearance setGender(Gender gender) {
		this.gender = gender;
		player.getUpdateFlags().add(Flag.APPEARANCE);
		return this;
	}
	
	/**
	 * Gets the player's current head hint index.
	 * @return	The player's head hint.
	 */
	public int getHeadHint() {
		return headHint;
	}

	/**
	 * Sets the player's head icon hint.
	 * @param headHint	The hint index to use.
	 * @return			The Appearance instance.
	 */
	public Appearance setHeadHint(int headHint) {
		this.headHint = headHint;
		player.getUpdateFlags().add(Flag.APPEARANCE);
		return this;
	}

	/**
	 * Gets the player's current bounty hunter skull.
	 * @return	The player's skull hint.
	 */
	public int getBountyHunterSkull() {
		return bountyHunterSkull;
	}
	
	/**
	 * Sets the player's bounty hunter skull.
	 * @param skullHint	The skull hint index to use.
	 * @return			The Appearance instance.
	 */
	public Appearance setBountyHunterSkull(int skullHint) {
		this.bountyHunterSkull = skullHint;
		player.getUpdateFlags().add(Flag.APPEARANCE);
		return this;
	}
	
	/**
	 * Checks if a player can change appearance right now
	 * @return the canChangeAppearance value
	 */
	public boolean canChangeAppearance() {
		return canChangeAppearance;
	}
	
	/**
	 * Sets if a player can change appearance right now
	 * @param l	The value to set
	 */
	public void setCanChangeAppearance(boolean l) {
		this.canChangeAppearance = l;
	}

	/**
	 * Gets the look array, which is an array with 13 elements describing the
	 * look of a player.
	 * @return The look array.
	 */
	public int[] getLook() {
		return look;
	}
	
	/**
	 * Sets the look array.
	 * @param look The look array.
	 * @throws IllegalArgumentException if the array length is not 12.
	 */
	public void set(int[] look) {
		if(look.length < 12) {
			throw new IllegalArgumentException("Array length must be 12.");
		}
		this.look = look;
		player.getUpdateFlags().add(Flag.APPEARANCE);
	}
	
	/**
	 * Sets a specific look.
	 * @param index		Array index to set.
	 * @param look		Value to change look[index] to.
	 */
	public void set(int index, int look) {
		this.look[index] = look;
		player.getUpdateFlags().add(Flag.APPEARANCE);
	}
	
	/**
	 * The player's current figure clothing.
	 */
	private int[] look = new int[13];
	
	/**
	 * The Appearance constructor, also sets
	 * the player's default clothing.
	 * @param player	The associated player.
	 */
	public Appearance(Player player) {
		this.player = player;
		set();
	}
	
	/**
	 * Sets the player's default clothing.
	 */
	public void set() {
		if (gender == Gender.MALE) {
			look[HEAD] = 0;
			look[BEARD] = 10;
			look[CHEST] = 18;
			look[ARMS] = 26;
			look[HANDS] = 33;
			look[LEGS] = 36;
			look[FEET] = 42;
		} else {
			look[HEAD] = 45;
			look[BEARD] = -1;
			look[CHEST] = 56;
			look[ARMS] = 61;
			look[HANDS] = 67;
			look[LEGS] = 70;
			look[FEET] = 79;
		}
		look[HAIR_COLOUR] = 0;
		look[TORSO_COLOUR] = 0;
		look[LEG_COLOUR] = 0;
		look[FEET_COLOUR] = 0;
		look[SKIN_COLOUR] = 0;
		player.getUpdateFlags().add(Flag.APPEARANCE);
	}
	
	/**
	 * The associated player.
	 */
	private Player player;

	/**
	 * The index of said body part color in the look array.
	 */
	public static final int HAIR_COLOUR = 8, TORSO_COLOUR = 9, LEG_COLOUR = 10, FEET_COLOUR = 11, SKIN_COLOUR = 12,
							HEAD = 1, CHEST = 2, ARMS = 3, HANDS = 4, LEGS = 5, FEET = 6, BEARD = 7, GENDER = 0;

	public void openInterface() {
		if (player.getEquipment().getSpaces() != player.getEquipment().capacity()) {
			player.message("You must have all items unequipped before doing this.");
			return;
		}
		player.getPacketSender().sendInterface(30700);
		player.getPacketSender().sendString(30553, player.getUsername());
		setCanChangeAppearance(true);
	}
}
