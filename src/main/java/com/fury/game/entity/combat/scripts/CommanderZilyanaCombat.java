package com.fury.game.entity.combat.scripts;

import com.fury.core.model.node.entity.actor.figure.Figure;
import com.fury.core.model.node.entity.actor.figure.mob.Mob;
import com.fury.game.entity.combat.CombatScript;
import com.fury.game.system.files.loaders.npc.MobCombatDefinitions;
import com.fury.game.world.update.flag.block.Animation;
import com.fury.util.Utils;

public class CommanderZilyanaCombat extends CombatScript {
    @Override
    public Object[] getKeys() {
        return new Object[] { 6247 };
    }

    @Override
    public int attack(Mob mob, Figure target) {
        final MobCombatDefinitions defs = mob.getCombatDefinition();
        if (Utils.getRandom(4) == 0) {
            switch (Utils.getRandom(9)) {
                case 0:
                    mob.forceChat("Death to the enemies of the light!");
//                    npc.playSound(3247, 2);
                    break;
                case 1:
                    mob.forceChat("Slay the evil ones!");
//                    npc.playSound(3242, 2);
                    break;
                case 2:
                    mob.forceChat("Saradomin lend me strength!");
//                    npc.playSound(3263, 2);
                    break;
                case 3:
                    mob.forceChat("By the power of Saradomin!");
//                    npc.playSound(3262, 2);
                    break;
                case 4:
                    mob.forceChat("May Saradomin be my sword.");
//                    npc.playSound(3251, 2);
                    break;
                case 5:
                    mob.forceChat("Good will always triumph!");
//                    npc.playSound(3260, 2);
                    break;
                case 6:
                    mob.forceChat("Forward! Our allies are with us!");
//                    npc.playSound(3245, 2);
                    break;
                case 7:
                    mob.forceChat("Saradomin is with us!");
//                    npc.playSound(3266, 2);
                    break;
                case 8:
                    mob.forceChat("In the name of Saradomin!");
//                    npc.playSound(3250, 2);
                    break;
                case 9:
                    mob.forceChat("Attack! Find the Godsword!");
//                    npc.playSound(3258, 2);
                    break;
            }
        }
        if (Utils.getRandom(1) == 0) { // mage magical attack
            mob.animate(6967);
            for (Figure t : mob.getPossibleTargets()) {
                if (!t.isWithinDistance(mob, 3))
                    continue;
                int damage = getRandomMaxHit(mob, defs.getMaxHit(), MobCombatDefinitions.MAGE, t);
                if (damage > 0) {
                    delayHit(mob, 1, t, getMagicHit(mob, damage));
                    t.graphic(1194);
                }
            }
        } else { // melee attack
            mob.perform(new Animation(defs.getAttackAnim()));
            delayHit(mob, 0, target, getMeleeHit(mob, getRandomMaxHit(mob, defs.getMaxHit(), MobCombatDefinitions.MELEE, target)));
        }
        return defs.getAttackDelay();
    }
}
