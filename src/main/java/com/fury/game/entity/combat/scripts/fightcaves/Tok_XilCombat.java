package com.fury.game.entity.combat.scripts.fightcaves;

import com.fury.core.model.node.entity.actor.figure.Figure;
import com.fury.core.model.node.entity.actor.figure.Projectile;
import com.fury.core.model.node.entity.actor.figure.mob.Mob;
import com.fury.game.entity.combat.CombatScript;
import com.fury.game.node.entity.actor.figure.ProjectileManager;
import com.fury.game.system.files.loaders.npc.MobCombatDefinitions;
import com.fury.game.world.update.flag.block.Animation;
import com.fury.util.Misc;
import com.fury.util.Utils;

public class Tok_XilCombat extends CombatScript {

    @Override
    public Object[] getKeys() {
        return new Object[] { 2739, 2740, 15205 };
    }

    @Override
    public int attack(Mob mob, Figure target) {
        final MobCombatDefinitions defs = mob.getCombatDefinition();
        int style = Misc.random(!Utils.isOnRange(mob, target, 0) ? 1 : 0, 2);
        switch (style) {
            case 0://MELEE
                mob.perform(new Animation(defs.getAttackAnim()));
                delayHit(mob, 0, target, getMeleeHit(mob, getRandomMaxHit(mob, MobCombatDefinitions.MELEE, target)));
                break;
            case 1://RANGED
                mob.perform(new Animation(defs.getAttackAnim()));
                Projectile projectile = new Projectile(mob, target, defs.getAttackProjectile(), 34, 16, 35, 2, 10, 0);
                ProjectileManager.send(projectile);
                delayHit(mob, projectile.getTickDelay(), target, getRangeHit(mob, getRandomMaxHit(mob, MobCombatDefinitions.RANGE, target)));
                break;
        }
        return mob.getCombat().getAttackSpeed();
    }
}
