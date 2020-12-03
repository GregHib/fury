package com.fury.game.entity.character.npc.impl.queenblackdragon;


import com.fury.core.model.node.entity.actor.figure.player.Player;
import com.fury.game.world.GameWorld;
import com.fury.util.Utils;

/**
 * Handles the Queen Black Dragon's change armour "attack".
 * 
 * @author Emperor
 * 
 */
public final class ChangeArmour implements QueenAttack {

	@Override
	public int attack(final QueenBlackDragon npc, Player victim) {
		npc.switchState(Utils.random(2) < 1 ? QueenState.CRYSTAL_ARMOUR : QueenState.HARDEN);
		GameWorld.schedule(40, () -> npc.switchState(QueenState.DEFAULT));
		npc.getTemporaryAttributes().put("_last_armour_change", npc.getTicks() + Utils.random(41, 100));
		return Utils.random(4, 10);
	}

	@Override
	public boolean canAttack(QueenBlackDragon npc, Player victim) {
		Integer last = (Integer) npc.getTemporaryAttributes().get("_last_armour_change");
		return last == null || last < npc.getTicks();
	}

}