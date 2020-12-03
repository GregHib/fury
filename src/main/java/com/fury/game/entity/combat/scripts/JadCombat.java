package com.fury.game.entity.combat.scripts;

import com.fury.core.model.node.entity.actor.figure.Figure;
import com.fury.core.model.node.entity.actor.figure.Projectile;
import com.fury.core.model.node.entity.actor.figure.mob.Mob;
import com.fury.game.entity.combat.CombatScript;
import com.fury.game.node.entity.actor.figure.ProjectileManager;
import com.fury.game.system.files.loaders.npc.MobCombatDefinitions;
import com.fury.game.world.update.flag.block.Animation;
import com.fury.game.world.update.flag.block.graphic.Graphic;
import com.fury.util.Utils;

public class JadCombat extends CombatScript {

    @Override
    public Object[] getKeys() {

        return new Object[] { 2745, 15208 };
    }

    @Override
    public int attack(final Mob mob, final Figure target) {
        int jadAttack = Utils.getRandom(2);
        int jadHit = Utils.getRandom(970);
        MobCombatDefinitions defs = mob.getCombatDefinition();

        if (target.isWithinDistance(mob, 1)) {
            mob.perform(new Animation(defs.getAttackAnim()));
            delayHit(mob, 2, target, getMeleeHit(mob, jadHit));
        } else {
            switch (jadAttack) {
                case 1://RANGE
                    mob.perform(new Animation(mob.getId() == 2745 ? 9276 : 16202));
                    mob.perform(new Graphic(mob.getId() == 2745 ? 1625 : 2994));
                    delayHit(mob, (int) ProjectileManager.getDelay(mob, target, (int) 3.5, 4) + ProjectileManager.getProjectileDelay(mob, target), target, getRangeHit(mob, jadHit));
                    target.perform(new Graphic(451, 6));
                    break;
                case 2://MAGIC
                    mob.perform(new Animation(mob.getId() == 2745 ? 9300 : 16195));
                    mob.perform(new Graphic(mob.getId() == 2745 ? 1626 : 2995));
                    Projectile projectile = new Projectile(mob, target, 1627, 41, 16, 41, 35, 16, 0);
                    ProjectileManager.send(projectile);
                    delayHit(mob, projectile.getTickDelay(), target, getMagicHit(mob, jadHit));
                    break;
            }
        }
        return defs.getAttackDelay();
    }

}
