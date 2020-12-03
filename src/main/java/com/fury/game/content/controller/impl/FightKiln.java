package com.fury.game.content.controller.impl;

import com.fury.cache.Revision;
import com.fury.cache.def.Loader;
import com.fury.core.model.node.entity.actor.figure.player.Player;
import com.fury.core.task.Task;
import com.fury.engine.task.executor.FadingScreen;
import com.fury.engine.task.executor.GameExecutorManager;
import com.fury.game.content.controller.Controller;
import com.fury.game.content.dialogue.impl.minigames.fightkiln.FightKilnD;
import com.fury.game.content.dialogue.impl.minigames.fightkiln.TokHaarHokD;
import com.fury.game.content.skill.Skill;
import com.fury.game.entity.character.combat.effects.Effect;
import com.fury.game.entity.character.combat.effects.Effects;
import com.fury.core.model.node.entity.actor.figure.mob.Mob;
import com.fury.game.entity.character.npc.impl.fightkiln.FightKilnMob;
import com.fury.game.entity.character.npc.impl.fightkiln.HarAken;
import com.fury.game.entity.character.npc.impl.fightkiln.TokHaarKetDill;
import com.fury.core.model.item.Item;
import com.fury.game.entity.object.GameObject;
import com.fury.game.node.entity.actor.figure.player.PlayerDeath;
import com.fury.game.node.entity.actor.figure.player.handles.Settings;
import com.fury.game.npc.familiar.Familiar;
import com.fury.game.world.FloorItemManager;
import com.fury.game.world.GameWorld;
import com.fury.game.world.map.Position;
import com.fury.game.world.map.build.MapBuilder;
import com.fury.game.world.update.flag.block.Animation;
import com.fury.game.world.update.flag.block.Direction;
import com.fury.util.Logger;
import com.fury.util.Utils;

import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

public class FightKiln extends Controller {

    public static final Position OUTSIDE = new Position(4744, 5172);

    private static final int TOKHAAR_HOK = 15195, TOKHAAR_HOK_SCENE = 15200;

    private static final int[] MUSICS = {1088, 1082, 1086};

    public void playMusic() {
        //player.getMusicsManager().playMusic(selectedMusic);
    }

    /*
     * 0 - south east
     * 1 - south west
     * 2 - north west
     * 3 - north east
     * TokHaar-Hur - 15201
     * TokHaar-Xil - 15202
     * TokHaar-Mej - 15203
     * TokHaar-Ket - 15204
     * TokHaar-Tok-Xil - 15205
     * TokHaar-Yt-Mejkot - 15206
     * TokHaar-Ket-Zek - 15207
     * TokHaar-Jad - 15208
     * TokHaar-Ket-Dill - 15213


     */
    private final int[][] WAVES = {
            {15202, 15202, 15205, 15201, 15201}, //1
            {15202, 15202, 15205, 15205, 15201}, //2
            {15202, 15205, 15205, 15205, 15201}, //3
            {15205, 15205, 15205, 15203, 15203}, //4
            {15202, 15205, 15205, 15205, 15213}, //5
            {15202, 15205, 15203, 15203, 15205, 15205}, //6
            {15203, 15205, 15205, 15205, 15202, 15205}, //7
            {15207, 15205, 15205}, //8
            {15205, 15205, 15205, 15205, 15205, 15205}, //9
            {15205, 15208}, //10
            {15203, 15203, 15203, 15203}, //11
            {15203, 15205, 15205, 15203}, //12
            {15203, 15207, 15203}, //13
            {15207, 15207, 15203, 15203}, //14
            {15207, 15207, 15205}, //15
            {15207, 15207, 15205, 15203, 15203}, //16
            {15207, 15207, 15205, 15203, 15206}, //17
            {15207, 15207, 15205, 15206, 15205, 15205}, //18
            {15203, 15203, 15203, 15203, 15203, 15203, 15203, 15203, 15203}, //19
            {15207, 15208}, //20
            {15201, 15201, 15201, 15201, 15201, 15201, 15201, 15201, 15201, 15201, 15201, 15201}, //21
            {15206, 15201, 15204, 15204, 15201}, //22
            {15206, 15206, 15204, 15206, 15201}, //23
            {15206, 15206, 15206, 15205, 15206}, //24
            {15206, 15206, 15205, 15205, 15207}, //25
            {15206, 15206, 15205, 15207, 15207}, //26
            {15204, 15206, 15205, 15204, 15206}, //27
            {15213, 15213, 15207, 15213, 15213, 15213, 15213}, //28
            {15206, 15206, 15206, 15206, 15206, 15206}, //29
            {15206, 15208, 15206, 15206}, //30
            {15205, 15205, 15205, 15205}, //31
            {15206, 15206, 15206, 15206}, //32
            {15207, 15207, 15207, 15207}, //33
            {15205, 15208, 15206}, //34
            {15207, 15206, 15206, 15208}, //35
            {15208, 15208} //36
    };


    /*** combat definitions TODO
     //npcId - hitpoints attackAnim defenceAnim deathAnim attackDelay deathDelay respawnDelay maxHit attackStyle attackGfx attackProjectile agressivenessType
     //TokHaar-Hur
     15201 - 900 16098 16173 16178 5 2 60 124 MELEE -1 -1 AGRESSIVE
     15202 - 1300 16148 16146 16145 5 2 60 194 RANGE -1 2985 AGRESSIVE
     15203 - 1940 16127 16124 16123 5 2 60 245 SPECIAL -1 -1 AGRESSIVE
     15204 - 1500 16180 16182 16181 5 2 60 214 MELEE -1 -1 AGRESSIVE
     15205 - 700 16131 16130 16110 5 3 60 234 SPECIAL -1 -1 AGRESSIVE
     15206 - 1000 16102 16101 16103 4 4 60 460 MELEE -1 -1 AGRESSIVE
     15207 - 2000 16134 16135 16111 4 3 60 780 SPECIAL -1 -1 AGRESSIVE
     15208 - 2500 16204 16201 16188 7 6 1 1014 SPECIAL -1 -1 AGRESSIVE
     15211 - 12800 -1 16246 16235 4 3 60 1 SPECIAL -1 -1 PASSIVE
     15212 - 12800 -1 16246 16235 4 3 60 1 SPECIAL -1 -1 PASSIVE
     //TokHaar-Ket-Dill
     15213 - 750 16210 16160 16197 8 3 60 250 MELEE -1 -1 AGRESSIVE
     //TokHaar-Ket-Dill (after armour broken by pickaxe)
     15214 - 750 16210 16160 16197 4 3 60 250 MELEE -1 -1 AGRESSIVE
     //Har-Aken (Ranged Tentacle)
     15209 - 700 16239 16253 16240 4 3 60 230 SPECIAL -1 -1 AGRESSIVE
     //Har-Aken (Magic Tentacle)
     15210 - 700 16252 16253 16243 4 3 60 230 SPECIAL -1 -1 AGRESSIVE
     //jads jad, TokHaar-Jad
     2745 - 2500 16204 16201 16188 7 6 1 998 SPECIAL -1 -1 AGRESSIVE
     14416 - 1 16204 16201 16188 5 4 1 550 SPECIAL -1 -1 AGRESSIVE
     ***/


    private int[] boundChuncks;
    private Stages stage;
    private boolean logoutAtEnd;
    private boolean login;
    public int selectedMusic;
    private Mob tokHaarHok;
    private HarAken harAken;
    private int aliveNPCSCount;

    public static void enterFightKiln(Player player, boolean quickEnter) {
        Familiar familiar = player.getFamiliar();
        if (familiar != null && ((player.getFamiliar() != null && player.getFamiliar().getBeastOfBurden().contains(new Item(23653), new Item(23654), new Item(23655), new Item(23656), new Item(23657), new Item(23658)))))
            return;
        if (!quickEnter)
            player.getDialogueManager().startDialogue(new FightKilnD());
        else
            player.getControllerManager().startController(new FightKiln(), 1); //start at wave 1
    }

    private enum Stages {
        LOADING,
        RUNNING,
        DESTROYING
    }

    @Override
    public void start() {
        loadCave(false);
    }

    @Override
    public boolean processButtonClick(int componentId) {
        /*if(stage != Stages.RUNNING)
            return false;
        if(interfaceId == 182 && (componentId == 6 || componentId == 13)) {
            if(!logoutAtEnd) {
                logoutAtEnd = true;
                player.getPacketSender().sendMessage("<col=ff0000>You will be logged out automatically at the end of this wave.");
                player.getPacketSender().sendMessage("<col=ff0000>If you log out sooner, you will have to repeat this wave.");
            }else
                player.logout();
            return false;
        }*/
        return true;
    }


    @Override
    public boolean processUntouchableObjectClick1(GameObject object) {
        if (object.getId() == 68111) {
            if (stage != Stages.RUNNING)
                return false;
            exitCave(1);
            return false;
        }
        return true;
    }

    @Override
    public boolean processObjectClick1(GameObject object) {
        if (object.getId() == 68111) {
            if (stage != Stages.RUNNING)
                return false;
            exitCave(1);
            return false;
        }
        return true;
    }


    /*
     * return false so wont remove script
     */
    @Override
    public boolean login() {
        loadCave(true);
        return false;
    }

    public int[] getMap() {
        int wave = getCurrentWave();
        if (wave < 11)
            return new int[]{504, 632};
        if (wave < 21)
            return new int[]{512, 632};
        if (wave < 31)
            return new int[]{520, 632};
        if (wave < 34)
            return new int[]{528, 632};
        return new int[]{536, 632};
    }

    public void buildMap() {
        int[] map = getMap();
        MapBuilder.copyAllPlanesMap(map[0], map[1], boundChuncks[0], boundChuncks[1], 8);
    }

    public static int getX(Player player, int x) {
        return new Position(x, 0, 0).getLocalX(
                player.getLastKnownRegion());
    }

    public static int getY(Player player, int y) {
        return new Position(0, y, 0).getLocalY(
                player.getLastKnownRegion());
    }

    public void loadCave(final boolean login) {
        final FightKiln kiln = this;
        stage = Stages.LOADING;
        player.getMovement().lock(); //locks player
        Runnable event = () -> GameExecutorManager.slowExecutor.execute(() -> {
            int currentWave = getCurrentWave();
            //finds empty map bounds
            if (boundChuncks == null) {
                boundChuncks = MapBuilder.findEmptyChunkBound(8, 8);
                buildMap();
            } else if (!login &&
                    (currentWave == 11 || currentWave == 21
                            || currentWave == 31 || currentWave == 34)) {
                buildMap();
                //player.setForceNextMapLoadRefresh(true);
                player.loadMapRegions();
            }
            //selects a music
            selectedMusic = MUSICS[Utils.random(MUSICS.length)];
            playMusic();
            //player.setForceMultiArea(true);
            player.stopAll();
            if (currentWave == 0) { //SCENE 0
            /*player.getInventory().deleteItemSet(new Item(23653, Integer.MAX_VALUE)
                    , new Item(23654, Integer.MAX_VALUE),
                    new Item(23655, Integer.MAX_VALUE),
                    new Item(23656, Integer.MAX_VALUE),
                    new Item(23657, Integer.MAX_VALUE),
                    new Item(23658, Integer.MAX_VALUE));*/
                player.setPosition(getPosition(31, 51));
                player.moveTo(player);
                tokHaarHok = new Mob(TOKHAAR_HOK, getPosition(30, 36), Revision.PRE_RS3);
                tokHaarHok.getDirection().face(player);
                //1delay because player cant walk while teleing :p, + possible issues avoid

                GameWorld.schedule(6, new Runnable() {
                    boolean run;

                    @Override
                    public void run() {
                        run = player.getSettings().getBool(Settings.RUNNING);
                        player.getSettings().set(Settings.RUNNING, false);
                        Position walkTo = getPosition(31, 39);
                        player.getMovement().addWalkSteps(walkTo.getX(), walkTo.getY(), -1, false);
                        GameWorld.schedule(new Task(false, 6) {
                            int count = 0;

                            @Override
                            public void run() {
                                if (count == 0) {
                                    player.getPacketSender().sendCameraNeutrality();
                                } else if (count == 1) {
                                    player.getDialogueManager().startDialogue(new TokHaarHokD(), 0, TOKHAAR_HOK, kiln);
                                    player.getSettings().set(Settings.RUNNING, run);
                                    stage = Stages.RUNNING;
                                    player.getMovement().unlock();
                                    stop();
                                }
                                count++;
                            }
                        });
                    }
                });
            } else if (currentWave == 38) { //SCENE 7, WIN
                player.setPosition(getPosition(38, 25));
                player.moveTo(player);
                player.getDirection().face(getPosition(38, 26));
                tokHaarHok = new Mob(TOKHAAR_HOK_SCENE, getPosition(37, 30), Revision.PRE_RS3);
                tokHaarHok.getDirection().face(player);
                //player.getPacketSender().sendBlackOut(2);
                //player.getPacketSender().sendConfig(1241, 1);
                GameWorld.schedule(1, () -> {
                    HarAken harAken = new HarAken(15211, getPosition(45, 26), kiln);
                    harAken.spawn();
                    harAken.sendDeath(player);
                    GameExecutorManager.fastExecutor.schedule(new TimerTask() {
                        @Override
                        public void run() {
                            player.getDialogueManager().startDialogue(new TokHaarHokD(), 6, TOKHAAR_HOK_SCENE, kiln);
                        }
                    }, 5000);
                });
            } else if (currentWave == 37) { //SCENE 6
                player.setPosition(getPosition(38, 25));
                player.moveTo(player);
                player.getDirection().face(getPosition(38, 26));
                tokHaarHok = new Mob(TOKHAAR_HOK_SCENE, getPosition(37, 30), Revision.PRE_RS3);
                tokHaarHok.getDirection().face(player);
                //player.getPacketSender().sendBlackOut(2);
                //player.getPacketSender().sendConfig(1241, 1);
                GameWorld.schedule(1, () -> player.getDialogueManager().startDialogue(new TokHaarHokD(), 5, TOKHAAR_HOK_SCENE, kiln));
            } else if (currentWave == 34) { //SCENE 5
                teleportPlayerToMiddle();
                //player.getPacketSender().sendBlackOut(2);
                //player.getPacketSender().sendConfig(1241, 1);
                GameWorld.schedule(new Task(false, 6) {
                    int count = 0;

                    @Override
                    public void run() {
                        if (count == 0) {
                            player.setPosition(getPosition(33, 39));
                            player.moveTo(player);
                            player.getDirection().face(getPosition(32, 39));
                        } else if (count == 1) {
                            stop();
                            tokHaarHok = new Mob(TOKHAAR_HOK_SCENE, getPosition(28, 38), Revision.PRE_RS3);
                            tokHaarHok.getDirection().face(player);
                            player.getDialogueManager().startDialogue(new TokHaarHokD(), 7, TOKHAAR_HOK_SCENE, kiln);
                        }
                        count++;
                    }
                });
            } else if (currentWave == 31) { //SCENE 4
                player.setPosition(getPosition(21, 21));
                player.moveTo(player);
                player.getDirection().face(getPosition(20, 20));
                //player.getPacketSender().sendBlackOut(2);
                //player.getPacketSender().sendConfig(1241, 1);

                GameWorld.schedule(1, () -> player.getDialogueManager().startDialogue(new TokHaarHokD(), 4, TOKHAAR_HOK_SCENE, kiln));
            } else if (currentWave == 21) { //SCENE 3
                tokHaarHok = new Mob(TOKHAAR_HOK_SCENE, getPosition(30, 43), Revision.PRE_RS3);
                tokHaarHok.getDirection().face(player);
                teleportPlayerToMiddle();
                //player.getPacketSender().sendBlackOut(2);
                //player.getPacketSender().sendConfig(1241, 1);
                GameWorld.schedule(1, () -> player.getDialogueManager().startDialogue(new TokHaarHokD(), 3, TOKHAAR_HOK_SCENE, kiln));
            } else if (currentWave == 11) { //SCENE 2
                tokHaarHok = new Mob(TOKHAAR_HOK_SCENE, getPosition(45, 45), Revision.PRE_RS3);
                tokHaarHok.getDirection().face(player);
                teleportPlayerToMiddle();
                //player.getPacketSender().sendBlackOut(2);
                //player.getPacketSender().sendConfig(1241, 1);
                GameWorld.schedule(1, () -> {
                    //Position lookTo = getPosition(45, 45);
                    //player.getPacketSender().sendCameraAngle(getX(player, lookTo.getX()),
                    //getY(player, lookTo.getY()), lookTo.getZ(), 0, 1000);
                    player.getDialogueManager().startDialogue(new TokHaarHokD(), 2, TOKHAAR_HOK_SCENE, kiln);
                });
            } else if (currentWave == 1) { //SCENE 1
            /*player.getInventory().deleteItemSet(new Item(23653, Integer.MAX_VALUE)
                    , new Item(23654, Integer.MAX_VALUE),
                    new Item(23655, Integer.MAX_VALUE),
                    new Item(23656, Integer.MAX_VALUE),
                    new Item(23657, Integer.MAX_VALUE),
                    new Item(23658, Integer.MAX_VALUE));*/
                tokHaarHok = new Mob(TOKHAAR_HOK_SCENE, getPosition(30, 36), Revision.PRE_RS3);
                tokHaarHok.getDirection().face(player);
                player.setPosition(getPosition(31, 39));
                player.moveTo(player);
                //player.getPacketSender().sendBlackOut(2);
                //player.getPacketSender().sendConfig(1241, 1);
                player.getDirection().face(tokHaarHok);
                GameWorld.schedule(1, () -> {
                    Position lookTo = getPosition(31, 40);
                    /*player.getPacketSender().sendCameraAngle(getX(player, lookTo.getX()),
                            getY(player, lookTo.getY()), lookTo.getZ(), 0, 1000);*/
                    player.getDialogueManager().startDialogue(new TokHaarHokD(), 1, TOKHAAR_HOK_SCENE, kiln);
                    stage = Stages.RUNNING;
                    player.getMovement().unlock();
                });
            } else if (login) { //LOGIN during
                kiln.login = login;
                teleportPlayerToMiddle();
                GameWorld.schedule(1, () -> {
                    stage = Stages.RUNNING;
                    player.getMovement().unlock();
                });
            }
        });
        if (!login)
            FadingScreen.fade(player, event);
        else
            event.run();
    }

    public Position getMaxTile() {
        if (getCurrentWave() < 11)
            return getPosition(49, 49);
        if (getCurrentWave() < 21)
            return getPosition(47, 47);
        if (getCurrentWave() < 31)
            return getPosition(45, 45);
        if (getCurrentWave() < 34)
            return getPosition(43, 43);
        return getPosition(41, 41);
    }

    public Position getMinTile() {
        if (getCurrentWave() < 11)
            return getPosition(14, 14);
        if (getCurrentWave() < 21)
            return getPosition(16, 16);
        if (getCurrentWave() < 31)
            return getPosition(18, 18);
        if (getCurrentWave() < 34)
            return getPosition(20, 20);
        return getPosition(22, 22);
    }


    /*
     * 20, 20 min X, min Y
42 42 maxX, maxY
     */
    /*
     * 0 - north
     * 1 - south
     * 2 - east
     * 3 - west
     */
    public Position getTentacleTile() {
        int corner = Utils.random(4);
        int position = Utils.random(5);
        while (corner != 0 && position == 2)
            position = Utils.random(5);
        switch (corner) {
            case 0: //north
                return getPosition(21 + (position * 5), 42);
            case 1: //south
                return getPosition(21 + (position * 5), 20);
            case 2: //east
                return getPosition(42, 21 + (position * 5));
            case 3: //west
            default:
                return getPosition(20, 21 + (position * 5));
        }
    }


    public Position getSpawnTile(int count, int size) {
        int position = count % 4;
        switch (position) {
            case 0: //east south
                Position maxTile = getMaxTile();
                Position minTile = getMinTile();
                return new Position(maxTile.getX() - 1 - size, minTile.getY() + 2, 1);
            case 1: // west south
                return getMinTile().transform(2, 2, 0);
            case 2: //west north
                maxTile = getMaxTile();
                minTile = getMinTile();
                return new Position(minTile.getX() + 2, maxTile.getY() - 1 - size, 1);
            case 3: //east north
            default:
                return getMaxTile().transform(-1 - size, -1 - size, 0);
        }
    }

    @Override
    public void moved() {
        if (stage != Stages.RUNNING || !login)
            return;
        login = false;
        setWaveEvent();
    }

    public void startWave() {
        if (stage != Stages.RUNNING)
            return;
        int currentWave = getCurrentWave();
        player.message("Starting Wave: " + currentWave, true);
        //player.getPacketSender().sendTab(316);
        //player.getPacketSender().sendConfig(639, currentWave);
        if (currentWave > WAVES.length) {
            if (currentWave == 37)
                aliveNPCSCount = 1;
            return;
        } else if (currentWave == 0) {
            exitCave(1);
            return;
        }
        aliveNPCSCount = WAVES[currentWave - 1].length;
        for (int i = 0; i < WAVES[currentWave - 1].length; i += 4) {
            final int next = i;
            GameExecutorManager.fastExecutor.schedule(new TimerTask() {
                @Override
                public void run() {
                    if (stage != Stages.RUNNING)
                        return;
                    spawn(next);
                }
            }, (next / 4) * 4000);
        }
    }

    public void spawn(int index) {
        int currentWave = getCurrentWave();
        for (int i = index; i < (index + 4 > WAVES[currentWave - 1].length ? WAVES[currentWave - 1].length : index + 4); i++) {
            int npcId = WAVES[currentWave - 1][i];
            if (npcId == 15213)
                new TokHaarKetDill(WAVES[currentWave - 1][i], getSpawnTile(i, Loader.getNpc(npcId, Revision.PRE_RS3).size), this);
            else
                new FightKilnMob(WAVES[currentWave - 1][i], getSpawnTile(i, Loader.getNpc(npcId, Revision.PRE_RS3).size), this);
        }
    }

    private int[] getLavaCrystal() {
        switch (getCurrentWave()) {
            case 1:
            case 13:
            case 25:
                return new int[]{23653};
            case 3:
            case 15:
            case 27:
                return new int[]{23654};
            case 5:
            case 18:
            case 29:
                return new int[]{23655};
            case 7:
            case 19:
            case 31:
                return new int[]{23656};
            case 9:
            case 21:
                return new int[]{23657};
            case 11:
            case 23:
                return new int[]{23658};
            case 35:
                return new int[]{23657, 23658};
            default:
                return null;
        }
    }


    public void checkCrystal() {
        if (stage != Stages.RUNNING)
            return;
        if (aliveNPCSCount == 1) {
            int[] crystals = getLavaCrystal();
            if (crystals != null) {
                for (int crystal : crystals) {
                    FloorItemManager.addGroundItem(new Item(crystal), getPosition(32, 32), player);
                }
            }
        }
    }

    public void removeNPC() {
        if (stage != Stages.RUNNING)
            return;
        aliveNPCSCount--;
        if (aliveNPCSCount == 0)
            nextWave();
    }

    public void win() {
        if (stage != Stages.RUNNING)
            return;
        exitCave(4);
    }

    public void unlockPlayer() {
        stage = Stages.RUNNING;
        player.getMovement().unlock();
    }

    public void removeScene() {
        FadingScreen.fade(player, new Runnable() {
            @Override
            public void run() {
                if (stage != Stages.RUNNING)
                    unlockPlayer();
                removeTokHaarTok();
                //player.getPacketSender().sendCameraNeutrality();
                //player.getPacketSender().sendBlackOut(0);
                //player.getPacketSender().sendConfig(1241, 0);
                if (getCurrentWave() == 38) {
                    Integer reward = (Integer) player.getTemporaryAttributes().get("FightKilnReward");
                    if (reward != null)
                        win();
                } else {
                    teleportPlayerToMiddle();
                    setWaveEvent();
                }
            }

        });
    }

    public void teleportPlayerToMiddle() {
        player.setPosition(getPosition(31, 32));
        player.moveTo(player);
        player.getMovement().unlock();
    }

    public void removeTokHaarTok() {
        if (tokHaarHok != null)
            tokHaarHok.deregister();
    }

    public void nextWave() {
        if (stage != Stages.RUNNING)
            return;
        // playMusic();
        int nextWave = getCurrentWave() + 1;
        setCurrentWave(nextWave);
        if (logoutAtEnd) {
            GameWorld.getPlayers().remove(player);
            return;
        } else if (nextWave == 1
                || nextWave == 11
                || nextWave == 21
                || nextWave == 31
                || nextWave == 34
                || nextWave == 37
                || nextWave == 38) {
            harAken = null;
            player.stopAll();
            loadCave(false);
            return;
        }
        setWaveEvent();
    }


    public void setWaveEvent() {
        GameExecutorManager.fastExecutor.schedule(new TimerTask() {

            @Override
            public void run() {
                if (stage != Stages.RUNNING)
                    return;
                startWave();
            }
        }, 6000);
    }


    @Override
    public boolean sendDeath() {
        player.getMovement().lock(7);
        player.stopAll();
        GameWorld.schedule(new PlayerDeath(player, player.copyPosition(), OUTSIDE, false, () -> {
            exitCave(1);
            //player.getPackets().sendMusicEffect(90);
        }));
        return false;
    }


    @Override
    public void magicTeleported(int type) {
        exitCave(2);
    }

    /*
     * logout or not. if didnt logout means lost, 0 logout, 1, normal,  2 tele
     */
    public void exitCave(int type) {
        stage = Stages.DESTROYING;
        Position outside = new Position(OUTSIDE, 2); //radomizes alil
        if (type == 0) {
            player.setPosition(outside);
            player.moveTo(player);
            if (getCurrentWave() == 0) //leaves if didnt start
                removeController();
        } else if (type == 2) {
            player.setPosition(outside);
            player.moveTo(player);
            removeController();
        } else {
            //player.setForceMultiArea(false);
            //player.getPacketSender().closeInterface(player.getInterfaceManager().hasRezizableScreen() ? 11 : 0);
            if (type == 1 || type == 4) {
                player.setPosition(outside);
                player.moveTo(player);
                if (type == 4) {
//                    player.setCompletedFightKiln();
                    player.message("You were victorious!!");
                    Integer reward = (Integer) player.getTemporaryAttributes().get("FightKilnReward");
                    int itemId = reward != null && reward == 1 ? 6571 : 23659;
                    if (player.getInventory().getSpaces() == 0) {
                        FloorItemManager.addGroundItem(new Item(itemId), player.copyPosition(), player);
                    } else {
                        player.getInventory().addSafe(new Item(itemId));
                    }
                    String moof1 = "completed ";
                    String moof2 = "Fight Kiln!"; /*
                    try {
						AdventureLog.createConnection();
						AdventureLog.query("INSERT INTO `adventure` (`username`,`time`,`action`,`monster`) VALUES ('"+player.getUsername()+"','"+AdventureLog.Timer()+"','"+moof1+"', '"+moof2+"');");
						AdventureLog.destroyConnection();
					} catch (SQLException e) {
						e.printStackTrace();
					}
					System.out.println("[SQLMANAGER] Query Executed.");
					AdventureLog.destroyConnection(); */
                }
            }
            player.getInventory().delete(new Item(23653), new Item(23654), new Item(23655), new Item(23656), new Item(23657), new Item(23658));
            removeCrystalEffects();
            removeController();
        }
        /*
         * 1200 delay because of leaving
         */
        GameExecutorManager.slowExecutor.schedule(() -> MapBuilder.destroyMap(boundChuncks[0], boundChuncks[1], 8, 8), 1200, TimeUnit.MILLISECONDS);
    }

    private void removeCrystalEffects() {
        //player.setInvulnerable(false);
        player.getSkills().reset();
        //player.setHpBoostMultiplier(0);
        //player.getEquipment().refreshItems();
        player.getTemporaryAttributes().remove("FightKilnCrystal");
    }

    /*
     * gets worldtile inside the map
     */
    public Position getPosition(int mapX, int mapY) {
        return new Position(boundChuncks[0] * 8 + mapX, boundChuncks[1] * 8 + mapY, 1);
    }


    /*
     * return false so wont remove script
     */
    @Override
    public boolean logout() {
        /*
         * only can happen if dungeon is loading and system update happens
         */
        if (stage != Stages.RUNNING)
            return false;
        exitCave(0);
        return false;

    }

    public int getCurrentWave() {
        if (getArguments() == null || getArguments().length == 0)
            return 0;
        return (Integer) getArguments()[0];
    }

    public void setCurrentWave(int wave) {
        if (getArguments() == null || getArguments().length == 0)
            this.setArguments(new Object[1]);
        getArguments()[0] = wave;
    }

    @Override
    public void forceClose() {
        /*
         * shouldnt happen
         */
        if (stage != Stages.RUNNING)
            return;
        exitCave(2);
    }

    public void showHarAken() {
        if (harAken == null) {
            harAken = new HarAken(15211, getPosition(45, 26), this);
            harAken.getDirection().setDirection(Direction.forID(Utils.getFaceDirection(-1, -1)));
        } else {
            if (stage != Stages.RUNNING)
                return;
            switch (Utils.random(3)) {
                case 0:
                    harAken.setPosition(getPosition(29, 17));
                    harAken.getDirection().setDirection(Direction.forID(Utils.getFaceDirection(0, 1)));
                    break;
                case 1:
                    harAken.setPosition(getPosition(17, 30));
                    harAken.getDirection().setDirection(Direction.forID(Utils.getFaceDirection(1, 0)));
                    break;
                case 2:
                    harAken.setPosition(getPosition(42, 30));
                    harAken.getDirection().setDirection(Direction.forID(Utils.getFaceDirection(-1, 0)));
                    break;
            }
            harAken.moveTo(harAken);
            //World.addNPC(harAken);
            //TODO set worldtile
        }
        harAken.setCantInteract(false);
        harAken.perform(new Animation(16232, Revision.PRE_RS3));
    }

    public static void useCrystal(final Player player, int id) {
        if (!(player.getControllerManager().getController() instanceof FightKiln)
                || player.getTemporaryAttributes().get("FightKilnCrystal") != null)
            return;
        player.getInventory().delete(new Item(id));
        switch (id) {
            case 23653: //invulnerability
                player.getPacketSender().sendMessage("The power of this crystal makes you invulnerable.", 0x7e2217);
                player.getTemporaryAttributes().put("FightKilnCrystal", Boolean.TRUE);
                //player.setInvulnerable(true);
                GameExecutorManager.slowExecutor.schedule(() -> {
                    player.getTemporaryAttributes().remove("FightKilnCrystal");
                    player.getPacketSender().sendMessage("The power of the crystal dwindles and you're vulnerable once more.", 0x7e2217);
                    //player.setInvulnerable(false);
                }, 15, TimeUnit.SECONDS);
                break;
            case 23654: //RESTORATION
                player.getHealth().heal(player.getMaxConstitution());
                player.getSkills().refresh(Skill.PRAYER);
                player.getPacketSender().sendMessage("The power of this crystal heals you fully.", 0x7e2217);
                break;
            case 23655: //MAGIC
                boostCrystal(player, Skill.MAGIC);
                break;
            case 23656: //RANGED
                boostCrystal(player, Skill.RANGED);
                break;
            case 23657: //STRENGTH
                boostCrystal(player, Skill.STRENGTH);
                break;
            case 23658: //CONSTITUTION
                player.getTemporaryAttributes().put("FightKilnCrystal", Boolean.TRUE);
                player.getEffects().startEffect(new Effect(Effects.CONSTITUTION_CRYSTAL, 350));
                player.getHealth().heal(player.getSkills().getLevel(Skill.CONSTITUTION) * 5);
                player.getPacketSender().sendMessage("The power of this crystal improves your Constitution.", 0x7e2217);
                break;
        }
    }

    private static void boostCrystal(final Player player, final Skill skill) {
        player.getTemporaryAttributes().put("FightKilnCrystal", Boolean.TRUE);
        if (skill == Skill.RANGED)
            player.getPacketSender().sendMessage("The power of the crystal improves your Ranged prowess, at the expense of your Defence, Strength and Magical ability.", 0x7e2217);
        else if (skill == Skill.MAGIC)
            player.getPacketSender().sendMessage("The power of the crystal improves your Magic prowess, at the expense of your Defence, Strength and Ranged ability.", 0x7e2217);
        else if (skill == Skill.STRENGTH)
            player.getPacketSender().sendMessage("The power of the crystal improves your Strength prowess, at the expense of your Defence, Ranged and Magical ability.", 0x7e2217);
        GameExecutorManager.fastExecutor.getExecutor().schedule(new TimerTask() {

            private int count;

            @Override
            public void run() {
                try {
                    if (count++ == 7 || !(player.getControllerManager().getController() instanceof FightKiln)) {
                        player.getTemporaryAttributes().remove("FightKilnCrystal");
                        player.getPacketSender().sendMessage("The power of the crystal dwindles and your " + skill.getName() + " prowess returns to normal.", 0x7e2217);
                        player.getSkills().reset();
                        cancel();
                    } else {
                        for (Skill s : Skill.combatSkills) {
                            if (skill == s)
                                continue;
                            player.getSkills().buff(s, 0.5);
                        }
                        player.getSkills().buff(skill, 1.5);
                    }
                } catch (Throwable e) {
                    Logger.handle(e);
                }
            }
        }, 0, 30000);
    }

    @Override
    public boolean processNPCClick1(Mob mob) {
        if (mob.getId() == 15195 && getCurrentWave() == 0) {
            player.getDialogueManager().startDialogue(new TokHaarHokD(), 0, TOKHAAR_HOK, this);
            return false;
        }
        return true;

    }

    @Override
    public void process() {
        if (harAken != null)
            harAken.process();
    }

    @Override
    public boolean processMagicTeleport(Position toTile) {
        player.getDialogueFactory().sendNpc(Loader.getNpc(TOKHAAR_HOK, Revision.PRE_RS3), "You cannot teleport out of the fight kiln!");
        return false;
    }

    @Override
    public boolean processItemTeleport(Position toTile) {
        player.getDialogueFactory().sendNpc(Loader.getNpc(TOKHAAR_HOK, Revision.PRE_RS3), "You cannot teleport out of the fight kiln!");
        return false;
    }

    @Override
    public boolean processObjectTeleport(Position toTile) {
        player.getDialogueFactory().sendNpc(Loader.getNpc(TOKHAAR_HOK, Revision.PRE_RS3), "You cannot teleport out of the fight kiln!");
        return false;
    }

    public void hideHarAken() {
        if (stage != Stages.RUNNING)
            return;
        harAken.resetTimer();
        harAken.setCantInteract(true);
        harAken.perform(new Animation(16234, Revision.PRE_RS3));
        GameExecutorManager.fastExecutor.schedule(new TimerTask() {

            @Override
            public void run() {
                if (stage != Stages.RUNNING)
                    return;
                harAken.deregister();
            }
        }, 3000);
    }

    //public List<Position> inhabitedPositions = new CopyOnWriteArrayList<Position>();

    /*@Override
    public boolean checkOffsetWalkStep(int lastX, int lastY, int nextX, int nextY) {
        Position to = new Position(nextX, nextY, 1);
        Position last = new Position(lastX, lastY, 1);
        if(inhabitedPositions.size() == 0) {
            inhabitedPositions.add(to);
            return true;
        }
        for(Position pos : inhabitedPositions) {
            if(last == pos) {
                inhabitedPositions.remove(last);
            }
            if(pos == to)
                return false;
        }
        inhabitedPositions.add(to);
        return true;
    }*/


}

