package com.fury.game.entity.combat.scripts;

import com.fury.core.model.node.entity.actor.figure.Figure;
import com.fury.core.model.node.entity.actor.figure.Projectile;
import com.fury.core.model.node.entity.actor.figure.mob.Mob;
import com.fury.game.entity.combat.CombatScript;
import com.fury.game.node.entity.actor.figure.ProjectileManager;
import com.fury.game.system.files.loaders.npc.MobCombatDefinitions;
import com.fury.game.world.update.flag.block.Animation;
import com.fury.util.Utils;

public class RockGolemCombat extends CombatScript {
    @Override
    public Object[] getKeys() {
        return new Object[] {8648};
    }

    @Override
    public int attack(Mob mob, Figure target) {
        MobCombatDefinitions definition = mob.getCombatDefinition();
        mob.perform(new Animation(definition.getAttackAnim()));
        if (Utils.isOnRange(target, mob, 0))
            delayHit(mob, 0, target, getMeleeHit(mob, getRandomMaxHit(mob, mob.getCombatDefinition().getAttackStyle(), target)));
        else {
            Projectile projectile = new Projectile(mob, target, 570, 41, 16, 35, 3, 10, 0);
            ProjectileManager.send(projectile);
            delayHit(mob, projectile.getTickDelay(), target, getRangeHit(mob, getRandomMaxHit(mob, MobCombatDefinitions.RANGE, target)));
        }
        return definition.getAttackDelay();
    }
}
