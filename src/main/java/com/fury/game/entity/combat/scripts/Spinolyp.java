package com.fury.game.entity.combat.scripts;

import com.fury.core.model.node.entity.actor.figure.Figure;
import com.fury.core.model.node.entity.actor.figure.Projectile;
import com.fury.core.model.node.entity.actor.figure.mob.Mob;
import com.fury.game.entity.combat.CombatScript;
import com.fury.game.node.entity.actor.figure.ProjectileManager;
import com.fury.game.system.files.loaders.npc.MobCombatDefinitions;
import com.fury.util.Misc;

public class Spinolyp extends CombatScript {
    @Override
    public Object[] getKeys() {
        return new Object[] {"Spinolyp"};
    }

    @Override
    public int attack(Mob mob, Figure target) {
        MobCombatDefinitions defs = mob.getCombatDefinition();

        if(Misc.random(1) == 0) {
            int damage = getRandomMaxHit(mob, defs.getMaxHit(), MobCombatDefinitions.MAGE, target);
            delayHit(mob, 2, target, getMagicHit(mob, damage));
            ProjectileManager.send(new Projectile(mob, target, 2703, 40, 39, 20, 30, 16, 0));
        } else {
            int damage = getRandomMaxHit(mob, defs.getMaxHit(), MobCombatDefinitions.RANGE, target);
            delayHit(mob, 2, target, getRangeHit(mob, damage));
            //not correct fx :(
            ProjectileManager.send(new Projectile(mob, target, 473, 40, 39, 20, 30, 16, 0));
        }
        return defs.getAttackDelay();
    }
}
