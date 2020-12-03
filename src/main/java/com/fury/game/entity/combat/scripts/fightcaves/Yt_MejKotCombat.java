package com.fury.game.entity.combat.scripts.fightcaves;

import com.fury.core.model.node.entity.actor.figure.Figure;
import com.fury.core.model.node.entity.actor.figure.mob.Mob;
import com.fury.game.entity.combat.CombatScript;
import com.fury.game.system.files.loaders.npc.MobCombatDefinitions;
import com.fury.game.world.update.flag.block.Animation;
import com.fury.game.world.update.flag.block.graphic.Graphic;

public class Yt_MejKotCombat extends CombatScript {

    @Override
    public Object[] getKeys() {
        return new Object[]{"Yt-MejKot"};
    }

    @Override
    public int attack(Mob mob, Figure target) {
        final MobCombatDefinitions defs = mob.getCombatDefinition();
        mob.perform(new Animation(defs.getAttackAnim()));
        delayHit(mob, 0, target, getMeleeHit(mob, getRandomMaxHit(mob, defs.getAttackStyle(), target)));
        if (mob.getHealth().getHitpoints() < mob.getMaxConstitution() / 2) {
            if (mob.getTemporaryAttributes().remove("Heal") != null) {
                mob.perform(new Graphic(444, 0, 100));
                for (Mob n : mob.getRegion().getNpcs(mob.getZ())) {
                    if (n == null || n.isDead() || n.getFinished())
                        continue;
                    n.getHealth().heal(100);
                }
            } else
                mob.getTemporaryAttributes().put("Heal", Boolean.TRUE);
        }
        return mob.getCombat().getAttackSpeed();
    }
}