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
import com.fury.util.Misc;

public class GiantEnt extends Familiar {

	public GiantEnt(Player owner, Summoning.Pouches pouch, Position tile) {
		super(owner, pouch, tile);
	}

	@Override
	public String getSpecialName() {
		return "Acorn Missile";
	}

	@Override
	public String getSpecialDescription() {
		return "Hits all players around a tile radius (not including you) with damage that could inflict up to 187 points.";
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
		final Familiar npc = this;
		Player player = getOwner();
		player.animate(7858);
		player.graphic(1316);
		GameWorld.schedule(1, () -> ProjectileManager.send(new Projectile(npc, target, 1362, 34, 16, 30, 35, 16, 0)));
		GameWorld.schedule(3, () -> {
			target.getCombat().applyHit(new Hit(getOwner(), Misc.getRandom(150), HitMask.RED, CombatIcon.MAGIC));
			target.graphic(1363);
		});
		return true;
	}
}
