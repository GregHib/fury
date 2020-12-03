package com.fury.game.entity.combat.scripts.slayer;

import com.fury.core.model.node.entity.actor.figure.Figure;
import com.fury.core.model.node.entity.actor.figure.mob.Mob;
import com.fury.game.entity.combat.CombatScript;
import com.fury.game.system.files.loaders.npc.MobCombatDefinitions;
import com.fury.game.world.update.flag.block.Animation;
import com.fury.util.Utils;

public class LivingRockStrickerCombat extends CombatScript {
    @Override
    public Object[] getKeys() {
        return new Object[] { 8833 };
    }

    @Override
    public int attack(Mob mob, Figure target) {
        final MobCombatDefinitions defs = mob.getCombatDefinition();
        if (!Utils.isOnRange(target, mob, 0)) {
            // TODO add projectile
            mob.animate(12196);
            delayHit(mob, 1, target, getRangeHit(mob, getRandomMaxHit(mob, MobCombatDefinitions.RANGE, target)));
        } else {
            mob.perform(new Animation(defs.getAttackAnim()));
            delayHit(mob, 0, target, getMeleeHit(mob, getRandomMaxHit(mob, 84, MobCombatDefinitions.MELEE, target)));
            return mob.getCombat().getAttackSpeed();
        }
        return mob.getCombat().getAttackSpeed();
    }
}
