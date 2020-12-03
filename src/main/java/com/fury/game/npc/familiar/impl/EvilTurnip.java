package com.fury.game.npc.familiar.impl;

import com.fury.core.model.node.entity.actor.figure.Figure;
import com.fury.core.model.node.entity.actor.figure.Projectile;
import com.fury.core.model.node.entity.actor.figure.player.Player;
import com.fury.game.content.skill.member.summoning.impl.Summoning;
import com.fury.game.entity.character.combat.CombatIcon;
import com.fury.game.entity.character.combat.Hit;
import com.fury.game.node.entity.actor.figure.ProjectileManager;
import com.fury.game.npc.familiar.Familiar;
import com.fury.game.world.GameWorld;
import com.fury.game.world.map.Position;
import com.fury.game.world.update.flag.block.HitMask;
import com.fury.game.world.update.flag.block.graphic.Graphic;
import com.fury.util.Misc;

public class EvilTurnip extends Familiar {

	public EvilTurnip(Player owner, Summoning.Pouches pouch, Position tile) {
		super(owner, pouch, tile);
	}

	@Override
	public String getSpecialName() {
		return "Evil flames";
	}

	@Override
	public String getSpecialDescription() {
		return "Magic based attack which will drain the enemy's magic level some and heal the Evil turnip a little.";
	}

	@Override
	public int getStoreSize() {
		return 0;
	}

	@Override
	public int getSpecialAttackEnergy() {
		return 5;
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
		animate(8251);
		ProjectileManager.send(new Projectile(this, target, 1330, 34, 16, 30, 35, 16, 0));
		GameWorld.schedule(2, () -> {
			int hitDamage = Misc.random(100);
			target.getCombat().applyHit(new Hit(getOwner(), hitDamage, HitMask.RED, CombatIcon.MAGIC));
			target.perform(new Graphic(1329));
			getHealth().heal(hitDamage / 5);
		});
		return true;
	}
}