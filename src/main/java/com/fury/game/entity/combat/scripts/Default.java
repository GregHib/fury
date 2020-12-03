package com.fury.game.entity.combat.scripts;

import com.fury.core.model.node.entity.actor.figure.Figure;
import com.fury.core.model.node.entity.actor.figure.Projectile;
import com.fury.core.model.node.entity.actor.figure.mob.Mob;
import com.fury.game.entity.combat.CombatScript;
import com.fury.game.node.entity.actor.figure.ProjectileManager;
import com.fury.game.system.files.loaders.npc.MobCombatDefinitions;
import com.fury.game.world.update.flag.block.Animation;
import com.fury.game.world.update.flag.block.graphic.Graphic;

public class Default extends CombatScript {
    @Override
    public Object[] getKeys() {
        return new Object[] { "Default" };
    }

    @Override
    public int attack(Mob mob, Figure target) {
        MobCombatDefinitions defs = mob.getCombatDefinition();
        int attackStyle = defs.getAttackStyle();
        if (attackStyle == MobCombatDefinitions.MELEE) {
            delayHit(mob, 0, target, getMeleeHit(mob, getRandomMaxHit(mob, defs.getMaxHit(), attackStyle, target)));
        } else {
            int damage = getRandomMaxHit(mob, defs.getMaxHit(), attackStyle, target);
            delayHit(mob, 2, target, attackStyle == MobCombatDefinitions.RANGE ? getRangeHit(mob, damage) : getMagicHit(mob, damage));
            if (defs.getAttackProjectile() != -1 || (defs.getAttackProjectile() == -1 && attackStyle == MobCombatDefinitions.MAGE))
                ProjectileManager.send(new Projectile(mob, target, defs.getAttackProjectile() != -1 ?  defs.getAttackProjectile() : 2730, 40, 39, 20, 30, 16, 0));
        }
        if (defs.getAttackGraphic() != -1)
            mob.perform(new Graphic(defs.getAttackGraphic()));
        mob.perform(new Animation(defs.getAttackAnim()));
        return defs.getAttackDelay();
    }
}
