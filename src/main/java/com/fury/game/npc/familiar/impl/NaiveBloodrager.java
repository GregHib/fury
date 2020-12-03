package com.fury.game.npc.familiar.impl;

import com.fury.game.content.skill.member.summoning.impl.Summoning;
import com.fury.core.model.node.entity.actor.figure.player.Player;
import com.fury.game.npc.familiar.Familiar;
import com.fury.game.world.map.Position;

public class NaiveBloodrager extends Familiar {

	public NaiveBloodrager(Player owner, Summoning.Pouches pouch, Position tile) {
		super(owner, pouch, tile);
	}

	@Override
	public String getSpecialName() {
		return "Sundering Strike";
	}

	@Override
	public String getSpecialDescription() {
		return "Deals a more damaging attack that is 15% more accurate, and reduces opponent's Defence.";
	}

	@Override
	public int getStoreSize() {
		return 0;
	}

	@Override
	public int getSpecialAttackEnergy() {
		return 20;
	}

	@Override
	public SpecialAttack getSpecialAttack() {
		return SpecialAttack.ENTITY;
	}

	@Override
	public boolean submitSpecial(Object object) {//COMBAT SPECIAL
		graphic(2445);
		getOwner().graphic(1316);
		getOwner().animate(7660);
		return true;
	}
}
