package com.fury.game.entity.combat.scripts.fightcaves;

import com.fury.core.model.node.entity.actor.figure.Figure;
import com.fury.core.model.node.entity.actor.figure.Projectile;
import com.fury.core.model.node.entity.actor.figure.mob.Mob;
import com.fury.game.entity.combat.CombatScript;
import com.fury.game.node.entity.actor.figure.ProjectileManager;
import com.fury.game.system.files.loaders.npc.MobCombatDefinitions;
import com.fury.game.world.update.flag.block.Animation;
import com.fury.game.world.update.flag.block.graphic.Graphic;
import com.fury.util.Misc;
import com.fury.util.Utils;

public class Ket_ZekCombat extends CombatScript {

    @Override
    public Object[] getKeys() {
        return new Object[] { 2743, 2744, 15207 };
    }

    @Override
    public int attack(final Mob mob, final Figure target) {
        MobCombatDefinitions defs = mob.getCombatDefinition();
        boolean isDistanced = !Utils.isOnRange(mob, target, 0);
        int style = Misc.random(isDistanced ? 1 : 0, 2);
        switch (style) {
            case 0://MELEE
                mob.perform(new Animation(defs.getAttackAnim()));
                delayHit(mob, 0, target, getMeleeHit(mob, getRandomMaxHit(mob, MobCombatDefinitions.MELEE, target)));
                break;
            case 1://MAGIC
                mob.animate(9266);
                mob.perform(new Graphic(defs.getAttackGraphic()));
                Projectile projectile = new Projectile(mob, target, defs.getAttackProjectile(), 110, 16, 35, 25, 0, 0);
                ProjectileManager.send(projectile);
                delayHit(mob, projectile.getTickDelay(), target, getMagicHit(mob, getRandomMaxHit(mob, MobCombatDefinitions.MAGE, target)));
                target.perform(new Graphic(1624, projectile.getTickDelay(), 96 << 16));
                break;
        }
        return mob.getCombat().getAttackSpeed();
    }
}