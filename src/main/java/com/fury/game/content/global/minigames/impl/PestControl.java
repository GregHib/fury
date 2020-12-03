package com.fury.game.content.global.minigames.impl;

import com.fury.cache.Revision;
import com.fury.cache.def.Loader;
import com.fury.cache.def.npc.NpcDefinition;
import com.fury.core.model.node.entity.actor.figure.Figure;
import com.fury.core.model.node.entity.actor.figure.player.Player;
import com.fury.engine.task.executor.GameExecutorManager;
import com.fury.game.content.controller.impl.PestControlGame;
import com.fury.game.content.dialogue.Expressions;
import com.fury.game.content.dialogue.impl.npcs.SimpleNpcMessageD;
import com.fury.game.content.global.Achievements;
import com.fury.core.model.node.entity.actor.figure.mob.Mob;
import com.fury.game.entity.character.player.info.DonorStatus;
import com.fury.game.node.entity.actor.figure.player.Points;
import com.fury.game.node.entity.actor.object.ObjectHandler;
import com.fury.game.npc.minigames.pest.*;
import com.fury.game.world.GameWorld;
import com.fury.game.world.World;
import com.fury.game.world.map.Position;
import com.fury.game.world.map.build.MapBuilder;
import com.fury.util.FontUtils;
import com.fury.util.Logger;
import com.fury.util.Misc;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class PestControl {

    private final static int[][] PORTAL_LOCATIONS = { { 4, 56, 45, 21, 32 }, { 31, 28, 10, 9, 32 } };
    private final static int[] KNIGHT_IDS = { 3782, 3784, 3785 };

    private int[] boundChunks;
    private int[] pestCounts = new int[5];

    private List<Player> team;
    private List<Mob> brawlers = new LinkedList<>();
    private PestPortal[] portals = new PestPortal[4];
    private List<Integer> portalOrder;
    private Set<PestPortal> deadPortals = Collections.newSetFromMap(new ConcurrentHashMap<>());
    private Consumer<Figure> portalDeath = figure -> {
        deadPortals.add((PestPortal) figure);
        System.out.println("@@@ Portal has died! [Dead: "+deadPortals.size()+"] @@@");
    };

    private PestPortal knight;
    private PestData data;

    private byte lockedPortals = 5;

    private class PestGameTimer extends TimerTask {

        private int seconds = 1200;

        @Override
        public void run() {
            try {
                updateTime(seconds / 60);
                if (seconds == 0 || canFinish()) {
                    endGame();
                    cancel();
                    return;
                }
                if (seconds % 10 == 0)
                    sendPortalInterfaces();
                if (seconds % 20 == 0)
                    unlockPortal();
                seconds--;
            } catch (Throwable e) {
                Logger.handle(e);
            }
        }
    }

    public PestControl(List<Player> team, PestData data) {
        this.team = Collections.synchronizedList(team);
        this.data = data;
        portalOrder = new ArrayList<>();
        for(int i = 0; i < portals.length; i++)
            portalOrder.add(i);
        Collections.shuffle(portalOrder);
    }

    public static void instantiate(List<Player> playerList, Lander.LanderRequirement landerRequirement) {
        new PestControl(playerList, PestControl.PestData.valueOf(landerRequirement.name())).create();
    }

    public PestControl create() {
        final PestControl instance = this;
        GameExecutorManager.slowExecutor.execute(() -> {
            try {
                boundChunks = MapBuilder.findEmptyChunkBound(8, 8);
                MapBuilder.copyAllPlanesMap(328, 320, boundChunks[0], boundChunks[1], 8);
                sendBeginningWave();
                for (Player player : team) {
                    player.getControllerManager().removeControllerWithoutCheck();
                    ObjectHandler.useStairs(player, -1, getWorldTile(35 - Misc.random(4), 54 - (Misc.random(3))), 1, 2);
                    player.getControllerManager().startController(new PestControlGame(), instance);
                    player.getDialogueManager().sendNPCDialogue(knight.getId(), Expressions.VERY_ANGRY, "You must defend the Void Knight while the portals", "are unsummoned. The ritual takes twenty minutes though,", "so you can help out by destroying them yourselves!", "Now GO GO GO!");
                }
                GameExecutorManager.fastExecutor.getExecutor().schedule(new PestGameTimer(), 1000, 1000);
            } catch (Throwable e) {
                Logger.handle(e);
            }
        });
        return instance;
    }

    private void sendBeginningWave() {
        knight = new PestPortal(KNIGHT_IDS[Misc.random(KNIGHT_IDS.length)], getWorldTile(32, 32), this, knight -> {});
        knight.dropShield();
        for (int index = 0; index < portals.length; index++) {
            PestPortal portal = portals[index] = new PestPortal(6146 + index, getWorldTile(PORTAL_LOCATIONS[0][index], PORTAL_LOCATIONS[1][index]), this, portalDeath);
            portal.getHealth().setHitpoints(data.ordinal() == 0 ? 2000 : 2500);
        }
    }

    public boolean createPestNpc(int index) {
        if (pestCounts[index] >= (index == 4 ? 4 : (portals[index] != null && portals[index].isShieldDown()) ? 5 : 15))
            return false;
        pestCounts[index]++;
        Position baseTile = getWorldTile(PORTAL_LOCATIONS[0][index], PORTAL_LOCATIONS[1][index]);
        Position teleTile = baseTile;
        int npcId = index == 4 ? data.getShifters()[Misc.random(data.getShifters().length)] : data.getPests()[Misc.random(data.getPests().length)];
        NpcDefinition defs = Loader.getNpc(npcId, Revision.RS2);
        for (int trycount = 0; trycount < 10; trycount++) {
            teleTile = new Position(baseTile, 5);
            if (World.isTileFree(teleTile.getX(), teleTile.getY(), baseTile.getZ(), defs.size))
                break;
            teleTile = baseTile;
        }
        String name = defs.getName().toLowerCase();
        if (name.contains("shifter"))
            new Shifter(npcId, teleTile, true, index, this);
        else if (name.contains("splatter"))
            new Splatter(npcId, teleTile, true, index, this);
        else if (name.contains("spinner"))
            new Spinner(npcId, teleTile, true, index, this);
        else if (name.contains("brawler"))
            brawlers.add(new PestMonsters(npcId, teleTile, true, index, this));
        else
            new PestMonsters(npcId, teleTile, true, index, this);
        return true;
    }

    public void endGame() {
        final List<Player> team = new LinkedList<>();
        team.addAll(this.team);
        this.team.clear();
        for (final Player player : team) {
            final int zeal = (int) ((PestControlGame) player.getControllerManager().getController()).getPoints();
            final int deaths = ((PestControlGame) player.getControllerManager().getController()).getDeaths();
            player.getControllerManager().forceStop();
            GameWorld.schedule(1, () -> distributeReward(player, zeal, deaths));
        }

        GameExecutorManager.slowExecutor.schedule(() -> {
            MapBuilder.destroyMap(boundChunks[0], boundChunks[1], 8, 8);
            boundChunks = null;
        }, 1200, TimeUnit.MILLISECONDS);
    }

    private void distributeReward(Player player, int knightZeal, int deaths) {
        if (knight.isDead())
            player.getDialogueManager().startDialogue(new SimpleNpcMessageD(), 3802, "You failed to protect the void knight", "and have not been awarded any points.");
        else if (knightZeal < 500)
            player.getDialogueManager().startDialogue(new SimpleNpcMessageD(), 3802, "The knights notice your lack of zeal in that", "battle and have not presented you with any points.");
        else {
            player.getDialogueManager().startDialogue(new SimpleNpcMessageD(), 3802, "Congratulations! You managed to destroy all the portals!", "We've awarded you " + data.getReward() + " Void Knight Commendation", "points. Please also accept these coins as a reward.");
            int points = data.getReward();

            if(DonorStatus.isDonor(player, DonorStatus.SAPPHIRE_DONOR))
                points += 1;

            if(DonorStatus.isDonor(player, DonorStatus.RUBY_DONOR))
                points += 1;

            if(DonorStatus.isDonor(player, DonorStatus.ONYX_DONOR))
                points += 1;

            player.getPoints().add(Points.COMMENDATIONS, points);
            int amount = (knightZeal - (deaths * 100)) * 10;

            if(DonorStatus.isDonor(player, DonorStatus.DRAGONSTONE_DONOR))
                amount *= 1.5;
            else if(DonorStatus.isDonor(player, DonorStatus.ONYX_DONOR))
                amount *= 2;

            if(amount > 0)
                player.getInventory().addCoins(amount);
        }
        Achievements.finishAchievement(player, Achievements.AchievementData.FINISH_A_GAME_OF_PEST_CONTROL);
        if(deaths == 0)
            Achievements.finishAchievement(player, Achievements.AchievementData.FINISH_PEST_CONTROL_WITHOUT_DEATH);
    }

    private void sendPortalInterfaces() {
        for (Player player : team) {
            for (int count = 0; count < portals.length; count++) {
                PestPortal npc = portals[count];
                if (npc != null)
                    player.getPacketSender().sendString(21111 + count, FontUtils.add(npc.getHealth().getHitpoints() + "", npc.getHealth().getHitpoints() == 0 ? 0xf80000 : 0x00f800));
            }
            player.getPacketSender().sendString(21115, FontUtils.add(knight.getHealth().getHitpoints() + "", knight.getHealth().getHitpoints() == 0 ? 0xf80000 : 0x00f800));
        }
    }

    public void unlockPortal() {
        if(!portalOrder.isEmpty()) {
            PestPortal portal = portals[portalOrder.get(0)];
            portal.dropShield();
            portalOrder.remove(0);
        }
    }

    public boolean isBrawlerAt(Position tile) {
        for (Iterator<Mob> it = brawlers.iterator(); it.hasNext();) {
            Mob mob = it.next();
            if (mob.isDead() || mob.getFinished()) {
                it.remove();
                continue;
            }
            if (mob.getX() == tile.getX() && mob.getY() == tile.getY() && tile.getZ() == tile.getZ())
                return true;
        }
        return false;
    }

    private void updateTime(int minutes) {
        for (Player player : team)
            player.getPacketSender().sendString(21117, minutes + " min");
    }

    public void sendTeamMessage(String message) {
        for (Player player : team)
            player.message(message, true);
    }

    private boolean canFinish() {
        if (knight == null || knight.isDead())
            return true;

        System.out.println("Can Finish: " + deadPortals.size());

        if(deadPortals.size() == 4)
            return true;

        return lockedPortals == 0;
    }

    public Position getWorldTile(int mapX, int mapY) {
        if (boundChunks == null) // temporary fix..
            return new Position(2657, 2639);
        return new Position(boundChunks[0] * 8 + mapX, boundChunks[1] * 8 + mapY, 0);
    }

    public PestPortal[] getPortals() {
        return portals;
    }

    public List<Player> getPlayers() {
        return team;
    }

    public Mob getKnight() {
        return knight;
    }

    public enum PestData {
        NOVICE(new int[] {
                3732, 3733, 3734, 3735, // Shifters
                3742, 3743, 3744, // Ravagers
                3772, 3773, // Brawler
                3727, 3728, 3729, // Splatter
                3747, 3748, 3749, // Spinner
                3752, 3753, 3754, 3755, // Torcher
                3762, 3763, 3764, 3765 // Defiler
        },
                new int[] { 3732, 3733, 3734, 3735 }, 2),
        INTERMEDIATE(new int[] {
                3734, 3735, 3736, 3737, 3738, 3739,// Shifters
                3744, 3743, 3745, // Ravagers
                3773, 3775, 3776, // Brawler
                3728, 3729, 3730, // Splatter
                3748, 3749, 3750, 3751,// Spinner
                3754, 3755, 3756, 3757, 3758, 3759,  // Torcher
                3764, 3765, 3766, 3768, 3769// Defiler
        },
                new int[] { 3734, 3735, 3736, 3737, 3738, 3739 }, 3),
        VETERAN(new int[] {
                3736, 3737, 3738, 3739, 3740, 3741, // Shifters
                3744, 3745, 3746, // Ravagers
                3776, 3774, // Brawler
                3729, 3730, 3731, // Splatter
                3749, 3750, 3751, // Spinner
                3758, 3759, 3760, 3761, // Torcher
                3770, 3771// Defiler
        }, new int[] { 3736, 3737, 3738, 3739, 3740, 3741 }, 4);

        private int[] waves, shifters;
        private int reward;

        PestData(int[] pests, int[] shifters, int reward) {
            this.waves = pests;
            this.shifters = shifters;
            this.reward = reward;
        }

        public int[] getShifters() {
            return shifters;
        }

        public int[] getPests() {
            return waves;
        }

        public int getReward() {
            return reward;
        }
    }

    public int[] getPestCounts() {
        return pestCounts;
    }

    public PestData getPestData() {
        return data;
    }

    public int getPortalCount() {
        return lockedPortals;
    }
}
