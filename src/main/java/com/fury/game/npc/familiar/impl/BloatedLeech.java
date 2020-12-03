package com.fury.game.npc.familiar.impl;

import com.fury.game.content.skill.member.summoning.impl.Summoning;
import com.fury.game.entity.character.combat.CombatIcon;
import com.fury.game.entity.character.combat.Hit;
import com.fury.game.entity.character.combat.effects.Effects;
import com.fury.core.model.node.entity.actor.figure.player.Player;
import com.fury.game.npc.familiar.Familiar;
import com.fury.game.world.map.Position;
import com.fury.game.world.update.flag.block.HitMask;
import com.fury.util.Misc;

public class BloatedLeech extends Familiar {

	public BloatedLeech(Player owner, Summoning.Pouches pouch, Position tile) {
		super(owner, pouch, tile);
	}

	@Override
	public String getSpecialName() {
		return "Blood Drain";
	}

	@Override
	public String getSpecialDescription() {
		return "Heals stat damage, poison, and disease but sacrifices some life points.";
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
		return SpecialAttack.CLICK;
	}

	@Override
	public boolean submitSpecial(Object object) {
		Player player = (Player) object;
		final int damage = Misc.random(100) + 50;
		if (player.getHealth().getHitpoints() - damage <= 0) {
			player.message("You don't have enough life points to use this special.");
			return false;
		}
		if(player.getEffects().hasActiveEffect(Effects.POISON))
			player.getEffects().removeEffect(Effects.POISON);
		player.getSkills().reset();
		player.getCombat().applyHit(new Hit(player, damage, HitMask.YELLOW, CombatIcon.NONE));
		graphic(1419);
		player.graphic(1420);
		return true;
	}
}