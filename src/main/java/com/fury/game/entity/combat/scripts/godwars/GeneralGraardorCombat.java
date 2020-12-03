package com.fury.game.entity.combat.scripts.godwars;

import com.fury.core.model.node.entity.actor.figure.Figure;
import com.fury.core.model.node.entity.actor.figure.Projectile;
import com.fury.core.model.node.entity.actor.figure.mob.Mob;
import com.fury.game.entity.combat.CombatScript;
import com.fury.game.node.entity.actor.figure.ProjectileManager;
import com.fury.game.system.files.loaders.npc.MobCombatDefinitions;
import com.fury.game.world.update.flag.block.Animation;
import com.fury.util.Utils;

public class GeneralGraardorCombat extends CombatScript {
    @Override
    public Object[] getKeys() {
        return new Object[] { 6260 };
    }

    @Override
    public int attack(Mob mob, Figure target) {
        final MobCombatDefinitions defs = mob.getCombatDefinition();
        if (Utils.getRandom(4) == 0) {
            switch (Utils.getRandom(10)) {
                case 0:
                    mob.forceChat("Death to our enemies!");
//                    npc.playSound(3219, 2);
                    break;
                case 1:
                    mob.forceChat("Brargh!");
//                    npc.playSound(3209, 2);
                    break;
                case 2:
                    mob.forceChat("Break their bones!");
                    break;
                case 3:
                    mob.forceChat("For the glory of Bandos!");
                    break;
                case 4:
                    mob.forceChat("Split their skulls!");
//                    npc.playSound(3229, 2);
                    break;
                case 5:
                    mob.forceChat("We feast on the bones of our enemies tonight!");
//                    npc.playSound(3206, 2);
                    break;
                case 6:
                    mob.forceChat("CHAAARGE!");
//                    npc.playSound(3220, 2);
                    break;
                case 7:
                    mob.forceChat("Crush them underfoot!");
//                    npc.playSound(3224, 2);
                    break;
                case 8:
                    mob.forceChat("All glory to Bandos!");
//                    npc.playSound(3205, 2);
                    break;
                case 9:
                    mob.forceChat("GRAAAAAAAAAR!");
//                    npc.playSound(3207, 2);
                    break;
                case 10:
                    mob.forceChat("FOR THE GLORY OF THE BIG HIGH WAR GOD!");
                    break;
            }
        }
        if (Utils.getRandom(2) == 0) { // range magical attack
            mob.animate(7063);
            for (Figure t : mob.getPossibleTargets()) {
                delayHit(mob, 1, t, getRangeHit(mob, getRandomMaxHit(mob, 355, MobCombatDefinitions.RANGE, t)));
                ProjectileManager.send(new Projectile(mob, t, 1200, 41, 16, 41, 35, 16, 0));
            }
        } else { // melee attack
            mob.perform(new Animation(defs.getAttackAnim()));
            delayHit(mob, 0, target, getMeleeHit(mob, getRandomMaxHit(mob, defs.getMaxHit(), MobCombatDefinitions.MELEE, target)));
        }
        return defs.getAttackDelay();
    }
}
