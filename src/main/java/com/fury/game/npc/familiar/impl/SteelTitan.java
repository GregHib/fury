package com.fury.game.npc.familiar.impl;

import com.fury.game.content.skill.member.summoning.impl.Summoning;
import com.fury.core.model.node.entity.actor.figure.player.Player;
import com.fury.game.npc.familiar.Familiar;
import com.fury.game.world.map.Position;

public class SteelTitan extends Familiar {

	public SteelTitan(Player owner, Summoning.Pouches pouch, Position tile) {
		super(owner, pouch, tile);
		animate(8188);
	}

	@Override
	public int getSpecialAttackEnergy() {
		return 12;
	}

	@Override
	public String getSpecialName() {
		return "Steel of Legends";
	}

	@Override
	public String getSpecialDescription() {
		return "Defence boost only applies to melee attacks. Scroll initiates attack on opponent, hitting four times, with either ranged or melee, depending on the distance to the target";
	}

	@Override
	public SpecialAttack getSpecialAttack() {
		return SpecialAttack.ENTITY;
	}

	@Override
	public int getStoreSize() {
		return 0;
	}

	@Override
	public boolean submitSpecial(Object object) {
		return true;
	}
}
