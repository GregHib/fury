package com.fury.game.entity.character.npc.impl.queenblackdragon;

import com.fury.cache.Revision;
import com.fury.core.task.Task;
import com.fury.game.entity.character.combat.CombatIcon;
import com.fury.game.entity.character.combat.Hit;
import com.fury.core.model.node.entity.actor.figure.player.Player;
import com.fury.game.world.GameWorld;
import com.fury.game.world.update.flag.block.HitMask;
import com.fury.game.world.update.flag.block.graphic.Graphic;
import com.fury.util.Utils;

import java.util.Iterator;

/**
 * The Queen Black Dragon's soul siphon attack.
 * 
 * @author Emperor
 * 
 */
public final class SoulSiphonAttack implements QueenAttack {

	/**
	 * The siphon graphics.
	 */
	private static final Graphic SIPHON_GRAPHIC = new Graphic(3148, Revision.PRE_RS3);

	@Override
	public int attack(final QueenBlackDragon npc, Player victim) {
		for (Iterator<TorturedSoul> it = npc.getSouls().iterator(); it.hasNext();) {
			TorturedSoul soul = it.next();
			if (soul.getHealth().getHitpoints() <= 0) {
				it.remove();
			}
		}
		if (npc.getSouls().isEmpty()) {
			return 1;
		}
		victim.getPacketSender().sendMessage("The Queen Black Dragon starts to siphon the energy of her mages.", 0x9900cc);
		GameWorld.schedule(new Task(true) {
			@Override
			public void run() {
				for (Iterator<TorturedSoul> it = npc.getSouls().iterator(); it.hasNext();) {
					TorturedSoul soul = it.next();
					if (soul.getHealth().getHitpoints() <= 0) {
						it.remove();
						continue;
					}
					soul.perform(SIPHON_GRAPHIC);
					soul.getCombat().applyHit(new Hit(100, HitMask.CRITICAL, CombatIcon.NONE));
					npc.getHealth().heal(200);
				}
				if (npc.getSouls().isEmpty()) {
					npc.getTemporaryAttributes().put("_last_soul_summon", npc.getTicks() + Utils.random(120) + 125);
					stop();
				}
			}
		});
		npc.getTemporaryAttributes().put("_last_soul_summon", npc.getTicks() + 999);
		npc.getTemporaryAttributes().put("_soul_siphon_atk", npc.getTicks() + 50 + Utils.random(40));
		return Utils.random(5, 10);
	}

	@Override
	public boolean canAttack(QueenBlackDragon npc, Player victim) {
		Integer tick = (Integer) npc.getTemporaryAttributes().get("_soul_siphon_atk");
		return tick == null || tick < npc.getTicks();
	}

}