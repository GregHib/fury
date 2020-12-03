package com.fury.game.npc.familiar.impl;

import com.fury.game.content.skill.Skill;
import com.fury.game.content.skill.member.summoning.impl.Summoning;
import com.fury.core.model.node.entity.actor.figure.player.Player;
import com.fury.game.npc.familiar.Familiar;
import com.fury.game.world.map.Position;

public class IceTitan extends Familiar {

	public IceTitan(Player owner, Summoning.Pouches pouch, Position tile) {
		super(owner, pouch, tile);
	}

	@Override
	public String getSpecialName() {
		return "Titan's Constitution ";
	}

	@Override
	public String getSpecialDescription() {
		return "Defence by 12.5%, and it can also increase a player's Life Points 80 points higher than their max Life Points.";
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
		return SpecialAttack.CLICK;
	}

	@Override
	public boolean submitSpecial(Object object) {
		getOwner().graphic(2011);
		getOwner().animate(7660);
		getOwner().getSkills().boost(Skill.DEFENCE, 12.5);
		return true;
	}
}
