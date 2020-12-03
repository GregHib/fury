package com.fury.game.npc.familiar.impl;

import com.fury.game.content.skill.member.summoning.impl.Summoning;
import com.fury.core.model.node.entity.actor.figure.player.Player;
import com.fury.game.npc.familiar.Familiar;
import com.fury.game.world.map.Position;

public class UnicornStallion extends Familiar {

	public UnicornStallion(Player owner, Summoning.Pouches pouch, Position tile) {
		super(owner, pouch, tile);
		setForceAggressive(false);
	}

	@Override
	public String getSpecialName() {
		return "Healing Aura";
	}

	@Override
	public String getSpecialDescription() {
		return "Heals 15% of your health points.";
	}

	@Override
	public int getStoreSize() {
		return 0;
	}

	@Override
	public int getSpecialAttackEnergy() {
		return 20;
	}

	@Override
	public SpecialAttack getSpecialAttack() {
		return SpecialAttack.CLICK;
	}

	@Override
	public boolean submitSpecial(Object object) {
		Player player = (Player) object;
		if (player.getHealth().getHitpoints() == player.getMaxConstitution()) {
			player.message("You need to have at least some damage before being able to heal yourself.");
			return false;
		} else {
			player.animate(7660);
			player.graphic(1300);
			int percentHealed = (int) (player.getMaxConstitution() * .1);
			player.getHealth().heal(percentHealed);
		}
		return true;
	}

}
