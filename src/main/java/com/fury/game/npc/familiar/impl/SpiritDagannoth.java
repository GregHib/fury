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

public class SpiritDagannoth extends Familiar {

	public SpiritDagannoth(Player owner, Summoning.Pouches pouch, Position tile) {
		super(owner, pouch, tile);
	}

	@Override
	public String getSpecialName() {
		return "Spike Shot";
	}

	@Override
	public String getSpecialDescription() {
		return "Inflicts damage to your target from up to 180 hitpoints.";
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
		getOwner().graphic(1316);
		getOwner().animate(7660);
		animate(7787);
		graphic(1467);
		GameWorld.schedule(1, () -> ProjectileManager.send(new Projectile(npc, target, 1426, 34, 16, 30, 35, 16, 0)));
		GameWorld.schedule(3, () -> {
			int hitDamage = Misc.random(180);
			if (hitDamage > 0)
				target.getCombat().addFreezeDelay(10000, false);
			target.getCombat().applyHit(new Hit(getOwner(), hitDamage, HitMask.RED, CombatIcon.MAGIC));
			target.graphic(1428);
		});
		return true;
	}
}
