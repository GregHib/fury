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

public class SpiritSpider extends Familiar {

	public SpiritSpider(Player owner, Summoning.Pouches pouch, Position tile) {
		super(owner, pouch, tile);
	}

	@Override
	public String getSpecialName() {
		return "Egg Spawn";
	}

	@Override
	public String getSpecialDescription() {
		return "Spawns a random amount of red eggs around the familiar.";
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
		return SpecialAttack.CLICK;
	}

	@Override
	public boolean submitSpecial(Object object) {
		Player player = (Player) object;
//		animate(8267);
		player.animate(7660);
		player.perform(new Graphic(1316));
		Position tile;
		// attemps to randomize tile by 4x4 area
		for (int attempt = 0; attempt < Misc.getRandom(10); attempt++) {
			tile = new Position(this, 2);
			if (!World.isTileFree(tile.getX(), tile.getY(), getZ(), player.getSizeX(), player.getSizeY()))
				return true;
			Graphic.sendGlobal(player, new Graphic(1342), tile);
			FloorItemManager.addGroundItem(new Item(223, 1), tile, player);
		}
		return true;
	}
}
