package com.fury.game.entity.combat.scripts;

import com.fury.core.model.node.entity.actor.figure.Figure;
import com.fury.core.model.node.entity.actor.figure.mob.Mob;
import com.fury.game.entity.combat.CombatScript;
import com.fury.game.npc.bosses.bork.Bork;
import com.fury.game.system.files.loaders.npc.MobCombatDefinitions;
import com.fury.game.world.update.flag.block.Animation;

public class BorkCombat extends CombatScript {

    @Override
    public Object[] getKeys() {
        return new Object[] { "Bork" };
    }


    @Override
    public int attack(Mob mob, Figure target) {
        final MobCombatDefinitions cdef = mob.getCombatDefinition();
        Bork bork = (Bork) mob;
        if (mob.getHealth().getHitpoints() <= (cdef.getHitpoints() * 0.6) && !bork.isSpawnedMinions()) {
            bork.spawnMinions();
            return 0;
        }
        mob.perform(new Animation(cdef.getAttackAnim()));
        delayHit(mob, 0, target, getMeleeHit(mob, getRandomMaxHit(mob, cdef.getMaxHit(), MobCombatDefinitions.MELEE, target)));
        return mob.getCombat().getAttackSpeed();
    }

}
