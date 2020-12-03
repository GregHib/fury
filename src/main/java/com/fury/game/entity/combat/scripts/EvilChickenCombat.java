package com.fury.game.entity.combat.scripts;

import com.fury.core.model.node.entity.actor.figure.Figure;
import com.fury.core.model.node.entity.actor.figure.Projectile;
import com.fury.core.model.node.entity.actor.figure.mob.Mob;
import com.fury.game.entity.combat.CombatScript;
import com.fury.game.node.entity.actor.figure.ProjectileManager;
import com.fury.game.system.files.loaders.npc.MobCombatDefinitions;
import com.fury.game.world.update.flag.block.Animation;
import com.fury.util.Misc;

public class EvilChickenCombat extends CombatScript {
    @Override
    public Object[] getKeys() {
        return new Object[] { 3375 };
    }

    @Override
    public int attack(Mob mob, Figure target) {
        MobCombatDefinitions defs = mob.getCombatDefinition();

        if(Misc.random(5) == 0)
            mob.forceChat("Bwuk bwuk bwuk");

        mob.perform(new Animation(defs.getAttackAnim()));
        Projectile projectile = new Projectile(mob, target, defs.getAttackProjectile(), 30, 30, 25, 3, 0, 0);
        ProjectileManager.send(projectile);
        delayHit(mob, projectile.getTickDelay(), target, getMagicHit(mob, getRandomMaxHit(mob, MobCombatDefinitions.MAGE, target)));
        return defs.getAttackDelay();
    }
}
