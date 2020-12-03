package com.fury.game.entity.character.player;

import com.fury.core.model.node.entity.actor.figure.player.Player;

public class PlayerProcess {

	/*
	 * The player (owner) of this instance
	 */
	private Player player;

	/*
	 * The loyalty tick, once this reaches 6, the player
	 * will be given loyalty points.
	 * 6 equals 3.6 seconds.
	 */
	private int loyaltyTick;

	/*
	 * The timer tick, once this reaches 2, the player's
	 * total play time will be updated.
	 * 2 equals 1.2 seconds.
	 */
	private int timerTick;

	/*
	 * Makes sure ground items are spawned on height change
	 */
	private int previousHeight;

	public PlayerProcess(Player player) {
		this.player = player;
		this.previousHeight = player.getZ();
	}

	public void sequence() {



	}
}
