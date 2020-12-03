package com.fury.game.entity.character.npc.impl.queenblackdragon;


import com.fury.cache.Revision;
import com.fury.core.model.node.entity.actor.figure.Projectile;
import com.fury.core.model.node.entity.actor.figure.SpeedProjectile;
import com.fury.core.model.node.entity.actor.figure.player.Player;
import com.fury.game.container.impl.equip.Slot;
import com.fury.game.entity.character.combat.CombatConstants;
import com.fury.game.entity.character.combat.CombatIcon;
import com.fury.game.entity.character.combat.Hit;
import com.fury.game.entity.character.combat.effects.Effects;
import com.fury.game.node.entity.actor.figure.ProjectileManager;
import com.fury.game.system.files.loaders.item.WeaponAnimations;
import com.fury.game.world.GameWorld;
import com.fury.game.world.update.flag.block.Animation;
import com.fury.game.world.update.flag.block.HitMask;
import com.fury.game.world.update.flag.block.graphic.Graphic;
import com.fury.util.Utils;

/**
 * Represents a default fire breath attack.
 * 
 * @author Emperor
 * 
 */
public final class FireBreathAttack implements QueenAttack {

	/**
	 * The animation of the attack.
	 */
	private static final Animation ANIMATION = new Animation(16721, Revision.PRE_RS3);

	/**
	 * The graphic of the attack.
	 */
	private static final Graphic GRAPHIC = new Graphic(3143, Revision.PRE_RS3);

	private static final Projectile FIRE = new SpeedProjectile(3143, Revision.PRE_RS3, 25, 0, 0, 10, 0);

	@Override
	public int attack(final QueenBlackDragon npc, final Player victim) {
		npc.perform(ANIMATION);
		GameWorld.schedule(1, () -> {
			String message = getProtectMessage(victim);
			int hit;
			ProjectileManager.send(FIRE.setPositions(npc, victim));
			if (message != null) {
				hit = Utils.random(50 + Utils.random(20), message.contains("prayer") ? 255 : 275);
				victim.message(message);
				if (victim.getEquipment().get(Slot.SHIELD).getId() == 11283) {
					CombatConstants.chargeDragonFireShield(victim);
					hit /= 2;
				}
			} else {
				hit = Utils.random(675 + Utils.random(75), 800);
				victim.message("You are horribly burned by the dragon's breath!", true);
			}
			victim.perform(new Animation(WeaponAnimations.getBlockAnimation((victim)), Revision.PRE_RS3));
			victim.getCombat().applyHit(new Hit(npc, hit, HitMask.RED, CombatIcon.MAGIC));
		});
		return Utils.random(4, 15); // Attack delay seems to be random a lot.
	}

	@Override
	public boolean canAttack(QueenBlackDragon npc, Player victim) {
		return true;
	}

	/**
	 * Gets the dragonfire protect message.
	 * 
	 * @param player
	 *            The player.
	 * @return The message to send, or {@code null} if the player was
	 *         unprotected.
	 */
	public static final String getProtectMessage(Player player) {
		if (CombatConstants.hasAntiDragProtection(player)) {
			return "Your shield absorbs most of the dragon's breath!";
		}
		if (player.getEffects().hasActiveEffect(Effects.FIRE_IMMUNITY)
				|| player.getEffects().hasActiveEffect(Effects.SUPER_FIRE_IMMUNITY)) {
			return "Your potion absorbs most of the dragon's breath!";
		}
		if (player.getPrayer().isMageProtecting()) {
			return "Your prayer absorbs most of the dragon's breath!";
		}
		return null;
	}
}