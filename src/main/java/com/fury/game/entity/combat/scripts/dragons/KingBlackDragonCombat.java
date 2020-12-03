package com.fury.game.entity.combat.scripts.dragons;

import com.fury.core.model.node.entity.actor.figure.Figure;
import com.fury.core.model.node.entity.actor.figure.Projectile;
import com.fury.core.model.node.entity.actor.figure.player.Player;
import com.fury.game.entity.character.combat.CombatConstants;
import com.fury.game.entity.character.combat.effects.Effects;
import com.fury.core.model.node.entity.actor.figure.mob.Mob;
import com.fury.game.entity.combat.CombatScript;
import com.fury.game.node.entity.actor.figure.ProjectileManager;
import com.fury.game.system.files.loaders.npc.MobCombatDefinitions;
import com.fury.game.world.update.flag.block.Animation;
import com.fury.util.Utils;

public class KingBlackDragonCombat extends CombatScript {
    @Override
    public Object[] getKeys() {
        return new Object[]{50};
    }

    @Override
    public int attack(Mob mob, Figure target) {
        final MobCombatDefinitions defs = mob.getCombatDefinition();
        int attackStyle = Utils.getRandom(5);
        int sizeX = mob.getSizeX();
        int sizeY = mob.getSizeY();

        if (attackStyle == 0) {
            int distanceX = target.getX() - mob.getX();
            int distanceY = target.getY() - mob.getY();
            //TODO colides?
            if (distanceX > sizeX || distanceX < -1 || distanceY > sizeY || distanceY < -1)
                attackStyle = Utils.getRandom(4) + 1;
            else {
                delayHit(mob, 0, target, getMeleeHit(mob, getRandomMaxHit(mob, defs.getMaxHit(), MobCombatDefinitions.MELEE, target)));
                mob.perform(new Animation(defs.getAttackAnim()));
                return defs.getAttackDelay();
            }
        } else if (attackStyle == 1 || attackStyle == 2) {
            int damage = Utils.getRandom(650);
            final Player player = target.isPlayer() ? (Player) target : null;
            if (CombatConstants.hasAntiDragProtection(target) || (player != null && player.getPrayer().isMageProtecting()))
                damage = 0;
            if (player != null && (player.getEffects().hasActiveEffect(Effects.FIRE_IMMUNITY) || player.getEffects().hasActiveEffect(Effects.SUPER_FIRE_IMMUNITY))) {
                if (damage != 0)
                    damage = Utils.getRandom(164);
            } else if (damage == 0)
                damage = Utils.getRandom(164);
            else if (player != null)
                player.message("You are hit by the dragon's fiery breath!", true);
            delayHit(mob, 2, target, getRegularHit(mob, damage));
            ProjectileManager.send(new Projectile(mob, target, 393, 34, 16, 30, 35, 16, 0));
            mob.animate(81);
        } else if (attackStyle == 3) {
            int damage;
            final Player player = target.isPlayer() ? (Player) target : null;
            if (CombatConstants.hasAntiDragProtection(target)) {
                damage = getRandomMaxHit(mob, 164, MobCombatDefinitions.MAGE, target);
                if (player != null)
                    player.message("Your shield absorbs most of the dragon's poisonous breath!", true);
            } else if (player != null && player.getPrayer().isMageProtecting()) {
                damage = getRandomMaxHit(mob, 164, MobCombatDefinitions.MAGE, target);
                if (player != null)
                    player.message("Your prayer absorbs most of the dragon's poisonous breath!", true);
            } else {
                damage = Utils.getRandom(650);
                if (player != null)
                    player.message("You are hit by the dragon's poisonous breath!", true);
            }
            if (Utils.getRandom(2) == 0)
                target.getEffects().makePoisoned(80);
            delayHit(mob, 2, target, getRegularHit(mob, damage));
            ProjectileManager.send(new Projectile(mob, target, 394, 34, 16, 30, 35, 16, 0));
            mob.animate(81);
        } else if (attackStyle == 4) {
            int damage;
            final Player player = target.isPlayer() ? (Player) target : null;
            if (CombatConstants.hasAntiDragProtection(target)) {
                damage = getRandomMaxHit(mob, 164, MobCombatDefinitions.MAGE, target);
                if (player != null)
                    player.message("Your shield absorbs most of the dragon's freezing breath!", true);
            } else if (player != null && player.getPrayer().isMageProtecting()) {
                damage = getRandomMaxHit(mob, 164, MobCombatDefinitions.MAGE, target);
                if (player != null)
                    player.message("Your prayer absorbs most of the dragon's freezing breath!", true);
            } else {
                damage = Utils.getRandom(650);
                if (player != null)
                    player.message("You are hit by the dragon's freezing breath!", true);
            }
            if (Utils.getRandom(2) == 0)
                target.getCombat().addFreezeDelay(15000, true);
            delayHit(mob, 2, target, getRegularHit(mob, damage));
            ProjectileManager.send(new Projectile(mob, target, 395, 34, 16, 30, 35, 16, 0));
            mob.animate(81);
        } else {
            int damage;
            final Player player = target.isPlayer() ? (Player) target : null;
            if (CombatConstants.hasAntiDragProtection(target)) {
                damage = getRandomMaxHit(mob, 164, MobCombatDefinitions.MAGE, target);
                if (player != null)
                    player.message("Your shield absorbs most of the dragon's shocking breath!", true);
            } else if (player != null && player.getPrayer().isMageProtecting()) {
                damage = getRandomMaxHit(mob, 164, MobCombatDefinitions.MAGE, target);
                if (player != null)
                    player.message("Your prayer absorbs most of the dragon's shocking breath!", true);
            } else {
                damage = Utils.getRandom(650);
                if (player != null)
                    player.message("You are hit by the dragon's shocking breath!", true);
            }
            delayHit(mob, 2, target, getRegularHit(mob, damage));
            ProjectileManager.send(new Projectile(mob, target, 396, 34, 16, 30, 35, 16, 0));
            mob.animate(81);
        }
        return defs.getAttackDelay();
    }
}
