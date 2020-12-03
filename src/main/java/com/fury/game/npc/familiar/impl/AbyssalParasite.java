package com.fury.game.npc.familiar.impl;

import com.fury.core.model.node.entity.actor.figure.Figure;
import com.fury.core.model.node.entity.actor.figure.Projectile;
import com.fury.core.model.node.entity.actor.figure.player.Player;
import com.fury.game.content.skill.Skill;
import com.fury.game.content.skill.member.summoning.impl.Summoning;
import com.fury.game.entity.character.combat.CombatIcon;
import com.fury.game.entity.character.combat.Hit;
import com.fury.game.node.entity.actor.figure.ProjectileManager;
import com.fury.game.npc.familiar.Familiar;
import com.fury.game.world.GameWorld;
import com.fury.game.world.map.Position;
import com.fury.game.world.update.flag.block.HitMask;
import com.fury.util.Misc;

public class AbyssalParasite extends Familiar {

	public AbyssalParasite(Player owner, Summoning.Pouches pouch, Position tile) {
		super(owner, pouch, tile);
	}

	@Override
	public String getSpecialName() {
		return "Abyssal drain";
	}

	@Override
	public String getSpecialDescription() {
		return "Lowers an opponent's prayer with a magic attack.";
	}

	@Override
	public int getStoreSize() {
		return 7;
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
		final Figure target = (Figure) object;
		final int damage = Misc.random(100);
		animate(7675);
		graphic(1422);
		ProjectileManager.send(new Projectile(this, target, 1423, 34, 16, 30, 35, 16, 0));
		if (target.isPlayer())
			((Player) target).getSkills().drain(Skill.PRAYER, damage / 2);
		GameWorld.schedule(2, () -> target.getCombat().applyHit(new Hit(getOwner(), damage, HitMask.RED, CombatIcon.MAGIC)));
		return false;
	}
}
