package com.fury.game.npc.familiar.impl;

import com.fury.core.model.node.entity.actor.figure.player.Player;
import com.fury.game.content.skill.Skill;
import com.fury.game.content.skill.free.fishing.Fishing;
import com.fury.game.content.skill.member.summoning.impl.Summoning;
import com.fury.core.model.item.Item;
import com.fury.game.npc.familiar.Familiar;
import com.fury.game.world.FloorItemManager;
import com.fury.game.world.GameWorld;
import com.fury.game.world.map.Position;
import com.fury.game.world.update.flag.block.graphic.Graphic;
import com.fury.util.Misc;

public class Ibis extends Familiar {

	private int forageTicks;

	public Ibis(Player owner, Summoning.Pouches pouch, Position tile) {
		super(owner, pouch, tile);
	}

	@Override
	public void processNpc() {
		super.processNpc();
		boolean isFishing = getOwner().getActionManager().getAction() != null && getOwner().getActionManager().getAction() instanceof Fishing;
		if (isFishing) {
			forageTicks++;
			if (forageTicks == 300)
				giveReward();
		}
	}

	private void giveReward() {
		boolean isSwordFish = Misc.random(3) == 0;
		int foragedItem = isSwordFish ? 371 : 359;
		if (isSwordFish)
			getOwner().getSkills().addExperience(Skill.FISHING, 10);
		getBeastOfBurden().add(new Item(foragedItem, 1));
		forageTicks = 0;
	}

	@Override
	public String getSpecialName() {
		return "Fish rain";
	}

	@Override
	public String getSpecialDescription() {
		return "Makes fish (raw shrimp, bass, cod, and mackerel) fall out of the sky.";
	}

	@Override
	public int getStoreSize() {
		return 10;
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
		final Player player = (Player) object;
		getOwner().graphic(1300);
		getOwner().animate(7660);
		animate(8201);
		final Position firstTile = new Position(player.getX() + 1, player.getY() + 1, player.getZ());
		final Position secondTile = new Position(player.getX() - 1, player.getY() - 1, player.getZ());
		Graphic.sendGlobal(player, new Graphic(1337), firstTile);
		Graphic.sendGlobal(player, new Graphic(1337), secondTile);
		GameWorld.schedule(2, () -> {
			FloorItemManager.addGroundItem(new Item(1, 1), firstTile, player);
			FloorItemManager.addGroundItem(new Item(1, 1), secondTile, player);
		});
		return true;
	}
}
