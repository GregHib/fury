package com.fury.game.entity.combat.scripts.dragons;

import com.fury.core.model.node.entity.actor.figure.Figure;
import com.fury.core.model.node.entity.actor.figure.Projectile;
import com.fury.game.entity.character.combat.CombatConstants;
import com.fury.core.model.node.entity.actor.figure.mob.Mob;
import com.fury.game.entity.combat.CombatScript;
import com.fury.game.node.entity.actor.figure.ProjectileManager;
import com.fury.game.system.files.loaders.npc.MobCombatDefinitions;
import com.fury.game.world.update.flag.block.Animation;
import com.fury.game.world.update.flag.block.graphic.Graphic;
import com.fury.util.Misc;

public class MithrilDragonCombat extends CombatScript {
    @Override
    public Object[] getKeys() {
        return new Object[]
                { "Mithril dragon" };
    }

    public int attack(Mob mob, Figure target) {
        MobCombatDefinitions defs = mob.getCombatDefinition();
        switch (Misc.random(Misc.isOnRange(mob.getX(), mob.getY(), mob.getSizeX(), mob.getSizeY(), target.getX(), target.getY(), target.getSizeX(), target.getSizeY(), 0) ? 4 : 3)) {
            case 3: //melee
                mob.perform(new Animation(defs.getAttackAnim()));
                delayHit(mob, 0, target, getMeleeHit(mob, getRandomMaxHit(mob, MobCombatDefinitions.MELEE, target)));
                break;
            case 2: //magic
                mob.animate(13160);
                Projectile projectile = new Projectile(mob, target, 2705, 28, 16, 35, 2, 16, 0);
                ProjectileManager.send(projectile);
                delayHit(mob, projectile.getTickDelay(), target, getMagicHit(mob, getRandomMaxHit(mob, MobCombatDefinitions.MAGE, target)));
                break;
            case 1: //range
                mob.animate(13160);
                projectile = new Projectile(mob, target, 16, 28, 16, 35, 2, 16, 0);
                ProjectileManager.send(projectile);
                delayHit(mob, projectile.getTickDelay(), target, getRangeHit(mob, getRandomMaxHit(mob, MobCombatDefinitions.RANGE, target)));
                break;
            case 0: //dragonfire
                int damage = Misc.random(650);
                damage *= CombatConstants.getDragonFireMultiplier(target, false, true);
                mob.animate(13164);
                mob.perform(new Graphic(2465, 0, 1));
                delayHit(mob, 1, target, getRegularHit(mob, damage));
                break;
        }

        return mob.getCombat().getAttackSpeed();

    }
}
