package com.fury.game.entity.combat.scripts;

import com.fury.core.model.node.entity.actor.figure.Figure;
import com.fury.core.model.node.entity.actor.figure.Projectile;
import com.fury.core.model.node.entity.actor.figure.mob.Mob;
import com.fury.game.entity.combat.CombatScript;
import com.fury.game.node.entity.actor.figure.ProjectileManager;
import com.fury.game.npc.bosses.TormentedDemon;
import com.fury.game.system.files.loaders.npc.MobCombatDefinitions;
import com.fury.game.world.update.flag.block.graphic.Graphic;
import com.fury.util.Utils;

public class TormentedDemonCombat extends CombatScript {

    @Override
    public Object[] getKeys() {
        return new Object[] { "Tormented demon" };
    }

    @Override
    public int attack(Mob mob, Figure target) {
        final MobCombatDefinitions defs = mob.getCombatDefinition();
        TormentedDemon torm = (TormentedDemon) mob;
        int hit = 0;
        int attackStyle = torm.getFixedAmount() == 0 ? Utils.getRandom(2) : torm.getFixedCombatType();
        if (torm.getFixedAmount() == 0)
            torm.setFixedCombatType(attackStyle);
        switch (attackStyle) {
            case 0:
                if (mob.isWithinDistance(target, 3)) {
                    hit = getRandomMaxHit(mob, 189, MobCombatDefinitions.MELEE, target);
                    mob.animate(10922);
                    mob.graphic(1886);
                    delayHit(mob, 1, target, getMeleeHit(mob, hit));
                }
                return defs.getAttackDelay();
            case 1:
                hit = getRandomMaxHit(mob, 270, MobCombatDefinitions.MAGE, target);
                mob.animate(10918);
                mob.perform(new Graphic(1883, 0, 96 << 16));
                ProjectileManager.send(new Projectile(mob, target, 1884, 34, 16, 30, 35, 16, 0));
                delayHit(mob, 1, target, getMagicHit(mob, hit));
                break;
            case 2:
                hit = getRandomMaxHit(mob, 270, MobCombatDefinitions.RANGE, target);
                mob.animate(10919);
                mob.graphic(1888);
                ProjectileManager.send(new Projectile(mob, target, 1887, 34, 16, 30, 35, 16, 0));
                delayHit(mob, 1, target, getRangeHit(mob, hit));
                break;
        }
        torm.setFixedAmount(torm.getFixedAmount() + 1);
        return defs.getAttackDelay();
    }
}