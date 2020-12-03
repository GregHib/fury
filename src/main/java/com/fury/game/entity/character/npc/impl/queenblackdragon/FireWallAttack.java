package com.fury.game.entity.character.npc.impl.queenblackdragon;

import com.fury.cache.Revision;
import com.fury.core.model.node.entity.actor.figure.Projectile;
import com.fury.core.model.node.entity.actor.figure.player.Player;
import com.fury.core.task.Task;
import com.fury.game.entity.character.combat.CombatIcon;
import com.fury.game.entity.character.combat.Hit;
import com.fury.game.node.entity.actor.figure.ProjectileManager;
import com.fury.game.world.GameWorld;
import com.fury.game.world.update.flag.block.Animation;
import com.fury.game.world.update.flag.block.HitMask;
import com.fury.util.Utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Represents the Queen Black Dragon's fire wall attack.
 * 
 * @author Emperor
 * 
 */
public final class FireWallAttack implements QueenAttack {

	/**
	 * The wall graphic ids.
	 */
	private static final int[] WALL_GRAPHIC_IDS = { 3158, 3159, 3160 };

	/**
	 * The animation.
	 */
	private static final Animation ANIMATION = new Animation(16746, Revision.PRE_RS3);

	@Override
	public int attack(final QueenBlackDragon npc, final Player victim) {
		int waves = npc.getPhase();
		if (waves > 3)
			waves = 3;
		npc.perform(ANIMATION);
		final List<Integer> wallIds = new ArrayList<>();
		for (int id : WALL_GRAPHIC_IDS)
			wallIds.add(id);

		Collections.shuffle(wallIds);
		victim.getPacketSender().sendMessage("The Queen Black Dragon takes a huge breath.", 0xFF9900);
		final int wallCount = waves;
		int tick = 0;
		GameWorld.schedule(1, () -> {
			for (int i = 0; i < wallCount; i++) {
				final int wallId = wallIds.get(i);
				GameWorld.schedule((i * 7) + 1, new Runnable() {
					@Override
					public void run() {
						for (int j = 0; j < 2; j++) {
							final boolean second = j == 1;
							GameWorld.schedule(new Task(true, 1) {
								int y = 37 + (second ? 1 : 0);
								@Override
								public void run() {
									if (!((wallId == 3158 && victim.getX() == npc.getBase().getX() + 28) || (wallId == 3159 && victim.getX() == npc.getBase().getX() + 37) || (wallId == 3160 && victim.getX() == npc.getBase().getX() + 32))) {
										if (victim.getY() == npc.getBase().getY() + y) {
											int hit;
											String message = FireBreathAttack.getProtectMessage(victim);
											if (message == null) {
												victim.message("You are horribly burned by the fire wall!");
												hit = Utils.random(200, 250);
											} else {
												victim.message(message);
												hit = Utils.random(100, 170);
											}
											victim.getCombat().applyHit(new Hit(npc, hit, HitMask.CRITICAL, CombatIcon.MAGIC));
										}
									}
									if (--y == 19) {
										stop();
									}
								}
							});
						}
						ProjectileManager.send(new Projectile(npc.getBase().transform(33, 38, 0), npc.getBase().transform(33, 19, 0), wallId, Revision.PRE_RS3, 0, 0, 18, 46, 0, 0));
					}
				});
			}
		});
		npc.getTemporaryAttributes().put("fire_wall_tick_", npc.getTicks() + Utils.random((waves * 7) + 5, 60)); // Don't make  it  too  often.
		return 8 + (waves * 2);
	}

	@Override
	public boolean canAttack(QueenBlackDragon npc, Player victim) {
		Integer tick = (Integer) npc.getTemporaryAttributes().get("fire_wall_tick_");
		return tick == null || tick < npc.getTicks();
	}

}