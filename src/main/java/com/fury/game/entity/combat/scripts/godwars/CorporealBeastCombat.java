package com.fury.game.entity.combat.scripts.godwars;

import com.fury.core.model.node.entity.actor.figure.Figure;
import com.fury.core.model.node.entity.actor.figure.Projectile;
import com.fury.core.model.node.entity.actor.figure.player.Player;
import com.fury.game.content.skill.Skill;
import com.fury.game.entity.character.combat.CombatIcon;
import com.fury.game.entity.character.combat.Hit;
import com.fury.core.model.node.entity.actor.figure.mob.Mob;
import com.fury.game.entity.combat.CombatScript;
import com.fury.game.node.entity.actor.figure.ProjectileManager;
import com.fury.game.npc.bosses.corp.DarkEnergyCore;
import com.fury.game.system.files.loaders.npc.MobCombatDefinitions;
import com.fury.game.world.GameWorld;
import com.fury.game.world.World;
import com.fury.game.world.map.Position;
import com.fury.game.world.update.flag.block.HitMask;
import com.fury.game.world.update.flag.block.graphic.Graphic;
import com.fury.util.Misc;

import java.util.ArrayList;

public class CorporealBeastCombat extends CombatScript {

    @Override
    public Object[] getKeys() {
        return new Object[]{8133};
    }

    @Override
    public int attack(final Mob mob, final Figure target) {
        boolean isDistanced = !Misc.isOnRange(mob.getX(), mob.getY(), mob.getSizeX(), mob.getSizeY(), target.getX(), target.getY(), target.getSizeX(), target.getSizeY(), 0);
        final ArrayList<Figure> targets = mob.getPossibleTargets(true, true);
        boolean stomp = false;
        for (Figure t : targets) {
            if (t instanceof DarkEnergyCore)
                continue;
            if (t.isFamiliar()) {
                t.getHealth().heal(mob.getHealth().getHitpoints());
                t.sendDeath(mob);
                continue;
            }
            if (Misc.colides(t.getX(), t.getY(), t.getSizeX(), t.getSizeY(), mob.getX(), mob.getY(), mob.getSizeX(), mob.getSizeY())) {
                stomp = true;
                delayHit(mob, 0, t, getRegularHit(mob, getRandomMaxHit(mob, Misc.random(580) + 200, MobCombatDefinitions.MELEE, target)));
            }
        }
        if (stomp) {
            mob.animate(10496);
            mob.perform(new Graphic(1834));
            return mob.getCombat().getAttackSpeed();
        }
        int style = Misc.random(isDistanced ? 3 : 5);
        if (style == 3 || style == 4) {
            mob.animate(10058);
            delayHit(mob, 0, target, getMeleeHit(mob, getRandomMaxHit(mob, MobCombatDefinitions.MELEE, target)));
            return mob.getCombat().getAttackSpeed();
        } else {
            mob.animate(10410);
            final Position tile = target.copyPosition();
            Projectile projectile = new Projectile(mob, style == 1 ? tile : target.copyPosition(), 1823 + style, 41, style == 1 ? 0 : 16, 10, 2, 16, 0);
            ProjectileManager.send(projectile);
            int projectileCycles = projectile.getTickDelay() - 1, damage = getRandomMaxHit(mob, style == 2 ? 500 : mob.getCombatDefinition().getMaxHit(), MobCombatDefinitions.MAGE, target);
            if (style == 0 && damage > 0 && target.isPlayer()) {
                final Player player = (Player) target;
                int skillSelect = Misc.random(3);
                final Skill skill = skillSelect == 0 ? Skill.MAGIC : (skillSelect == 1 ? Skill.SUMMONING : Skill.PRAYER);
                if (player.getSkills().getLevel(skill) == 0) {
                    damage += 150 + Misc.random(100); //extra dmg as cant drain more
                } else {
                    GameWorld.schedule(projectileCycles, () -> {
                        if (skill == Skill.PRAYER)
                            player.getSkills().drain(Skill.PRAYER, 10 + Misc.random(41));
                        else {
                            player.getSkills().drain(skill, 1 + Misc.random(5));
                        }
                        player.message("Your " + skill.getFormatName() + " has been slightly drained!", true);
                    });
                }

            } else if (style == 1) {
                for (int i = 0; i < 6; i++) {
                    final Position newTile = new Position(tile, 3);
                    if (!World.isFloorFree(newTile.getX(), newTile.getY(), newTile.getZ()))
                        continue;
                    final Projectile subProjectile = new Projectile(tile, newTile, 1824, 0, 0, projectile.getTickDelay() + 5, 1, 30, 0);
                    ProjectileManager.send(subProjectile);
                    GameWorld.schedule(subProjectile.getTickDelay(), () -> {
                        for (Figure t : targets) {
                            if (t instanceof DarkEnergyCore)
                                continue;
                            if (t.getX() >= newTile.getX() - 1 && t.getX() <= newTile.getX() + 1 && t.getY() >= newTile.getY() - 1 && t.getY() <= newTile.getY() + 1)
                                t.getCombat().applyHit(new Hit(mob, Misc.random(150, 400), HitMask.RED, CombatIcon.MAGIC));
                        }
                    });
                    Graphic.sendGlobal(mob, new Graphic(1806, subProjectile.getTickDelay(), 0), newTile);
                }
                return mob.getCombat().getAttackSpeed();
            }
            delayHit(mob, projectileCycles, target, getMagicHit(mob, damage));
        }
        return mob.getCombat().getAttackSpeed();
    }
}