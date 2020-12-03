package com.fury.game.npc.familiar.impl;

import com.fury.game.content.skill.Skill;
import com.fury.game.content.skill.member.summoning.impl.Summoning;
import com.fury.core.model.node.entity.actor.figure.Figure;
import com.fury.game.entity.character.combat.CombatIcon;
import com.fury.game.entity.character.combat.Hit;
import com.fury.core.model.node.entity.actor.figure.player.Player;
import com.fury.game.npc.familiar.Familiar;
import com.fury.game.world.GameWorld;
import com.fury.game.world.map.Position;
import com.fury.game.world.update.flag.block.HitMask;
import com.fury.game.world.update.flag.block.graphic.Graphic;
import com.fury.util.Misc;

public class PrayingMantis extends Familiar {

	public PrayingMantis(Player owner, Summoning.Pouches pouch, Position tile) {
		super(owner, pouch, tile);
	}

	@Override
	public String getSpecialName() {
		return "Mantis Strike";
	}

	@Override
	public String getSpecialDescription() {
		return "Uses a magic based attack (max hit 170) which always drains the opponent's prayer and binds if it deals any damage.";
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
		final Figure target = (Figure) object;
		getOwner().graphic(1316);
		getOwner().animate(7660);
		animate(8071);
		perform(new Graphic(1422));
		final int hitDamage = Misc.random(170);
		if (hitDamage > 0) {
			if (target.isPlayer())
				((Player) target).getSkills().drain(Skill.PRAYER, hitDamage);
		}
		GameWorld.schedule(2, () -> {
			target.graphic(1423);
			target.getCombat().applyHit(new Hit(getOwner(), hitDamage, HitMask.RED, CombatIcon.MAGIC));
		});
		return true;
	}
}
