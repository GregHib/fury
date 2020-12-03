package com.fury.game.npc.familiar.impl;

import com.fury.game.content.skill.member.summoning.impl.Summoning;
import com.fury.core.model.node.entity.actor.figure.player.Player;
import com.fury.core.model.item.Item;
import com.fury.game.npc.familiar.Familiar;
import com.fury.game.world.map.Position;

public class PackYak extends Familiar {

	public PackYak(Player owner, Summoning.Pouches pouch, Position tile) {
		super(owner, pouch, tile);
		setForceAggressive(false);
	}

	@Override
	public int getSpecialAttackEnergy() {
		return 12;
	}

	@Override
	public String getSpecialName() {
		return "Winter Storage";
	}

	@Override
	public String getSpecialDescription() {
		return "Use special move on an item in your inventory to send it to your bank.";
	}

	@Override
	public SpecialAttack getSpecialAttack() {
		return SpecialAttack.ITEM;
	}

	@Override
	public int getStoreSize() {
		return 30;
	}

	@Override
	public boolean submitSpecial(Object object) {
		Item item = (Item) object;
		if (!getOwner().getBank().isFull()) {
			boolean success = getOwner().getBank().deposit(new Item(item, 1), getOwner().getInventory());
			if(success) {
				getOwner().message("Your pack yak has sent an item to your bank.");
				getOwner().graphic(1316);
				getOwner().animate(7660);
			}
			return success;
		}
		//Failed because bank full so return scroll
		getOwner().getBank().tab().full();
		getOwner().getInventory().add(new Item(getPouch().getScrollId(), 1));
		return false;
	}
}
