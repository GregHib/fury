package com.fury.game.entity.combat.scripts.godwars;

import com.fury.cache.Revision;
import com.fury.cache.def.Loader;
import com.fury.core.model.node.entity.actor.figure.Figure;
import com.fury.core.model.node.entity.actor.figure.Projectile;
import com.fury.core.model.node.entity.actor.figure.player.Player;
import com.fury.core.task.Task;
import com.fury.game.content.controller.impl.ZarosGodwars;
import com.fury.game.content.skill.Skill;
import com.fury.game.entity.character.combat.CombatIcon;
import com.fury.game.entity.character.combat.Hit;
import com.fury.core.model.node.entity.actor.figure.mob.Mob;
import com.fury.game.entity.character.player.actions.ForceMovement;
import com.fury.game.entity.combat.CombatScript;
import com.fury.game.entity.object.GameObject;
import com.fury.game.node.entity.actor.figure.ProjectileManager;
import com.fury.game.node.entity.actor.object.TempObjectManager;
import com.fury.game.npc.bosses.nex.Nex;
import com.fury.game.system.files.loaders.npc.MobCombatDefinitions;
import com.fury.game.world.GameWorld;
import com.fury.game.world.World;
import com.fury.game.world.map.Position;
import com.fury.game.world.update.flag.block.Animation;
import com.fury.game.world.update.flag.block.HitMask;
import com.fury.game.world.update.flag.block.graphic.Graphic;
import com.fury.util.Utils;

import java.util.ArrayList;
import java.util.HashMap;

public class NexCombat extends CombatScript {

    @Override
    public Object[] getKeys() {
        return new Object[]{"Nex"};
    }

    public Position[] NO_ESCAPE_TELEPORTS = {new Position(2924, 5213), // north
            new Position(2934, 5202), // east,
            new Position(2924, 5192), // south
            new Position(2913, 5202),}; // west

    @Override
    public int attack(final Mob mob, final Figure target) {
        final MobCombatDefinitions defs = mob.getCombatDefinition();
        final Nex nex = (Nex) mob;
        int sizeX = mob.getSizeX();
        int sizeY = mob.getSizeY();
        // attacks close only
        if (nex.isFollowTarget()) {
            int distanceX = target.getX() - mob.getX();
            int distanceY = target.getY() - mob.getY();
            if (distanceX > sizeX || distanceX < -1 || distanceY > sizeY
                    || distanceY < -1)
                return 0;
            nex.setFollowTarget(Utils.getRandom(1) == 0);
            // first stage close attacks
            if (nex.getAttacksStage() == 0) {
                // virus 1/3 probability every 1min
                if (nex.getLastVirus() < Utils.currentTimeMillis() && Utils.getRandom(2) == 0) {
                    nex.setLastVirus(Utils.currentTimeMillis() + 60000);
                    mob.forceChat("Let the virus flow through you.");
//                    nex.playSound(3296, 2);
                    mob.animate(6987);
                    nex.sendVirusAttack(new ArrayList<Figure>(),
                            mob.getPossibleTargets(), target);
                    return defs.getAttackDelay();
                }
            }
            // no escape, 1/10 probability doing it
            if (Utils.getRandom(nex.getStage() == 4 ? 5 : 10) == 0) {
                mob.forceChat("There is...");
//                nex.playSound(3294, 2);
                mob.setCantInteract(true);
                mob.getMobCombat().removeTarget();
                final int idx = Utils.random(NO_ESCAPE_TELEPORTS.length);
                final Position dir = NO_ESCAPE_TELEPORTS[idx];
                final Position center = new Position(2924, 5202);
                GameWorld.schedule(new Task(true) {
                    private int count;

                    @Override
                    public void run() {

                        if (count == 0) {
                            mob.animate(6321);
                            mob.perform(new Graphic(1216));
                        } else if (count == 1) {
                            nex.moveTo(dir);
                            nex.forceChat("NO ESCAPE!");
//                            nex.playSound(3292, 2);
                            nex.setForceMovement(new ForceMovement(dir, 1, center, 3, idx == 3 ? 1 : idx == 2 ? 0 : idx == 1 ? 3 : 2));
                            for (Figure entity : nex.calculatePossibleTargets(
                                    center, dir, idx == 0 || idx == 2)) {
                                if (entity.isPlayer()) {
                                    Player player = (Player) entity;
                                    /*player.getCutscenesManager().play(
                                            new NexCutScene(dir, idx));*/
                                    player.getCombat().applyHit(new Hit(mob, Utils.getRandom(nex.getStage() == 4 ? 800 : 650), HitMask.RED, CombatIcon.NONE));
                                    player.animate(10070);
                                    player.setForceMovement(new ForceMovement(player.copyPosition(), 1, idx == 3 ? 3 : idx == 2 ? 2 : idx == 1 ? 1 : 0));
                                }
                            }
                        } else if (count == 3) {
                            nex.moveTo(center);
                        } else if (count == 4) {
                            nex.setTarget(target);
                            mob.setCantInteract(false);
                            stop();
                        }
                        count++;
                    }
                });
                return defs.getAttackDelay();
            }
            // normal melee attack
            int damage = getRandomMaxHit(mob, defs.getMaxHit(),
                    MobCombatDefinitions.MELEE, target);
            delayHit(mob, 0, target, getMeleeHit(mob, damage));
            mob.perform(new Animation(defs.getAttackAnim()));
            return defs.getAttackDelay();
            // far attacks
        } else {
            nex.setFollowTarget(Utils.getRandom(1) == 0);
            // drag a player to center
            if (Utils.getRandom(15) == 0) {
                int distance = 0;
                Figure settedTarget = null;
                for (Figure t : mob.getPossibleTargets()) {
                    if (t.isPlayer()) {
                        int thisDistance = Utils.getDistance(t.getX(), t.getY(), mob.getX(), mob.getY());
                        if (settedTarget == null || thisDistance > distance) {
                            distance = thisDistance;
                            settedTarget = t;
                        }
                    }
                }
                if (settedTarget != null && distance > 10) {
                    final Player player = (Player) settedTarget;
                    player.addStopDelay(3);
                    player.animate(14386);
                    player.setForceMovement(new ForceMovement(nex.copyPosition(), 2, Utils.getMoveDirection(mob.getCoordFaceX(player.getSizeX()) - player.getX(), mob.getCoordFaceY(player.getSizeY()) - player.getY())));
                    mob.animate(6986);
                    mob.setTarget(player);
                    player.animate(-1);
                    GameWorld.schedule(1, () -> {
                        player.moveTo(nex.copyPosition());
                        player.message("You've been injured and you can't use protective curses!");
                        player.setPrayerDelay(Utils.getRandom(20000) + 5);// random 20 seconds
                        player.message("You're stunned.");
                    });
                    return defs.getAttackDelay();
                }
            }
            // first stage close attacks
            if (nex.getAttacksStage() == 0) {
                mob.animate(6987);
                for (Figure t : mob.getPossibleTargets()) {
                    ProjectileManager.send(new Projectile(mob, t, 471, 41, 16, 41, 35, 16, 0));
                    int damage = getRandomMaxHit(mob, defs.getMaxHit(), MobCombatDefinitions.MAGE, t);
                    delayHit(mob, 1, t, getMagicHit(mob, damage));
                    if (damage > 0 && Utils.getRandom(5) == 0) // 1/6 probability  poisoning
                        t.getEffects().makePoisoned(80);
                }
                return defs.getAttackDelay();
            } else if (nex.getAttacksStage() == 1) {
                if (!nex.isEmbracedShadow()) {
                    nex.setEmbracedShadow(true);
                    mob.forceChat("Embrace darkness!");
//                    nex.playSound(3322, 2);
                    mob.animate(6355);
                    mob.perform(new Graphic(1217));
                    GameWorld.schedule(new Task(true) {
                        @Override
                        public void run() {
                            if (nex.getAttacksStage() != 1 || nex.getFinished()) {
                                for (Figure entity : nex.getPossibleTargets()) {
                                    if (entity.isPlayer()) {
                                        Player player = (Player) entity;
                                        //Screen splash?
//                                        player.getPacketSender().sendGlobalConfig(1435, 255);
                                    }
                                }
                                stop();
                                return;
                            }
                            if (Utils.getRandom(2) == 0) {
                                for (Figure entity : nex.getPossibleTargets()) {
                                    if (entity.isPlayer()) {
                                        Player player = (Player) entity;
                                        int distance = Utils.getDistance(
                                                player.getX(), player.getY(),
                                                mob.getX(), mob.getY());
                                        if (distance > 30)
                                            distance = 30;
//                                        player.getPacketSender().sendGlobalConfig(1435, (distance * 255 / 30));
                                    }
                                }
                            }
                        }
                    });
                    return defs.getAttackDelay();
                }
                if (!nex.isTrapsSetUp() && Utils.getRandom(3) == 0) {
                    nex.setTrapsSetUp(true);
                    mob.forceChat("Fear the Shadow!");
//                    nex.playSound(3314, 2);
                    mob.animate(6984);
                    mob.perform(new Graphic(1215));
                    ArrayList<Figure> possibleTargets = nex.getPossibleTargets();
                    final HashMap<String, int[]> tiles = new HashMap<>();
                    for (Figure t : possibleTargets) {
                        String key = t.getX() + "_" + t.getY();
                        if (!tiles.containsKey(t.getX() + "_" + t.getY())) {
                            tiles.put(key, new int[]{t.getX(), t.getY()});
                            TempObjectManager.spawnObjectTemporary(new GameObject(57261, t.copyPosition(), 10, 0, Revision.RS2), 2400);
                        }
                    }
                    GameWorld.schedule(3, () -> {
                        ArrayList<Figure> possibleTargets1 = nex.getPossibleTargets();
                        for (int[] tile : tiles.values()) {
                            Graphic.sendGlobal(mob, new Graphic(383), new Position(tile[0], tile[1], 0));
                            for (Figure t : possibleTargets1)
                                if (t.getX() == tile[0] && t.getY() == tile[1])
                                    t.getCombat().applyHit(new Hit(mob, Utils.getRandom(400) + 400, HitMask.RED, CombatIcon.NONE));
                        }
                    });
                    GameWorld.schedule(6, () -> nex.setTrapsSetUp(false));
                    return defs.getAttackDelay();
                }
                mob.animate(6987);
                for (final Figure t : mob.getPossibleTargets()) {
                    int distance = Utils.getDistance(t.getX(), t.getY(), mob.getX(), mob.getY());
                    if (distance <= 10) {
                        int damage = 800 - (distance * 800 / 11);
                        ProjectileManager.send(new Projectile(mob, t, 380, 41, 16, 41, 35, 16, 0));
                        delayHit(mob, 1, t, getRangeHit(mob, getRandomMaxHit(mob, damage, MobCombatDefinitions.RANGE, t)));
                        GameWorld.schedule(1, () -> t.graphic(471));
                    }
                }
                return defs.getAttackDelay();
            } else if (nex.getAttacksStage() == 2) {
                if (Utils.getRandom(4) == 0 && target.isPlayer()) {
                    mob.forceChat("I demand a blood sacrifice!");
//                    nex.playSound(3293, 2);
                    final Player player = (Player) target;
//                    player.getAppearence().setGlowRed(true);
                    player.getPacketSender().sendMessage("Nex has marked you as a sacrifice, RUN!", 0xff0000);
                    final int x = player.getX();
                    final int y = player.getY();
                    GameWorld.schedule(defs.getAttackDelay(), () -> {
//                            player.getAppearence().setGlowRed(false);
                        if (x == player.getX() && y == player.getY()) {
                            player.message("You didn't make it far enough in time - Nex fires a punishing attack!");
                            mob.animate(6987);
                            for (final Figure t : mob.getPossibleTargets()) {
                                ProjectileManager.send(new Projectile(mob, t, 374, 41, 16, 41, 35, 16, 0));
                                final int damage = getRandomMaxHit(mob, 290, MobCombatDefinitions.MAGE, t);
                                delayHit(mob, 1, t, getMagicHit(mob, damage));
                                GameWorld.schedule(1, () -> {
                                    t.perform(new Graphic(376));
                                    nex.getHealth().heal(damage / 4);
                                    if (t.isPlayer()) {
                                        Player p = (Player) t;
                                        p.getSkills().drain(Skill.PRAYER, 0.5);
                                    }
                                });
                            }
                        }
                    });
                    return defs.getAttackDelay() * 2;
                }
                if (nex.getLastSiphon() < Utils.currentTimeMillis() && mob.getHealth().getHitpoints() <= 18000 && Utils.getRandom(2) == 0) {
                    nex.setLastSiphon(Utils.currentTimeMillis() + 30000);
                    nex.killBloodReavers();
                    mob.forceChat("A siphon will solve this!");
//                    nex.playSound(3317, 2);
                    mob.animate(6948);
                    mob.perform(new Graphic(1201));
                    nex.setDoingSiphon(true);
                    int bloodReaverSize = Loader.getNpc(13458, Revision.RS2).getSize();
                    int respawnedBloodReaverCount = 0;
                    int maxMinions = Utils.getRandom(3);
                    if (maxMinions != 0) {
                        int[][] dirs = Utils.getCoordOffsetsNear(bloodReaverSize);
                        for (int dir = 0; dir < dirs[0].length; dir++) {
                            final Position tile = new Position(new Position(target.getX() + dirs[0][dir], target.getY() + dirs[1][dir], target.getZ()));
                            if (World.isTileFree(tile.getX(), tile.getY(), tile.getZ(), bloodReaverSize)) {
                                nex.getBloodReavers()[respawnedBloodReaverCount++] = new Mob(13458, tile, true);
                                if (respawnedBloodReaverCount == maxMinions)
                                    break;
                            }
                        }
                    }
                    GameWorld.schedule(8, () -> nex.setDoingSiphon(false));
                    return defs.getAttackDelay();
                }
                mob.animate(6986);
                ProjectileManager.send(new Projectile(mob, target, 374, 41, 16, 41, 35, 16, 0));
                delayHit(mob, 1, target, getMagicHit(mob, getRandomMaxHit(mob, defs.getMaxHit(), MobCombatDefinitions.MAGE, target)));
                return defs.getAttackDelay();
            } else if (nex.getAttacksStage() == 3) {
                mob.animate(6986);
                for (final Figure t : mob.getPossibleTargets()) {
                    ProjectileManager.send(new Projectile(mob, t, 362, 41, 16, 41, 35, 16, 0));
                    int damage = getRandomMaxHit(mob, defs.getMaxHit(), MobCombatDefinitions.MAGE, t);
                    delayHit(mob, 1, t, getMagicHit(mob, damage));
                    if (damage > 0 && Utils.getRandom(5) == 0) {// 1/6 probability freezing
                        GameWorld.schedule(2, () -> {
                            t.getCombat().addFreezeDelay(18000);
                            t.perform(new Graphic(369));
                        });
                    }
                }
                return defs.getAttackDelay();
            } else if (nex.getAttacksStage() == 4) {
                mob.animate(6987);
                for (Figure t : mob.getPossibleTargets()) {
                    ProjectileManager.send(new Projectile(mob, t, 471, 41, 16, 41, 35, 16, 0));
                    int damage = getRandomMaxHit(mob, defs.getMaxHit(), MobCombatDefinitions.MAGE, t);
                    delayHit(mob, 1, t, getMagicHit(mob, damage));
                }
                return defs.getAttackDelay();
            }
        }
        return defs.getAttackDelay();
    }
}