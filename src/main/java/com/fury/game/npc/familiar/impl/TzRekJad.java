package com.fury.game.npc.familiar.impl;

import com.fury.core.model.node.entity.actor.figure.player.Player;
import com.fury.game.npc.familiar.Familiar;
import com.fury.game.world.map.Position;

//TODO surely this is a pet not a familiar?
public final class TzRekJad extends Familiar {

	public TzRekJad(Player owner, Position tile) {
		super(owner, null, tile);
	}

	@Override
	public String getSpecialName() {
		return "null";
	}

	@Override
	public String getSpecialDescription() {
		return "null";
	}

	@Override
	public int getStoreSize() {
		return 0;
	}

	@Override
	public int getSpecialAttackEnergy() {
		return 0;
	}

	@Override
	public SpecialAttack getSpecialAttack() {
		return null;
	}

	@Override
	public boolean submitSpecial(Object object) {
		return false;
	}

}