package com.fury.game.npc.familiar.impl;

import com.fury.core.model.node.entity.actor.figure.Figure;
import com.fury.core.model.node.entity.actor.figure.Projectile;
import com.fury.core.model.node.entity.actor.figure.player.Player;
import com.fury.core.task.TickableTask;
import com.fury.game.content.skill.member.summoning.impl.Summoning;
import com.fury.game.entity.character.combat.CombatIcon;
import com.fury.game.entity.character.combat.Hit;
import com.fury.game.node.entity.actor.figure.ProjectileManager;
import com.fury.game.npc.familiar.Familiar;
import com.fury.game.world.GameWorld;
import com.fury.game.world.map.Position;
import com.fury.game.world.update.flag.block.HitMask;
import com.fury.util.Misc;

public class TalonBeast extends Familiar {

	public TalonBeast(Player owner, Summoning.Pouches pouch, Position tile) {
		super(owner, pouch, tile);
	}

	@Override
	public String getSpecialName() {
		return "Deadly Claw";
	}

	@Override
	public String getSpecialDescription() {
		return "A magical attack that hits 3 times. It is similar to its normal attack, but may hit higher (80 per hit, adding up to a max of 240) and will also hit more often through metal armour.";
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
		animate(5989);
		graphic(1519);
		ProjectileManager.send(new Projectile(this, target, 1520, 34, 16, 30, 35, 16, 0));
		GameWorld.schedule(new TickableTask(true) {
			@Override
			public void tick() {
				if (getTick() == 2) {
					stop();
					return;
				}
				target.getCombat().applyHit(new Hit(getOwner(), Misc.random(80), HitMask.RED, CombatIcon.MAGIC));
			}
		});
		return false;
	}
}
