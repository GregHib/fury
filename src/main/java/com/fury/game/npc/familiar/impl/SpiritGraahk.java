package com.fury.game.npc.familiar.impl;

import com.fury.game.content.skill.member.summoning.impl.Summoning;
import com.fury.core.model.node.entity.actor.figure.Figure;
import com.fury.core.model.node.entity.actor.figure.player.Player;
import com.fury.game.npc.familiar.Familiar;
import com.fury.game.world.map.Position;

public class SpiritGraahk extends Familiar {

	public SpiritGraahk(Player owner, Summoning.Pouches pouch, Position tile) {
		super(owner, pouch, tile);
	}

	@Override
	public String getSpecialName() {
		return "Groad";
	}

	@Override
	public String getSpecialDescription() {
		return "Attack the selected opponent at the cost of 3 special attack points.";
	}

	@Override
	public int getStoreSize() {
		return 0;
	}

	@Override
	public int getSpecialAttackEnergy() {
		return 3;
	}

	@Override
	public SpecialAttack getSpecialAttack() {
		return SpecialAttack.ENTITY;
	}

	@Override
	public boolean submitSpecial(Object object) {
		Figure figure = (Figure) object;
		if (getCombat().getAttackedBy() != null) {
			getOwner().message("Your graahk already has a target in its sights!");
			return false;
		}
		getOwner().animate(7660);
		getOwner().graphic(1316);
		getMobCombat().setTarget(figure);
		return false;
	}
}
