package com.fury.game.entity.character.npc.impl.queenblackdragon;


import com.fury.cache.Revision;
import com.fury.core.model.node.entity.actor.figure.Projectile;
import com.fury.core.model.node.entity.actor.figure.SpeedProjectile;
import com.fury.core.model.node.entity.actor.figure.player.Player;
import com.fury.core.task.Task;
import com.fury.game.entity.character.combat.CombatIcon;
import com.fury.game.entity.character.combat.Hit;
import com.fury.game.node.entity.actor.figure.ProjectileManager;
import com.fury.game.system.files.loaders.item.WeaponAnimations;
import com.fury.game.world.GameWorld;
import com.fury.game.world.update.flag.block.Animation;
import com.fury.game.world.update.flag.block.HitMask;
import com.fury.game.world.update.flag.block.graphic.Graphic;
import com.fury.util.Utils;

/**
 * Handles the super dragonfire attack.
 * 
 * @author Emperor
 * 
 */
public final class SuperFireAttack implements QueenAttack {

	/**
	 * The animation.
	 */
	private static final Animation ANIMATION = new Animation(16745, Revision.PRE_RS3);

	/**
	 * The graphics.
	 */
	private static final Graphic GRAPHIC = new Graphic(3152, Revision.PRE_RS3);

	private static final Projectile FIRE = new SpeedProjectile(3152, Revision.PRE_RS3, 25, 0, 0, 10, 0);

	@Override
	public int attack(final QueenBlackDragon npc, final Player victim) {
		npc.perform(ANIMATION);
		victim.getPacketSender().sendMessage("The Queen Black Dragon gathers her strength to breath extremely hot flames.", 0xffcc00);
		GameWorld.schedule(4, new Runnable() {
			int count = 0;
			@Override
			public void run() {
				GameWorld.schedule(new Task(true, 1) {
					@Override
					public void run() {
						ProjectileManager.send(FIRE.setPositions(npc, victim));
						int hit = 195;
						int distance = Utils.getDistance(npc.getBase().transform(33, 31, 0), victim);
						hit /= (distance / 3) + 1;
						victim.perform(new Animation(WeaponAnimations.getBlockAnimation(victim), Revision.PRE_RS3));
						victim.getCombat().applyHit(new Hit(npc, hit, HitMask.RED, CombatIcon.MAGIC));
						if (++count == 3) {
							stop();
						}
					}
				});
			}
		});
		return Utils.random(8, 15);
	}

	@Override
	public boolean canAttack(QueenBlackDragon npc, Player victim) {
		return true;
	}

}