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

public class MetalDragonCombat extends CombatScript {
    @Override
    public Object[] getKeys() {
        return new Object[] { "Bronze dragon", "Iron dragon", "Steel dragon" };
    }

    public int attack(Mob mob, Figure target) {
        MobCombatDefinitions defs = mob.getCombatDefinition();
        int damage;

        if (mob.getCentredPosition().isWithinDistance(target, 2)) {
            if(Utils.getRandom(1) == 0) {
                damage = getRandomMaxHit(mob, defs.getMaxHit(), MobCombatDefinitions.MELEE, target);
                mob.perform(new Animation(defs.getAttackAnim()));
                delayHit(mob, 0, target, getMeleeHit(mob, damage));
            } else {
                damage = Utils.getRandom(650);
                damage *= CombatConstants.getDragonFireMultiplier(target, false, true);
                mob.animate(13164);
                mob.perform(new Graphic(2465));
                delayHit(mob, 1, target, getRegularHit(mob, damage));
            }
        } else {
            damage = Utils.getRandom(650);
            damage *= CombatConstants.getDragonFireMultiplier(target, false, true);
            mob.animate(13160);
            ProjectileManager.send(new Projectile(mob, target, 393, 28, 16, 35, 35, 16, 0));
            delayHit(mob, 1, target, getRegularHit(mob, damage));
        }

        return defs.getAttackDelay();
    }
}
