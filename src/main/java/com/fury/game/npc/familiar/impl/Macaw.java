package com.fury.game.npc.familiar.impl;


import com.fury.core.model.node.entity.actor.figure.player.Player;
import com.fury.game.content.skill.member.herblore.Herbs;
import com.fury.game.content.skill.member.summoning.impl.Summoning;
import com.fury.core.model.item.Item;
import com.fury.game.npc.familiar.Familiar;
import com.fury.game.world.FloorItemManager;
import com.fury.game.world.GameWorld;
import com.fury.game.world.map.Position;
import com.fury.game.world.update.flag.block.graphic.Graphic;
import com.fury.util.Misc;

public class Macaw extends Familiar {

	private int specialLock = -1;

	public Macaw(Player owner, Summoning.Pouches pouch, Position tile) {
		super(owner, pouch, tile);
	}

	@Override
	public String getSpecialName() {
		return "Herbcall";
	}

	@Override
	public void processNpc() {
		super.processNpc();
		if (specialLock > 0)
			specialLock--;
		else if (specialLock == 0) {
			specialLock = -1;
			getOwner().message("Your macaw feels rested and ready for flight again.");
		}
	}

	@Override
	public String getSpecialDescription() {
		return "Can produce one herb, all herbs up to and including Torstol, within a 60 second range.";
	}

	@Override
	public int getStoreSize() {
		return 0;
	}

	@Override
	public int getSpecialAttackEnergy() {
		return 12;
	}

	@Override
	public SpecialAttack getSpecialAttack() {
		return SpecialAttack.CLICK;
	}

	@Override
	public boolean submitSpecial(Object object) {
		if (specialLock > 0) {
			getOwner().message("Your macaw is too tired to continue searching for herbs.");
			return false;
		}
		specialLock = 100;
		getOwner().graphic(1300);
		getOwner().animate(7660);
		animate(8013);
		final Position tile = new Position(getOwner().getX() - 1, getOwner().getY(), getOwner().getZ());
		GameWorld.schedule(2, () -> Graphic.sendGlobal(getOwner(), new Graphic(1321), tile));
		GameWorld.schedule(5, () -> animate(8014));
		Herbs herb;
		if (Misc.random(100) >= 5)
			herb = Herbs.values()[Misc.random(Herbs.getHerbs().size())];
		else
			herb = Herbs.values()[Misc.random(3)];
		FloorItemManager.addGroundItem(new Item(herb.getHerbId(), 1), tile, getOwner());
		return true;
	}
}
