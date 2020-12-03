package com.fury.game.entity.combat.scripts.dragons;

import com.fury.core.model.node.entity.actor.figure.Figure;
import com.fury.core.model.node.entity.actor.figure.Projectile;
import com.fury.game.entity.character.combat.CombatConstants;
import com.fury.core.model.node.entity.actor.figure.mob.Mob;
import com.fury.game.entity.combat.CombatScript;
import com.fury.game.node.entity.actor.figure.ProjectileManager;
import com.fury.game.system.files.loaders.npc.MobCombatDefinitions;
import com.fury.game.world.update.flag.block.Animation;
import com.fury.game.world.update.flag.block.graphic.Graphic;
import com.fury.util.Utils;

public class FrostDragonCombat extends CombatScript {

    @Override
    public Object[] getKeys() {
        return new Object[] { "Frost dragon" };
    }

    @Override
    public int attack(Mob mob, Figure target) {
        final MobCombatDefinitions defs = mob.getCombatDefinition();
        int damage;
        switch (Utils.getRandom(3)) {
            case 0: // Melee
                if (mob.isWithinDistance(target, 3)) {
                    damage = getRandomMaxHit(mob, defs.getMaxHit(), MobCombatDefinitions.MELEE, target);
                    mob.perform(new Animation(defs.getAttackAnim()));
                    delayHit(mob, 0, target, getMeleeHit(mob, damage));
                } else {
                    damage = Utils.getRandom(650);
                    damage *= CombatConstants.getDragonFireMultiplier(target, true, false);
                    mob.animate(13155);
                    ProjectileManager.send(new Projectile(mob, target, 393, 28, 16, 35, 35, 16, 0));
                    delayHit(mob, 1, target, getRegularHit(mob, damage));
                }
                break;
            case 1: // Dragon breath
                if (mob.isWithinDistance(target, 3)) {
                    damage = Utils.getRandom(650);
                    damage *= CombatConstants.getDragonFireMultiplier(target, true, false);
                    mob.animate(13152);
                    mob.perform(new Graphic(2465));
                    delayHit(mob, 1, target, getRegularHit(mob, damage));
                } else {
                    damage = Utils.getRandom(650);
                    damage *= CombatConstants.getDragonFireMultiplier(target, false, true);
                    mob.animate(13155);
                    ProjectileManager.send(new Projectile(mob, target, 393, 28, 16, 35, 35, 16, 0));
                    delayHit(mob, 1, target, getRegularHit(mob, damage));
                }
                break;
            case 2: // Range
                damage = Utils.getRandom(250);
                mob.animate(13155);
                ProjectileManager.send(new Projectile(mob, target, 2707, 28, 16, 35, 35, 16, 0));
                delayHit(mob, 1, target, getMagicHit(mob, damage));
                break;
            case 3: // Ice arrows range
                damage = Utils.getRandom(250);
                mob.animate(13155);
                ProjectileManager.send(new Projectile(mob, target, 16, 28, 16, 35, 35, 16, 0));
                delayHit(mob, 1, target, getRangeHit(mob, damage));
                break;
            case 4: // Orb crap
                break;
        }
        return defs.getAttackDelay();
    }

}
