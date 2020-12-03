package com.fury.game.entity.character.npc.impl.queenblackdragon;


import com.fury.cache.Revision;
import com.fury.game.entity.character.combat.CombatIcon;
import com.fury.game.entity.character.combat.Hit;
import com.fury.core.model.node.entity.actor.figure.player.Player;
import com.fury.game.system.files.loaders.item.WeaponAnimations;
import com.fury.game.world.GameWorld;
import com.fury.game.world.update.flag.block.Animation;
import com.fury.game.world.update.flag.block.HitMask;
import com.fury.util.Utils;

/**
 * Handles the Queen Black Dragon's melee attack.
 * 
 * @author Emperor
 * 
 */
public final class MeleeAttack implements QueenAttack {

	/**
	 * The default melee animation.
	 */
	private static final Animation DEFAULT = new Animation(16717, Revision.PRE_RS3);

	/**
	 * The east melee animation.
	 */
	private static final Animation EAST = new Animation(16744, Revision.PRE_RS3);

	/**
	 * The west melee animation.
	 */
	private static final Animation WEST = new Animation(16743, Revision.PRE_RS3);

	@Override
	public int attack(final QueenBlackDragon npc, final Player victim) {
		if (victim.getX() < npc.getBase().getX() + 31) {
			npc.perform(WEST);
		} else if (victim.getX() > npc.getBase().getX() + 35) {
			npc.perform(EAST);
		} else {
			npc.perform(DEFAULT);
		}
		GameWorld.schedule(1, () -> {
			int hit = Utils.random(90 + Utils.random(40), 200);
			if (victim.getPrayer().isMeleeProtecting()) {
				victim.perform(new Animation(WeaponAnimations.getBlockAnimation(victim), Revision.PRE_RS3));
				hit /= 2;
			} else {
				victim.perform(new Animation(WeaponAnimations.getBlockAnimation(victim), Revision.PRE_RS3));
			}
			victim.getCombat().applyHit(new Hit(npc, hit, hit == 0 ? HitMask.NONE : HitMask.RED, hit == 0 ? CombatIcon.BLOCK : CombatIcon.MELEE));
		});
		return Utils.random(4, 15);
	}

	@Override
	public boolean canAttack(QueenBlackDragon npc, Player victim) {
		return victim.getY() > npc.getBase().getY() + 32;
	}

}