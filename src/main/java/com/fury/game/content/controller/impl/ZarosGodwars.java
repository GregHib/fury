package com.fury.game.content.controller.impl;

import com.fury.core.model.node.entity.actor.figure.Figure;
import com.fury.core.model.node.entity.actor.figure.Projectile;
import com.fury.core.model.node.entity.actor.figure.player.Player;
import com.fury.core.task.Task;
import com.fury.engine.task.executor.GameExecutorManager;
import com.fury.game.content.controller.Controller;
import com.fury.game.entity.object.GameObject;
import com.fury.game.node.entity.actor.figure.ProjectileManager;
import com.fury.game.node.entity.actor.figure.player.handles.Settings;
import com.fury.game.node.entity.actor.object.ObjectManager;
import com.fury.game.npc.bosses.nex.Nex;
import com.fury.game.npc.bosses.nex.NexMinion;
import com.fury.game.world.GameWorld;
import com.fury.game.world.map.Position;
import com.fury.game.world.update.flag.block.Direction;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class ZarosGodwars extends Controller {
    @Override
    public void start() {
        addPlayer(player);
        sendInterfaces();
    }

    @Override
    public boolean logout() {
        removePlayer(player);
        return false; // so doesn't remove script
    }

    public boolean login() {
        addPlayer(player);
        sendInterfaces();
        return false; // so doesnt remove script
    }

    @Override
    public void sendInterfaces() {
//        player.getInterfaceManager().sendTab(player.getInterfaceManager().hasRezizableScreen() ? 9 : 8, 601);
    }

    @Override
    public boolean sendDeath() {
        remove();
        removeController();
        return true;
    }

    @Override
    public void magicTeleported(int type) {
        remove();
        removeController();
    }

    @Override
    public void forceClose() {
        remove();
    }

    public void remove() {
        removePlayer(player);
    }

    private static final List<Player> playersOn = Collections.synchronizedList(new ArrayList<Player>());

    private static Nex nex;
    private static NexMinion fumus;
    public static NexMinion umbra;
    public static NexMinion cruor;
    public static NexMinion glacies;

    private static int getPlayersCount() {
        return playersOn.size();
    }

    public static void breakFumusBarrier() {
        if (fumus == null)
            return;
        fumus.breakBarrier();
    }

    public static void breakUmbraBarrier() {
        if (umbra == null)
            return;
        umbra.breakBarrier();
    }

    public static void breakCruorBarrier() {
        if (cruor == null)
            return;
        cruor.breakBarrier();
    }

    public static void breakGlaciesBarrier() {
        if (glacies == null)
            return;
        glacies.breakBarrier();
    }

    private static void addPlayer(Player player) {
        if (playersOn.contains(player)) {
            return;
        }
        playersOn.add(player);

        startWar();
    }

    public static void removePlayer(Player player) {
        playersOn.remove(player);
        cancelWar();
    }

    private static void deleteNpcs() {
        if (nex != null) {
            nex.killBloodReavers();
            nex.deregister();
            nex = null;
        }
        if (fumus != null) {
            fumus.deregister();
            fumus = null;
        }
        if (umbra != null) {
            umbra.deregister();
            umbra = null;
        }
        if (cruor != null) {
            cruor.deregister();
            cruor = null;
        }
        if (glacies != null) {
            glacies.deregister();
            glacies = null;
        }
    }

    private static void cancelWar() {
        if (getPlayersCount() == 0)
            deleteNpcs();
    }

    public static ArrayList<Figure> getPossibleTargets() {
        ArrayList<Figure> possibleTarget = new ArrayList<>(
                playersOn.size());
        for (Player player : playersOn) {
            if (player == null || player.isDead() || player.getFinished() || !player.getSettings().getBool(Settings.RUNNING))
                continue;
            possibleTarget.add(player);
            /*
             * if (player.getFamiliar() != null &&
             * player.getFamiliar().isAgressive())
             * possibleTarget.add(player.getFamiliar());
             */
        }
        return possibleTarget;
    }

    public static void moveNextStage() {
        if (nex == null)
            return;
        nex.moveNextStage();
    }

    public static void endWar() {
        deleteNpcs();
        // GameWorld.schedule(60, () -> startWar());
        //GameExecutorManager.slowExecutor.schedule(() -> startWar(), 1, TimeUnit.MINUTES);
    }

    // TODO: replace sync with proper lock
    // called by slowExecutor thread
    // called by engine thread
    private static void startWar() {
        if (getPlayersCount() >= 1) {
            if (nex == null) {
                nex = (Nex) GameWorld.getMobs().spawn(13447, new Position(2924, 5202), true);
                GameWorld.schedule(new Task(false) { // called by engine thread (except first iteration if instant) - disabled instant
                    private int count = 0;

                    @Override
                    public void run() {
                        if (nex == null) {
                            stop();
                            return;
                        }
                        if (count == 1) {
                            nex.forceChat("AT LAST!");
                            nex.animate(6355);
                            nex.graphic(1217);
//                            nex.playSound(3295, 2);
                        } else if (count == 3) {
                            fumus = (NexMinion) GameWorld.getMobs().spawn(13451, new Position(2912, 5216), true);
                            fumus.getDirection().setDirection(Direction.SOUTH_EAST);
                            nex.getDirection().face(new Position(fumus.getCoordFaceX(fumus.getSizeX()), fumus.getCoordFaceY(fumus.getSizeY()), 0));
                            nex.forceChat("Fumus!");
                            nex.animate(6987);
                            ProjectileManager.send(new Projectile(fumus, nex, 2244, 18, 18, 60, 30, 0, 0));
//                            nex.playSound(3325, 2);
                        } else if (count == 5) {
                            umbra = (NexMinion) GameWorld.getMobs().spawn(13452, new Position(2937, 5216), true);
                            umbra.getDirection().setDirection(Direction.SOUTH_WEST);
                            nex.getDirection().face(new Position(umbra.getCoordFaceX(umbra.getSizeX()), umbra.getCoordFaceY(umbra.getSizeY()), 0));
                            nex.forceChat("Umbra!");
                            nex.animate(6987);
                            ProjectileManager.send(new Projectile(umbra, nex, 2244, 18, 18, 60, 30, 0, 0));
//                            nex.playSound(3313, 2);
                        } else if (count == 7) {
                            cruor = (NexMinion) GameWorld.getMobs().spawn(13453, new Position(2937, 5190), true);
                            cruor.getDirection().setDirection(Direction.NORTH_WEST);
                            nex.getDirection().face(new Position(cruor.getCoordFaceX(cruor.getSizeX()), cruor.getCoordFaceY(cruor.getSizeY()), 0));
                            nex.forceChat("Cruor!");
                            nex.animate(6987);
                            ProjectileManager.send(new Projectile(cruor, nex, 2244, 18, 18, 60, 30, 0, 0));
//                            nex.playSound(3299, 2);
                        } else if (count == 9) {
                            glacies = (NexMinion) GameWorld.getMobs().spawn(13454, new Position(2912, 5190), true);
                            glacies.getDirection().face(new Position(glacies.getCoordFaceX(glacies.getSizeX()), glacies.getCoordFaceY(glacies.getSizeY()), 0));
                            glacies.getDirection().setDirection(Direction.NORTH_EAST);
                            nex.getDirection().face(new Position(glacies.getCoordFaceX(glacies.getSizeX()), glacies.getCoordFaceY(glacies.getSizeY()), 0));
                            nex.forceChat("Glacies!");
                            nex.animate(6987);
                            ProjectileManager.send(new Projectile(glacies, nex, 2244, 18, 18, 60, 30, 0, 0));
//                            nex.playSound(3304, 2);
                        } else if (count == 11) {
                            nex.forceChat("Fill my soul with smoke!");
                            //ProjectileManager.send(new Projectile(fumus, nex, 2244, 18, 18, 60, 30, 0, 0));

//                            nex.playSound(3310, 2);
                        } else if (count == 13) {
                            nex.setCantInteract(false);
                            stop();
                            return;
                        }
                        count++;
                    }
                });
            }
        }
    }
}
