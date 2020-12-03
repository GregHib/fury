package com.fury.game.npc.familiar.impl;


import com.fury.game.content.skill.member.summoning.impl.Summoning;
import com.fury.core.model.node.entity.actor.figure.Figure;
import com.fury.game.entity.character.combat.CombatIcon;
import com.fury.game.entity.character.combat.Hit;
import com.fury.core.model.node.entity.actor.figure.player.Player;
import com.fury.game.npc.familiar.Familiar;
import com.fury.game.world.GameWorld;
import com.fury.game.world.map.Position;
import com.fury.game.world.update.flag.block.HitMask;
import com.fury.util.Misc;

public class SpiritKyatt extends Familiar {

	public SpiritKyatt(Player owner, Summoning.Pouches pouch, Position tile) {
		super(owner, pouch, tile);
	}

	@Override
	public String getSpecialName() {
		return "Ambush";
	}

	@Override
	public String getSpecialDescription() {
		return "Hit three times greater than the highest possible max hit.";
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
		Player player = getOwner();
		moveTo(player);
		player.graphic(1316);
		player.animate(7660);
		animate(5229);
		graphic(1365);
		final Figure target = (Figure) object;
		GameWorld.schedule(1, () -> target.getCombat().applyHit(new Hit(getOwner(), Misc.random(321), HitMask.RED, CombatIcon.MAGIC)));
		return true;
	}
}
