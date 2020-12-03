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
import com.fury.game.world.update.flag.block.graphic.Graphic;
import com.fury.util.Misc;

public class BarkerToad extends Familiar {

	public BarkerToad(Player owner, Summoning.Pouches pouch, Position tile) {
		super(owner, pouch, tile);
	}

	@Override
	public String getSpecialName() {
		return "Toad Bark";
	}

	@Override
	public String getSpecialDescription() {
		return "A magic-based attack that can inflict up to 180 damage on an opponent is unleashed";
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
		animate(7260);
		perform(new Graphic(1403));
		GameWorld.schedule(2, () -> {
			target.getCombat().applyHit(new Hit(getOwner(), Misc.random(180), HitMask.RED, CombatIcon.MAGIC));
			target.graphic(1404);
		});
		return true;
	}
}
