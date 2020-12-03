package com.fury.game.entity.combat.scripts.slayer;

import com.fury.core.model.node.entity.actor.figure.Figure;
import com.fury.core.model.node.entity.actor.figure.Projectile;
import com.fury.core.model.node.entity.actor.figure.mob.Mob;
import com.fury.game.entity.combat.CombatScript;
import com.fury.game.node.entity.actor.figure.ProjectileManager;
import com.fury.game.system.files.loaders.npc.MobCombatDefinitions;
import com.fury.util.Utils;

public class DarkBeastCombat extends CombatScript {

    @Override
    public Object[] getKeys() {
        return new Object[] { 2783 };
    }

    @Override
    public int attack(Mob mob, final Figure target) {
        mob.animate(2731);
        if (Utils.isOnRange(target, mob, 0))
            delayHit(mob, 0, target, getMeleeHit(mob, getRandomMaxHit(mob, mob.getCombatDefinition().getAttackStyle(), target)));
        else {
            Projectile projectile = new Projectile(mob, target, 2181, 41, 16, 35, 3, 10, 0);
            ProjectileManager.send(projectile);
            delayHit(mob, projectile.getTickDelay(), target, getMagicHit(mob, getRandomMaxHit(mob, MobCombatDefinitions.MAGE, target)));
        }
        return mob.getCombat().getAttackSpeed();
    }
}