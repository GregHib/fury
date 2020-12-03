package com.fury.game.entity.character.npc.impl.queenblackdragon;


import com.fury.core.model.node.entity.actor.figure.player.Player;

/**
 * Represents an attack from the Queen Black Dragon.
 * 
 * @author Emperor
 * 
 */
public interface QueenAttack {

	/**
	 * Starts the attack.
	 * 
	 * @param npc
	 *            The Npc.
	 * @param victim
	 *            The victim.
	 * @return The next attack value.
	 */
	int attack(QueenBlackDragon npc, Player victim);

	/**
	 * Checks if the QBD can use this attack.
	 * 
	 * @param npc
	 *            The QBD.
	 * @param victim
	 *            The player.
	 * @return {@code True} if so.
	 */
	boolean canAttack(QueenBlackDragon npc, Player victim);

}