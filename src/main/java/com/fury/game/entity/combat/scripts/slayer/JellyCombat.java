package com.fury.game.entity.combat.scripts.slayer;

import com.fury.core.model.node.entity.actor.figure.Figure;
import com.fury.core.model.node.entity.actor.figure.mob.Mob;
import com.fury.game.entity.combat.CombatScript;
import com.fury.game.system.files.loaders.npc.MobCombatDefinitions;
import com.fury.game.world.update.flag.block.Animation;

public class JellyCombat extends CombatScript {

    @Override
    public Object[] getKeys() {
        return new Object[] { "Jelly" };
    }

    @Override
    public int attack(Mob mob, Figure target) {
        mob.perform(new Animation(mob.getCombatDefinition().getAttackAnim()));
        delayHit(mob, 2, target, getRangeHit(mob, getRandomMaxHit(mob, MobCombatDefinitions.MELEE, target)));
        return mob.getCombat().getAttackSpeed();
    }
}
