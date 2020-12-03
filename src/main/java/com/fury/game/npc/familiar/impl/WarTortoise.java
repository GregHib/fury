package com.fury.game.npc.familiar.impl;

import com.fury.game.content.skill.Skill;
import com.fury.game.content.skill.member.summoning.impl.Summoning;
import com.fury.core.model.node.entity.actor.figure.player.Player;
import com.fury.game.npc.familiar.Familiar;
import com.fury.game.world.map.Position;
import com.fury.game.world.update.flag.block.graphic.Graphic;

public class WarTortoise extends Familiar {

	public WarTortoise(Player owner, Summoning.Pouches pouch, Position tile) {
		super(owner, pouch, tile);
	}

	@Override
	public String getSpecialName() {
		return "Testudo";
	}

	@Override
	public String getSpecialDescription() {
		return "Increases defence by nine points.";
	}

	@Override
	public int getStoreSize() {
		return 18;
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
		Player player = (Player) object;
		animate(8288);
		perform(new Graphic(1414));
		player.perform(new Graphic(1300));
		player.animate(7660);
		player.getSkills().boost(Skill.DEFENCE, 9);
		return true;
	}
}
