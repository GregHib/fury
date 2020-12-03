package com.fury.game.entity.combat.scripts.rfd;

import com.fury.core.model.node.entity.actor.figure.Figure;
import com.fury.core.model.node.entity.actor.figure.Projectile;
import com.fury.core.model.node.entity.actor.figure.mob.Mob;
import com.fury.game.entity.combat.CombatScript;
import com.fury.game.node.entity.actor.figure.ProjectileManager;
import com.fury.game.system.files.loaders.npc.MobCombatDefinitions;
import com.fury.game.world.update.flag.block.Animation;
import com.fury.util.Utils;

public class CulinaromancerCombat extends CombatScript {

    @Override
    public Object[] getKeys() {
        return new Object[] { 3491 };
    }


    @Override
    public int attack(Mob mob, Figure target) {
        final MobCombatDefinitions def = mob.getCombatDefinition();
        mob.perform(new Animation(def.getAttackAnim()));
        if (Utils.isOnRange(target, mob, 0))
            delayHit(mob, 0, target, getMeleeHit(mob, getRandomMaxHit(mob, def.getAttackStyle(), target)));
        else {
            Projectile projectile = new Projectile(mob, target, 599, 44, 30, 35, 3, 10, 0);
            ProjectileManager.send(projectile);
            delayHit(mob, projectile.getTickDelay(), target, getMagicHit(mob, getRandomMaxHit(mob, 250, MobCombatDefinitions.MAGE, target)));
        }
        return mob.getCombat().getAttackSpeed();
    }

}
