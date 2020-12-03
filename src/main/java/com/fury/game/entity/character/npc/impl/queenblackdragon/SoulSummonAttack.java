package com.fury.game.entity.character.npc.impl.queenblackdragon;

import com.fury.core.model.node.entity.actor.figure.player.Player;
import com.fury.util.Utils;

import java.util.Iterator;

/**
 * Handles the summoning of the tortured souls.
 * 
 * @author Emperor
 * 
 */
public final class SoulSummonAttack implements QueenAttack {

	/**
	 * The spawn offset locations.
	 */
	private static final int[][] SPAWN_LOCATIONS =
	{
	{ 31, 35 },
	{ 33, 35 },
	{ 34, 33 },
	{ 31, 29 } };

	@Override
	public int attack(QueenBlackDragon npc, Player victim) {
		for (Iterator<TorturedSoul> it = npc.getSouls().iterator(); it.hasNext();) {
			if (it.next().getHealth().getHitpoints() <= 0) {
				it.remove();
			}
		}
		npc.getTemporaryAttributes().put("_last_soul_summon", npc.getTicks() + Utils.random(41, 100));
		int count = 3 - 1;
		if (count == 3) {
			count = 4;
		}
		if (npc.getSouls().size() < count) {
			victim.getPacketSender().sendMessage((count - npc.getSouls().size()) < 2 ? "The Queen Black Dragon summons one of her captive, tortured souls." : "The Queen Black Dragon summons several of her captive, tortured souls.", 0x9900cc);
			for (int i = npc.getSouls().size(); i < count; i++) {
				TorturedSoul soul = new TorturedSoul(npc, victim, npc.getBase().transform(SPAWN_LOCATIONS[i][0], SPAWN_LOCATIONS[i][1], 0));
				System.out.println("spawning tortured soul");
				//System.out.println();
				soul.setCantInteract(false);
				soul.setTargetDistance(7);
				soul.getDirection().setInteracting(victim);
				soul.getMobCombat().setTarget(victim);
				npc.getSouls().add(soul);
			}
		}
		for (int i = 0; i < count; i++) {
			if (i >= npc.getSouls().size()) {
				break;
			}
			TorturedSoul s = npc.getSouls().get(i);
			if (s == null || s.getHealth().getHitpoints() <= 0) {
				continue;
			}
			//s.specialAttack(npc.getBase().transform(SPAWN_LOCATIONS[i][0], SPAWN_LOCATIONS[i][1], 0));
		}
		return Utils.random(4, 15);
	}

	@Override
	public boolean canAttack(QueenBlackDragon npc, Player victim) {
		Integer last = (Integer) npc.getTemporaryAttributes().get("_last_soul_summon");
		return last == null || last < npc.getTicks();
	}

}