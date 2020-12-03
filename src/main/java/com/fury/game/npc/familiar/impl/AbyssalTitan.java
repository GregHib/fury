package com.fury.game.npc.familiar.impl;

import com.fury.game.content.skill.member.summoning.impl.Summoning;
import com.fury.core.model.node.entity.actor.figure.player.Player;
import com.fury.game.npc.familiar.Familiar;
import com.fury.game.world.map.Position;

public class AbyssalTitan extends Familiar {

	public AbyssalTitan(Player owner, Summoning.Pouches pouch, Position tile) {
		super(owner, pouch, tile);
	}

	@Override
	public String getSpecialName() {
		return "Essence Shipment";
	}

	@Override
	public String getSpecialDescription() {
		return "Sends all your inventory and beast's essence to your bank.";
	}

	@Override
	public int getStoreSize() {
		return 7;
	}

	@Override
	public int getSpecialAttackEnergy() {
		return 6;
	}

	@Override
	public SpecialAttack getSpecialAttack() {
		return SpecialAttack.CLICK;
	}

	@Override
	public boolean submitSpecial(Object object) {
		if (getOwner().getBank().isFull()) {
			if (!getBeastOfBurden().hasRoom()) {
				getOwner().message("Your familiar has no essence to deposit.");
				return false;
			}
			getOwner().getBank().deposit(getBeastOfBurden());
			getOwner().graphic(1316);
			getOwner().animate(7660);
			return true;
		}
		return false;
	}
}
