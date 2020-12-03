package com.fury.game.npc.familiar.impl;

import com.fury.game.content.skill.member.summoning.impl.Summoning;
import com.fury.core.model.node.entity.actor.figure.player.Player;
import com.fury.game.npc.familiar.Familiar;
import com.fury.game.world.map.Position;

public class SpiritTzKih extends Familiar {

	public SpiritTzKih(Player owner, Summoning.Pouches pouch, Position tile) {
		super(owner, pouch, tile);
	}

	@Override
	public String getSpecialName() {
		return "Fireball Assault";
	}

	@Override
	public String getSpecialDescription() {
		return "Has the potential of hitting up to two nearby targets with up to 70 points of damage";
	}

	@Override
	public int getStoreSize() {
		return 0;
	}

	@Override
	public int getSpecialAttackEnergy() {
		return 6;
	}

	@Override
	public SpecialAttack getSpecialAttack() {
		return SpecialAttack.ENTITY;
	}

	@Override
	public boolean submitSpecial(Object object) {
		return false;
	}
}
