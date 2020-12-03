package com.fury.game.content.global.minigames.impl;

import com.fury.core.model.node.entity.actor.figure.player.Player;
import com.fury.engine.task.executor.GameExecutorManager;
import com.fury.game.content.controller.impl.PestControlLobby;
import com.fury.game.content.dialogue.impl.misc.SimpleMessageD;
import com.fury.game.node.entity.actor.object.ObjectHandler;
import com.fury.game.world.map.Position;
import com.fury.util.Logger;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.TimerTask;

public class Lander {

    public static Lander[] landers = new Lander[3];
    private static final int AUTO_GAME = 10;
    private static final int LANDER_TIME = 30;

    private List<Player> byStanders = Collections.synchronizedList(new LinkedList<Player>());
    private LobbyTimer timer;
    private LanderRequirement landerRequirement;

    public Lander(LanderRequirement landerRequirement) {
        this.landerRequirement = landerRequirement;
    }

    public class LobbyTimer extends TimerTask {

        private int seconds = LANDER_TIME;

        @Override
        public void run() {
            try {
                if (seconds == 0 && byStanders.size() >= 5 || byStanders.size() >= AUTO_GAME)
                    begin();
                else if (seconds == 0)
                    seconds = LANDER_TIME;
                else if (byStanders.size() == 0) {
                    cancel();
                    return;
                }
                seconds--;
                if (seconds % 30 == 0)
                    refresh();
            } catch (Throwable e) {
                Logger.handle(e);
            }
        }

        public int getSeconds() {
            return seconds;
        }
    }

    private void begin() {
        final List<Player> playerList = new LinkedList<>();
        playerList.addAll(Collections.synchronizedList(byStanders));
        byStanders.clear();
        if (playerList.size() > AUTO_GAME) {
            for (int index = AUTO_GAME; index < playerList.size(); index++) {
                Player player = playerList.get(index);
                if (player == null) {
                    playerList.remove(index);
                    continue;
                }
                player.message("You have received priority over other players.");
                playerList.remove(index);
                byStanders.add(player);
            }
        }
        PestControl.instantiate(playerList, landerRequirement);
    }

    public void enter(Player player) {
        if (byStanders.size() == 0)
            GameExecutorManager.fastExecutor.getExecutor().schedule(timer = new LobbyTimer(), 1000, 1000);
        player.getControllerManager().startController(new PestControlLobby(), landerRequirement.ordinal());
        add(player);
        ObjectHandler.useStairs(player, -1, landerRequirement.getWorldTile(), 1, 2, "You board the lander.");
    }

    public void exit(Player player, boolean logout) {
        if (logout)
            player.moveTo(landerRequirement.getExitTile());
        else
            ObjectHandler.useStairs(player, 828, landerRequirement.getExitTile(), 1, 2, "You leave the lander.");
        remove(player);
    }

    private void refresh() {
        for (Player teamPlayer : byStanders)
            teamPlayer.getControllerManager().getController().sendInterfaces();
    }

    public void add(Player player) {
        byStanders.add(player);
        refresh();
    }

    public void remove(Player player) {
        byStanders.remove(player);
        refresh();
    }

    public List<Player> getByStanders() {
        return byStanders;
    }

    public static Lander[] getLanders() {
        return landers;
    }

    public static enum LanderRequirement {

        NOVICE(40, new Position(2661, 2639), new Position(2657, 2639)),

        INTERMEDIATE(70, new Position(2641, 2644), new Position(2644, 2644)),

        VETERAN(100, new Position(2635, 2653), new Position(2638, 2653));

        public static LanderRequirement forId(int id) {
            for (LanderRequirement reqs : LanderRequirement.values()) {
                if (reqs.ordinal() == id)
                    return reqs;
            }
            return null;
        }

        private int requirement;
        private int[] pests;
        private Position tile, exit;

        private LanderRequirement(int requirement, Position tile, Position exit) {
            this.requirement = requirement;
            this.tile = tile;
            this.exit = exit;
        }

        public int getRequirement() {
            return requirement;
        }

        public Position getWorldTile() {
            return tile;
        }

        public Position getExitTile() {
            return exit;
        }
    }

    public LanderRequirement getLanderRequirement() {
        return landerRequirement;
    }

    static {
        for (int i = 0; i < landers.length; i++)
            landers[i] = new Lander(LanderRequirement.forId(i));
    }

    @Override
    public String toString() {
        return landerRequirement.name().toLowerCase();
    }

    public static boolean canEnter(Player player, int landerIndex) {
        Lander lander = landers[landerIndex];
        if (player.getSkills().getCombatLevel() < lander.getLanderRequirement().requirement) {
            player.getDialogueManager().startDialogue(new SimpleMessageD(), "You need a combat level of "
                    + lander.getLanderRequirement().getRequirement() + " or more to enter in boat.");
            return false;
        } else if (player.getPet() != null || player.getFamiliar() != null) {
            player.message("You can't take a follower into the lander, there isn't enough room!");
            return false;
        }
        lander.enter(player);
        return true;
    }

    public LobbyTimer getTimer() {
        return timer;
    }
}
