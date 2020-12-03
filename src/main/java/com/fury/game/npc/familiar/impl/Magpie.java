package com.fury.game.npc.familiar.impl;


import com.fury.game.content.skill.Skill;
import com.fury.game.content.skill.member.summoning.impl.Summoning;
import com.fury.core.model.node.entity.actor.figure.player.Player;
import com.fury.core.model.item.Item;
import com.fury.game.npc.familiar.Familiar;
import com.fury.game.world.GameWorld;
import com.fury.game.world.map.Position;
import com.fury.util.Misc;

public class Magpie extends Familiar {

	private static final int[] RANDOM_ITEMS = { 1617, 1619, 1621, 1623, 1625, 1627, 1629, 1631, 1635, 1637, 1639, 1641, 1643, 1645, 2552, 2568, 2572, 2570, 2550 };

	private int theivingTicks;

	public Magpie(Player owner, Summoning.Pouches pouch, Position tile) {
		super(owner, pouch, tile);
	}

	@Override
	public void processNpc() {
		super.processNpc();
		if (!getMovement().hasWalkSteps())
			theivingTicks += 2;
		else
			theivingTicks++;
		if (theivingTicks == 30) {
			getBeastOfBurden().add(new Item(RANDOM_ITEMS[Misc.random(RANDOM_ITEMS.length)], 1));
			theivingTicks = 0;
		} else if (theivingTicks % 50 == 0)
			forceChat("*Tweet*");
	}

	@Override
	public String getSpecialName() {
		return "Thieving Fingers";
	}

	@Override
	public String getSpecialDescription() {
		return "Increases theiving level by two.";
	}

	@Override
	public int getStoreSize() {
		return 30;
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
		final Player player = (Player) object;
		graphic(1336);
		animate(8020);
		player.animate(7660);
		GameWorld.schedule(3, () -> player.graphic(1300));
		player.getSkills().boost(Skill.THIEVING, 2);
		return true;
	}
}
