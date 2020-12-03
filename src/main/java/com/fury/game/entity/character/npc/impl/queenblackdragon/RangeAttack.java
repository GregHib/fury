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
 * Handles the Queen Black Dragon's range attack.
 * 
 * @author Emperor
 * 
 */
public final class RangeAttack implements QueenAttack {

	/**
	 * The animation.
	 */
	private static final Animation ANIMATION = new Animation(16718, Revision.PRE_RS3);

	@Override
	public int attack(final QueenBlackDragon npc, final Player victim) {
		npc.perform(ANIMATION);
		GameWorld.schedule(1, () -> {
			int hit = Utils.random(Utils.random(140) + 50, 299);
			if (victim.getPrayer().isRangeProtecting()) {
				victim.perform(new Animation(WeaponAnimations.getBlockAnimation(victim), Revision.PRE_RS3));
				hit /= 2;
			} else {
				victim.perform(new Animation(WeaponAnimations.getBlockAnimation(victim), Revision.PRE_RS3));
			}
			victim.getCombat().applyHit(new Hit(npc, hit, hit == 0 ? HitMask.NONE : HitMask.RED, hit == 0 ? CombatIcon.BLOCK : CombatIcon.RANGED));
		});
		return Utils.random(4, 15);
	}

	@Override
	public boolean canAttack(QueenBlackDragon npc, Player victim) {
		return true;
	}

}