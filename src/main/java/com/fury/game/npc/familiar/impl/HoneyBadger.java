package com.fury.game.npc.familiar.impl;

import com.fury.game.content.skill.Skill;
import com.fury.game.content.skill.member.summoning.impl.Summoning;
import com.fury.core.model.node.entity.actor.figure.player.Player;
import com.fury.game.npc.familiar.Familiar;
import com.fury.game.world.map.Position;
import com.fury.game.world.update.flag.block.graphic.Graphic;

public class HoneyBadger extends Familiar {

	public HoneyBadger(Player owner, Summoning.Pouches pouch, Position tile) {
		super(owner, pouch, tile);
	}

	@Override
	public String getSpecialName() {
		return "Insane ferocity";
	}

	@Override
	public String getSpecialDescription() {
		return "Decreases the owner's Magic, Range, and Defence, but also increasing Strength and Attack, there is also a chance of hitting twice.";
	}

	@Override
	public int getStoreSize() {
		return 0;
	}

	@Override
	public int getSpecialAttackEnergy() {
		return 4;
	}

	@Override
	public SpecialAttack getSpecialAttack() {
		return SpecialAttack.CLICK;
	}

	@Override
	public boolean submitSpecial(Object object) {
		Player player = getOwner();
		player.getSkills().boost(Skill.ATTACK, 0.15);
		player.getSkills().boost(Skill.STRENGTH, 0.15);
		player.getSkills().drain(Skill.DEFENCE, 0.15);
		player.getSkills().drain(Skill.RANGED, 0.10);
		player.getSkills().drain(Skill.MAGIC, 5);
		animate(7930);
		perform(new Graphic(1397));
		player.animate(7660);
		player.perform(new Graphic(1399));
		return false;
	}
}
