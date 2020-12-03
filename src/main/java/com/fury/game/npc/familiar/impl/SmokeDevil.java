package com.fury.game.npc.familiar.impl;


import com.fury.game.content.skill.member.summoning.impl.Summoning;
import com.fury.core.model.node.entity.actor.figure.Figure;
import com.fury.game.entity.character.combat.CombatIcon;
import com.fury.game.entity.character.combat.Hit;
import com.fury.core.model.node.entity.actor.figure.player.Player;
import com.fury.game.npc.familiar.Familiar;
import com.fury.game.world.map.Position;
import com.fury.game.world.update.flag.block.HitMask;
import com.fury.game.world.update.flag.block.graphic.Graphic;
import com.fury.util.Misc;

public class SmokeDevil extends Familiar {

	public SmokeDevil(Player owner, Summoning.Pouches pouch, Position tile) {
		super(owner, pouch, tile);
	}

	@Override
	public String getSpecialName() {
		return "Dust Cloud";
	}

	@Override
	public String getSpecialDescription() {
		return "Hit up to 80 damage to all people within 1 square of you.";
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
		getOwner().graphic(1316);
		getOwner().animate(7660);
		animate(7820);
		perform(new Graphic(1470));
		for (Figure figure : this.getPossibleTargets()) {
			if (figure == null || figure == getOwner() || !figure.isWithinDistance(this, 1))
				continue;
			figure.getCombat().applyHit(new Hit(this, Misc.random(80), HitMask.RED, CombatIcon.MAGIC));
		}
		return true;
	}
}
