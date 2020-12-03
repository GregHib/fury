package com.fury.game.entity.combat.scripts.slayer;

import com.fury.core.model.node.entity.actor.figure.Figure;
import com.fury.core.model.node.entity.actor.figure.mob.Mob;
import com.fury.game.entity.combat.CombatScript;
import com.fury.game.npc.slayer.Nechryael;
import com.fury.game.world.update.flag.block.Animation;
import com.fury.util.Misc;

public class NechryaelCombat extends CombatScript {

    @Override
    public Object[] getKeys() {
        return new Object[] {"Nechryael"};
    }

    @Override
    public int attack(Mob mob, Figure target) {
        if(mob instanceof Nechryael) {
            Nechryael n = (Nechryael) mob;
            if (Misc.random(10) == 0 && !n.hasActiveSpawns())
                n.summonDeathSpawns();
        }
        mob.perform(new Animation(mob.getCombatDefinition().getAttackAnim()));
        delayHit(mob, 0, target, getMagicHit(mob, getRandomMaxHit(mob, mob.getCombatDefinition().getAttackStyle(), target)));
        return mob.getCombat().getAttackSpeed();
    }
}