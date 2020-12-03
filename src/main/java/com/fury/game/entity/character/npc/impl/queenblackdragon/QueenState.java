package com.fury.game.entity.character.npc.impl.queenblackdragon;

import com.fury.util.FontUtils;

/**
 * Represents the Queen Black Dragon's states.
 * 
 * @author Emperor
 * 
 */
public enum QueenState {

	/**
	 * The queen is asleep.
	 */
	SLEEPING(15509),

	/**
	 * The default, attackable Qeeun Black Dragon.
	 */
	DEFAULT(15454),

	/**
	 * The crystal armour state (weak to physical attacks, strong against
	 * magic).
	 */
	CRYSTAL_ARMOUR(15506, FontUtils.add("The Queen Black Dragon takes the consistency of crystal; she is more resistant to", 0x66ffff), FontUtils.add("magic, but weaker to physical damage.", 0x66ffff)),

	/**
	 * The hardened state (weak to magic attacks, strong against melee/range).
	 */
	HARDEN(15507, FontUtils.add("The Queen Black Dragon hardens her carapace; she is more resistant to", 0x669900), FontUtils.add("physical damage, but more vulerable to magic.", 0x669900));

	/**
	 * The Npc id.
	 */
	private final int npcId;

	/**
	 * The message to be send to the player.
	 */
	private final String[] messages;

	/**
	 * Constructs a new {@code QueenState} {@code Object}.
	 * 
	 * @param npcId
	 *            The Npc id.
	 * @param messages
	 *            The message to send.
	 */
	private QueenState(int npcId, String...messages) {
		this.npcId = npcId;
		this.messages = messages;
	}

	/**
	 * Gets the npcId.
	 * 
	 * @return The npcId.
	 */
	public int getNpcId() {
		return npcId;
	}

	/**
	 * Gets the message.
	 * 
	 * @return The message.
	 */
	public String[] getMessages() {
		return messages;
	}
}