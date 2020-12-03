package com.fury.game.entity.character.npc.impl.dungeoneering;

import com.fury.core.model.node.entity.actor.figure.mob.Mob;
import com.fury.game.world.map.Position;

@SuppressWarnings("serial")
public class MastyxTrap extends Mob {

	private static final int BASE_TRAP = 11076;

	private String playerName;
	private int ticks;

	public MastyxTrap(String playerName, int id, Position tile) {
		super(id, tile);
		this.playerName = playerName;
	}

	@Override
	public void processNpc() {
		//Doesn't move or do anything so we don't process it.
		ticks++;
		if (ticks == 500) {
			deregister();
			return;
		}
	}

	public String getPlayerName() {
		return playerName;
	}

	public int getTier() {
		return getId() - BASE_TRAP;
	}
}
