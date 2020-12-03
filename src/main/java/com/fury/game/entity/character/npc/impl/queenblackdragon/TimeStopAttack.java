package com.fury.game.entity.character.npc.impl.queenblackdragon;


import com.fury.core.task.Task;
import com.fury.core.model.node.entity.actor.figure.mob.Mob;
import com.fury.core.model.node.entity.actor.figure.player.Player;
import com.fury.game.world.GameWorld;
import com.fury.util.Utils;

import java.util.Iterator;

/**
 * Handles the Queen Black Dragon's time stop attack.
 * 
 * @author Emperor
 * 
 */
public final class TimeStopAttack implements QueenAttack {

	/**
	 * The messages the soul says.
	 */
	private static final String[] MESSAGES =
	{
		new String("Kill me, mortal... quickly! HURRY! BEFORE THE SPELL IS COMPLETE!"),
		new String("Time is short!"),
		new String("She is pouring her energy into me... hurry!"),
		new String("The spell is nearly complete!") };

	@Override
	public int attack(final QueenBlackDragon npc, final Player victim) {
		for (Iterator<TorturedSoul> it = npc.getSouls().iterator(); it.hasNext();) {
			TorturedSoul soul = it.next();
			if (soul.getHealth().getHitpoints() <= 0) {
				it.remove();
			}
		}
		if (npc.getSouls().isEmpty()) {
			return 1;
		}
		final TorturedSoul soul = npc.getSouls().get(Utils.random(npc.getSouls().size()));
		soul.setPosition(Utils.random(2) == 0 ? npc.getBase().transform(24, 28, 0) : npc.getBase().transform(42, 28, 0));
		soul.moveTo(soul);
		soul.perform(TorturedSoul.Companion.getTELEPORT_GRAPHIC());
		soul.perform(TorturedSoul.Companion.getTELEPORT_ANIMATION());
		soul.getMovement().lock();
		GameWorld.schedule(new Task(false, 3) {
			private int stage = -1;
			@Override
			public void run() {

				stage++;
				if (stage == 8) {
					npc.getTemporaryAttributes().put("_time_stop_atk", npc.getTicks() + Utils.random(50) + 40);
					for (TorturedSoul s : npc.getSouls()) {
						s.getMovement().unlock();
					}
					for (Mob worm : npc.getWorms()) {
						worm.getMovement().unlock();
					}
					victim.getMovement().unlock();
					//victim.getPacketSender().sendCSVarInteger(1925, 0);
					stop();
					return;
				} else if (stage == 4) {
					for (TorturedSoul s : npc.getSouls()) {
						s.getMovement().lock();
					}
					for (Mob worm : npc.getWorms()) {
						worm.getMovement().lock();
					}
					victim.getMovement().lock();
					soul.getMovement().unlock();
					//victim.getPacketSender().sendCSVarInteger(1925, 1);
					victim.getPacketSender().sendMessage("he tortured soul has stopped time for everyone except himself and the Queen Black", 0x33900);
					victim.getPacketSender().sendMessage("Dragon.", 0x33900);
					return;
				} else if (stage > 3) {
					return;
				}
				if (soul.getHealth().getHitpoints() <= 0) {
					stop();
					return;
				}
				soul.forceChat(MESSAGES[stage]);
			}
		});
		npc.getTemporaryAttributes().put("_time_stop_atk", 9999999);
		return Utils.random(5, 10);
	}

	@Override
	public boolean canAttack(QueenBlackDragon npc, Player victim) {
		if (npc.getSouls().isEmpty()) {
			return false;
		}
		Integer tick = (Integer) npc.getTemporaryAttributes().get("_time_stop_atk");
		return tick == null || tick < npc.getTicks();
	}

}