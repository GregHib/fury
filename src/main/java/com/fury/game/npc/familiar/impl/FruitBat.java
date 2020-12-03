package com.fury.game.npc.familiar.impl;

import com.fury.core.model.node.entity.actor.figure.player.Player;
import com.fury.game.content.skill.member.summoning.impl.Summoning;
import com.fury.core.model.item.Item;
import com.fury.game.npc.familiar.Familiar;
import com.fury.game.world.FloorItemManager;
import com.fury.game.world.World;
import com.fury.game.world.map.Position;
import com.fury.game.world.update.flag.block.graphic.Graphic;
import com.fury.util.Misc;

public class FruitBat extends Familiar {

	private static final int[] FRUITS = new int[]{ 5972, 5974, 2102, 2120, 1963, 2108, 5982 };
	private int fruitTicks;

	public FruitBat(Player owner, Summoning.Pouches pouch, Position tile) {
		super(owner, pouch, tile);
	}

	@Override
	public void processNpc() {
		super.processNpc();
		fruitTicks++;
		if (fruitTicks == 500)
			addFruitReward();
	}

	private void addFruitReward() {
		getBeastOfBurden().add(new Item(FRUITS[Misc.random(FRUITS.length)], 1));
		fruitTicks = 0;
	}

	@Override
	public String getSpecialName() {
		return "Fruitfall";
	}

	@Override
	public String getSpecialDescription() {
		return "Bat can get up to eight fruit and drop them on the ground around the player.";
	}

	@Override
	public int getStoreSize() {
		return 30;
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
		Player player = (Player) object;
		player.animate(7660);
		player.graphic(1316);
		Position tile;
		for (int trycount = 0; trycount < Misc.getRandom(8); trycount++) {
			tile = new Position(this, 2);
			if (!World.isTileFree(tile.getX(), tile.getY(), getZ(), player.getSizeX(), player.getSizeY()))
				continue;
			Graphic.sendGlobal(player, new Graphic(1331), tile);
			FloorItemManager.addGroundItem(new Item(FRUITS[Misc.random(FRUITS.length)], 1), tile, player);
		}
		return false;
	}
}
