package com.fury.game.npc.familiar.impl;

import com.fury.game.content.skill.member.summoning.impl.Summoning;
import com.fury.core.model.node.entity.actor.figure.player.Player;
import com.fury.game.npc.familiar.Familiar;
import com.fury.game.world.map.Position;

public class ClayFamiliarClass3 extends Familiar {

	public ClayFamiliarClass3(Player owner, Summoning.Pouches pouch, Position tile) {
		super(owner, pouch, tile);
	}

	@Override
	public String getSpecialName() {
		return "Clay Deposit";
	}

	@Override
	public String getSpecialDescription() {
		return "Deposit all items in the beast of burden's possession in exchange for points.";
	}

	@Override
	public int getStoreSize() {
		return 12;
	}

	@Override
	public int getSpecialAttackEnergy() {
		return 30;
	}

	@Override
	public SpecialAttack getSpecialAttack() {
		return SpecialAttack.CLICK;
	}

	@Override
	public boolean submitSpecial(Object object) {
//		if (getOwner().getControllerManager().getController() == null || !(getOwner().getControllerManager().getController() instanceof StealingCreationController)) {
//			dismissFamiliar(false);
//			return false;
//		}
		getOwner().graphic(1316);
		getOwner().animate(7660);
//		StealingCreationController sc = (StealingCreationController) getOwner().getControllerManager().getController();
//		Score score = sc.getGame().getScore(getOwner());
//		if (score == null)
//			return false;
//		for (Item item : getBeastOfBurden().getBeastItems().getItems()) {
//			if (item == null)
//				continue;
//			sc.getGame().sendItemToBase(getOwner(), item, sc.getTeam(), true, false);
//		}
		return true;
	}
}
