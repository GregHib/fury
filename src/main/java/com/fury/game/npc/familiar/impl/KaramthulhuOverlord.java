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

public class KaramthulhuOverlord extends Familiar {

	public KaramthulhuOverlord(Player owner, Summoning.Pouches pouch, Position tile) {
		super(owner, pouch, tile);
	}

	@Override
	public String getSpecialName() {
		return "Doomsphere Device";
	}

	@Override
	public String getSpecialDescription() {
		return "Attacks the target with a powerful water spell that can cause up to 160 life points";
	}

	@Override
	public int getStoreSize() {
		return 0;
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
		Player player = getOwner();
		graphic(7974);
		graphic(1478);
		player.animate(7660);
		player.graphic(1316);
		ProjectileManager.send(new Projectile(this, target, 1479, 34, 16, 30, 35, 16, 0));
		GameWorld.schedule(2, () -> {
			target.getCombat().applyHit(new Hit(getOwner(), Misc.getRandom(163), HitMask.RED, CombatIcon.MAGIC));
			target.graphic(1480);
		});
		return true;
	}
}
