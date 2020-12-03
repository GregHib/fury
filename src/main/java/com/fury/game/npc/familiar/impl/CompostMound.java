package com.fury.game.npc.familiar.impl;

import com.fury.game.content.skill.Skill;
import com.fury.game.content.skill.member.summoning.impl.Summoning;
import com.fury.core.model.node.entity.actor.figure.player.Player;
import com.fury.game.npc.familiar.Familiar;
import com.fury.game.world.map.Position;

public class CompostMound extends Familiar {

	public CompostMound(Player owner, Summoning.Pouches pouch, Position tile) {
		super(owner, pouch, tile);
	}

	@Override
	public String getSpecialName() {
		return "Generate Compost";
	}

	@Override
	public String getSpecialDescription() {
		return "Fill a nearby compost bin with compost, with a chance of creating super compost.";
	}

	@Override
	public int getStoreSize() {
		return 0;
	}

	@Override
	public int getSpecialAttackEnergy() {
		return 12;
	}

	@Override
	public SpecialAttack getSpecialAttack() {
		return SpecialAttack.CLICK;
	}

	@Override
	public boolean submitSpecial(Object object) {
		Player player = (Player) object;
		player.graphic(1300);
		player.animate(7660);
		graphic(1312);
		player.getSkills().boost(Skill.FARMING, 1, 0.02);
		return true;
	}

}
