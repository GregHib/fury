package com.fury.game.entity.combat.scripts.slayer;

import com.fury.core.model.node.entity.actor.figure.Figure;
import com.fury.core.model.node.entity.actor.figure.Projectile;
import com.fury.core.task.TickableTask;
import com.fury.game.entity.character.combat.CombatIcon;
import com.fury.game.entity.character.combat.Hit;
import com.fury.core.model.node.entity.actor.figure.mob.Mob;
import com.fury.game.entity.combat.CombatScript;
import com.fury.game.node.entity.actor.figure.ProjectileManager;
import com.fury.game.system.files.loaders.npc.MobCombatDefinitions;
import com.fury.game.world.GameWorld;
import com.fury.game.world.map.Position;
import com.fury.game.world.update.flag.block.Animation;
import com.fury.game.world.update.flag.block.HitMask;
import com.fury.game.world.update.flag.block.graphic.Graphic;
import com.fury.util.Misc;
import com.fury.util.RandomUtils;
import com.fury.util.Utils;

public class StrykewyrmCombat extends CombatScript {

    @Override
    public Object[] getKeys() {
        return new Object[] { 9463, 9465, 9467 };
    }

    @Override
    public int attack(final Mob mob, final Figure target) {
        final MobCombatDefinitions defs = mob.getCombatDefinition();
        if (RandomUtils.success(0.2)) {// Digging
            final Position tile = target.copyPosition();
            tile.add(-1, -1, 0);
            mob.animate(12796);
            mob.setCantInteract(true);

            GameWorld.schedule(new TickableTask() {
                @Override
                public void tick() {
                    if (getTick() == 0) {
                        mob.setTransformation(mob.getId() - 1);
                        mob.setForceWalk(tile);
                    } else if (getTick() == -1) {
                        mob.setCantInteract(false);
                        mob.setTarget(target);
                        stop();
                    } else if (!mob.isForceWalking()) {
                        mob.setTransformation(mob.getId() + 1);
                        mob.animate(12795);
                        if (Utils.colides(target, mob)) {
                            delayHit(mob, 0, target, new Hit(mob, Misc.random(50, 200), HitMask.RED, CombatIcon.NONE));
                            if (mob.getId() == 9467)
                                target.getEffects().makePoisoned(88);
                            else if (mob.getId() == 9465) {
                                delayHit(mob, 0, target, new Hit(mob, Misc.random(50, 200), HitMask.RED, CombatIcon.NONE));
                                target.perform(new Graphic(2311));
                            }
                        }
                        setTick(-2);
                    }
                }
            });
        } else if (RandomUtils.success(0.33) || !Utils.isOnRange(mob, target, 0)) {//Magical attack
            mob.animate(12794);
            final Hit hit = getMagicHit(mob, getRandomMaxHit(mob, MobCombatDefinitions.MAGE, target));
            Projectile projectile = new Projectile(mob, target, defs.getAttackProjectile(), 41, 16, 30, 2, 16, 0);
            ProjectileManager.send(projectile);
            delayHit(mob, projectile.getTickDelay(), target, hit);
            if (mob.getId() == 9463) {
                if (Misc.random(5) == 0) {
                    target.getCombat().addFreezeDelay(5000, true);
                    target.perform(new Graphic(369, projectile.getTickDelay(), 0));
                }// else
//                    target.perform(new Graphic(2315, projectile.getEndTime(), 0));
            }
            else if (mob.getId() == 9467) {
                target.perform(new Graphic(2313, projectile.getTickDelay(), 0));
                if (RandomUtils.success(0.33))
                    target.getEffects().makePoisoned(88);
            }
        }
        else {//Melee Attack
            mob.perform(new Animation(defs.getAttackAnim()));
            if (mob.getId() == 9467 && RandomUtils.success(0.2)) {
                target.perform(new Graphic(2309));
                target.getEffects().makePoisoned(44);
            }
            delayHit(mob, 0, target, getMeleeHit(mob, getRandomMaxHit(mob, MobCombatDefinitions.MELEE, target)));
        }
        return defs.getAttackDelay();
    }
}