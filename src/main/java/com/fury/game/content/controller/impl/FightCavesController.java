package com.fury.game.content.controller.impl;

import com.fury.core.model.node.entity.actor.figure.player.Player;
import com.fury.core.task.Task;
import com.fury.engine.task.executor.GameExecutorManager;
import com.fury.game.content.controller.Controller;
import com.fury.game.content.dialogue.impl.minigames.fightcave.JadD;
import com.fury.game.content.dialogue.impl.npcs.SimpleNpcMessageD;
import com.fury.game.content.global.Achievements;
import com.fury.game.content.skill.member.summoning.impl.Pets;
import com.fury.game.content.skill.member.summoning.impl.Summoning;
import com.fury.core.model.node.entity.actor.figure.mob.Mob;
import com.fury.core.model.item.Item;
import com.fury.game.entity.object.GameObject;
import com.fury.game.node.entity.actor.object.ObjectHandler;
import com.fury.game.npc.minigames.fightcaves.FightCavesMob;
import com.fury.game.npc.minigames.fightcaves.TzTok_Jad;
import com.fury.game.npc.minigames.fightcaves.Yt_HurKot;
import com.fury.game.world.GameWorld;
import com.fury.game.world.map.Position;
import com.fury.game.world.map.instance.MapInstance;
import com.fury.util.Misc;

import java.util.Collection;
import java.util.TimerTask;

public class FightCavesController extends Controller {

    private MapInstance instance;

    public static final Position OUTSIDE = new Position(2439, 5170);

    private static final int THHAAR_MEJ_JAL = 2617;

    private static final int[][] WAVES = {
            {2734},
            {2734, 2734},
            {2736},
            {2736, 2734},
            {2736, 2734, 2734},
            {2736, 2736},
            {2739},
            {2739, 2734},
            {2739, 2734, 2734},
            {2739, 2736},
            {2739, 2736, 2734},
            {2739, 2736, 2734, 2734},
            {2739, 2736, 2736},
            {2739, 2739},
            {2741},
            {2741, 2734},
            {2741, 2734, 2734},
            {2741, 2736},
            {2741, 2736, 2734},
            {2741, 2736, 2734, 2734},
            {2741, 2736, 2736},
            {2741, 2739},
            {2741, 2739, 2734},
            {2741, 2739, 2734, 2734},
            {2741, 2739, 2736},
            {2741, 2739, 2736, 2734},
            {2741, 2739, 2736, 2734, 2734},
            {2741, 2739, 2736, 2736},
            {2741, 2739, 2739},
            {2741, 2741},
            {2743},
            {2743, 2734},
            {2743, 2734, 2734},
            {2743, 2736},
            {2743, 2736, 2734},
            {2743, 2736, 2734, 2734},
            {2743, 2736, 2736},
            {2743, 2739},
            {2743, 2739, 2734},
            {2743, 2739, 2734, 2734},
            {2743, 2739, 2736},
            {2743, 2739, 2736, 2734},
            {2743, 2739, 2736, 2734, 2734},
            {2743, 2739, 2736, 2736},
            {2743, 2739, 2739},
            {2743, 2741},
            {2743, 2741, 2734},
            {2743, 2741, 2734, 2734},
            {2743, 2741, 2736},
            {2743, 2741, 2736, 2734},
            {2743, 2741, 2736, 2734, 2734},
            {2743, 2741, 2736, 2736},
            {2743, 2741, 2739},
            {2743, 2741, 2739, 2734},
            {2743, 2741, 2739, 2734, 2734},
            {2743, 2741, 2739, 2736},
            {2743, 2741, 2739, 2736, 2734},
            {2743, 2741, 2739, 2736, 2734, 2734},
            {2743, 2741, 2739, 2736, 2736},
            {2743, 2741, 2739, 2739},
            {2743, 2741, 2741},
            {2743, 2743},
            {2745}
    };

    private boolean login;
    public boolean spawned;
    private int currentWave;

    public static void enterFightCaves(Player player) {
        if (player.getFamiliar() != null || player.getPet() != null || Summoning.hasPouch(player) || Pets.hasPet(player)) {
            player.getDialogueManager().startDialogue(new SimpleNpcMessageD(), THHAAR_MEJ_JAL, "No Kimit-Zil in the pits! This is a fight for YOU, not your friends!");
            return;
        }
        player.getControllerManager().startController(new FightCavesController(), 60); // wave to start at
    }

    @Override
    public void start() {
        int wave = (int) getArguments()[0];
        setCurrentWave(wave);
        loadCave(false);
    }

    /**
     * return process normaly
     */
    @Override
    public boolean processObjectClick1(GameObject object) {
        if (object.getId() == 9357) {
            if (instance.getStage() != MapInstance.Stages.RUNNING)
                return false;
            exitCave(1);
            return false;
        }
        return true;
    }

    @Override
    public boolean login() {
        removeController();
        player.moveTo(new Position(OUTSIDE, 2));
        return false;
    }

    public void loadCave(final boolean login) {
        this.login = login;
        player.getMovement().lock(); // locks player
        instance = new MapInstance(296, 632);
        instance.load(() -> {
            Position tile = !login ? getWorldTile(46, 51) : getWorldTile(32, 32);
            player.moveTo(tile);

            GameWorld.schedule(1, () -> {
                if (!login) {
                    Position walkTo = getWorldTile(32, 32);
                    player.getMovement().addWalkSteps(walkTo.getX(), walkTo.getY(), -1, false);
                }
                player.getDialogueManager().startDialogue(new SimpleNpcMessageD(), THHAAR_MEJ_JAL, "You're on your own now, JalYt.", "Prepare to fight for your life!");
                player.getMovement().unlock(); // unlocks player
            });

            if (!login) {
                /*
                 * lets stress less the worldthread, also fastexecutor used
                 * for mini stuff
                 */
                GameExecutorManager.fastExecutor.schedule(new TimerTask() {

                    @Override
                    public void run() {
                        if (instance.getStage() != MapInstance.Stages.RUNNING)
                            return;
                        startWave();
                    }
                }, 6000);
            }
        });
    }

    public Position getSpawnTile() {
        switch (Misc.random(5)) {
            case 0:
                return getWorldTile(11, 16);
            case 1:
                return getWorldTile(51, 25);
            case 2:
                return getWorldTile(10, 50);
            case 3:
                return getWorldTile(46, 49);
            case 4:
            default:
                return getWorldTile(32, 30);
        }
    }

    @Override
    public void moved() {
        if (instance.getStage() != MapInstance.Stages.RUNNING || !login)
            return;
        login = false;
        setWaveEvent();
    }

    public void startWave() {
        int currentWave = getCurrentWave();
        if (currentWave > WAVES.length) {
            win();
            return;
        }
        player.message("Wave " + currentWave);
//        player.getInterfaceManager().sendMinigameInterface(316);

        if (instance.getStage() != MapInstance.Stages.RUNNING)
            return;
        for (int id : WAVES[currentWave - 1]) {
            /*if (id == 2736)
                new TzKekCaves(id, getSpawnTile());
            else*/
            if (id == 2745)
                new TzTok_Jad(id, getSpawnTile(), this);
            else
                new FightCavesMob(id, getSpawnTile());
        }
        spawned = true;
    }

    public void spawnHealers(TzTok_Jad tzTok_Jad) {
        if (instance.getStage() != MapInstance.Stages.RUNNING)
            return;
        for (int i = 0; i < 4; i++)
            new Yt_HurKot(2746, getSpawnTile(), tzTok_Jad);
    }

    public void win() {
        if (instance.getStage() != MapInstance.Stages.RUNNING)
            return;
        exitCave(4);
    }

    public void nextWave() {
        setCurrentWave(getCurrentWave() + 1);
        setWaveEvent();
    }

    public void setWaveEvent() {
        if (getCurrentWave() == 63)
            player.getDialogueManager().startDialogue(new JadD());
        GameExecutorManager.fastExecutor.schedule(new TimerTask() {

            @Override
            public void run() {
                if (instance.getStage() != MapInstance.Stages.RUNNING)
                    return;
                startWave();
            }
        }, 600);
    }

    @Override
    public void process() {
        if (spawned) {
            Collection<Mob> mobs = player.getRegion().getNpcs(player.getZ());
            if (mobs == null || mobs.isEmpty()) {
                spawned = false;
                nextWave();
            }
        }
    }

    @Override
    public boolean sendDeath() {
        player.getMovement().lock(8);
        player.stopAll();
        GameWorld.schedule(new Task(true) {
            int loop;
            @Override
            public void run() {
                if (loop == 0) {
                    player.perform(player.getDeathAnimation());
                } else if (loop == 1) {
                    player.message("You have been defeated!");
                } else if (loop == 3) {
                    player.reset();
                    exitCave(1);
                } else if (loop == 4) {
                    stop();
                }
                loop++;
            }
        });
        return false;
    }

    @Override
    public void magicTeleported(int type) {
        exitCave(2);
    }

    public void exitCave(int type) {
        instance.destroy(null);
        Position outside = new Position(OUTSIDE, 2);
        if (type == 0)
            player.moveTo(outside);
        else {
//            player.getInterfaceManager().removeMinigameInterface();
            if (type == 1 || type == 4) {
                ObjectHandler.useStairs(player, -1, outside, 1, 2);
                GameWorld.schedule(1, () -> {
                    if (type == 4) {
//                        player.setCompletedFightCaves();
                        player.reset();
                        player.getDialogueManager().startDialogue(new SimpleNpcMessageD(), THHAAR_MEJ_JAL, "You even defeated Tz Tok-Jad,", "I am most impressed!", "Please accept this gift as a reward.");
                        player.message("You were victorious!!");
                        player.getInventory().addSafe(new Item(6570, 1));
                        player.getInventory().addSafe(new Item(6529, 16064));
                        Achievements.finishAchievement(player, Achievements.AchievementData.DEFEAT_JAD);
                    } else if (getCurrentWave() == 60)
                        player.getDialogueManager().startDialogue(new SimpleNpcMessageD(), THHAAR_MEJ_JAL, "Well I suppose you tried...", "better luck next time.");
                    else {
                        int tokkul = (getCurrentWave() - 60) * 2008;
                        if (tokkul > 0)
                            player.getInventory().addSafe(new Item(6529, tokkul));
                        player.getDialogueManager().startDialogue(new SimpleNpcMessageD(), THHAAR_MEJ_JAL, "Well done in the cave,", "here, take TokKul as reward.");
                    }
                });
            }
            removeController();
        }
    }


    public Position getWorldTile(int mapX, int mapY) {
        return new Position(instance.getInstancePos()[0] * 8 + mapX, instance.getInstancePos()[1] * 8 + mapY, 0);
    }

    @Override
    public boolean logout() {
        if (instance.getStage() != MapInstance.Stages.RUNNING)
            return false;

        exitCave(0);
        return false;

    }

    public int getCurrentWave() {
        return currentWave;
    }

    public void setCurrentWave(int wave) {
        currentWave = wave;
    }

    @Override
    public void forceClose() {
        if (instance.getStage() != MapInstance.Stages.RUNNING)
            return;
        exitCave(2);
    }
}
