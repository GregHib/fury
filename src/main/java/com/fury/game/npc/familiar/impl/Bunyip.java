package com.fury.game.npc.familiar.impl;


import com.fury.game.content.skill.Skill;
import com.fury.game.content.skill.free.cooking.Consumables;
import com.fury.game.content.skill.free.fishing.Fish;
import com.fury.game.content.skill.member.summoning.impl.Summoning;
import com.fury.core.model.node.entity.actor.figure.player.Player;
import com.fury.core.model.item.Item;
import com.fury.game.npc.familiar.Familiar;
import com.fury.game.world.map.Position;

public class Bunyip extends Familiar {
	private int restoreTicks;

	public Bunyip(Player owner, Summoning.Pouches pouch, Position tile) {
		super(owner, pouch, tile);
	}

	@Override
	public String getSpecialName() {
		return "Swallow Whole";
	}

	@Override
	public String getSpecialDescription() {
		return "Eat an uncooked fish and gain the correct number of life points corresponding to the fish eaten if you have the cooking level to cook the fish.";
	}

	@Override
	public int getStoreSize() {
		return 0;
	}

	@Override
	public int getSpecialAttackEnergy() {
		return 7;
	}

	@Override
	public SpecialAttack getSpecialAttack() {
		return SpecialAttack.ITEM;
	}

	@Override
	public void processNpc() {
		super.processNpc();
		restoreTicks++;
		if (restoreTicks == 20) { // approx 15 secs
			getOwner().getHealth().heal(20);
			getOwner().graphic(1507);
			restoreTicks = 0;
		}
	}

	@Override
	public boolean submitSpecial(Object object) {
		Item item = getOwner().getInventory().get((Integer) object);
		if (item == null)
			return false;
		for (Fish fish : Fish.values()) {
			if (fish.getId() == item.getId()) {
				if (getOwner().getSkills().getLevel(Skill.COOKING) < fish.getLevel()) {
					getOwner().message("Your cooking level is not high enough for the bunyip to eat this fish.");
					return false;
				} else {
					getOwner().graphic(1316);
					getOwner().animate(7660);
					getOwner().getHealth().heal(Consumables.getHealAmount(item));
					getOwner().getInventory().delete(new Item(item.getId(), item.getAmount()));
					return true;// stop here
				}
			}
		}
		getOwner().message("Your bunyip cannot eat this.");
		return false;
	}
}
