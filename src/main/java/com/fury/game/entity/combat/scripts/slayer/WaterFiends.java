package com.fury.game.entity.combat.scripts.slayer;

import com.fury.core.model.node.entity.actor.figure.Figure;
import com.fury.core.model.node.entity.actor.figure.Projectile;
import com.fury.core.model.node.entity.actor.figure.mob.Mob;
import com.fury.game.entity.combat.CombatScript;
import com.fury.game.node.entity.actor.figure.ProjectileManager;
import com.fury.game.system.files.loaders.npc.MobCombatDefinitions;
import com.fury.game.world.update.flag.block.Animation;
import com.fury.util.Misc;

public class WaterFiends extends CombatScript {

    @Override
    public Object[] getKeys() {
        return new Object[] { "Waterfiend" };
    }

    @Override
    public int attack(Mob mob, Figure target) {
        MobCombatDefinitions defs = mob.getCombatDefinition();
        mob.perform(new Animation(defs.getAttackAnim()));

        Projectile projectile = new Projectile(mob, target, Misc.random(2) == 0 ? 16 : defs.getAttackProjectile(), 20, 30, 25, 2, 16, 5);
        ProjectileManager.send(projectile);
        delayHit(mob, projectile.getTickDelay(), target, getMagicHit(mob, getRandomMaxHit(mob, MobCombatDefinitions.MAGE, target)));
        return mob.getCombat().getAttackSpeed();
    }

}