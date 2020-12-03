package com.fury.game.content.global.minigames.impl;

import com.fury.game.content.global.minigames.MinigameAttributes;
import com.fury.game.content.skill.Skill;
import com.fury.core.model.node.entity.actor.figure.player.Player;
import com.fury.core.model.item.Item;
import com.fury.game.entity.object.GameObject;
import com.fury.game.network.packet.out.WalkableInterface;

/**
 * Created by Chris on 10/13/2016.
 */
public class AllFiredUp {

    public static boolean handleObject(final Player player, GameObject object) {
        if(object.getId() >= 38448 && object.getId() <= 38461) {
            transformBeacon(player, object.getId() - 38448);
            return true;
        }
        return false;
    }

    private static void transformBeacon(final Player player, int beaconId) {
        MinigameAttributes.AllFiredUpAttributes AFU = player.getMinigameAttributes().getAllFiredUpAttributes();
        int[][] beaconData = AFU.beaconData;
        if(hasBeaconRequirements(player, true) && !hasFinished(player) && getBeaconState(player, beaconId) == 0) {
            int acceptableLogs[] = player.getMinigameAttributes().getAllFiredUpAttributes().acceptableLogs;
            for (int i = 0; i < acceptableLogs.length; i++) {
                if (player.getInventory().contains(new Item(acceptableLogs[i])) && player.getInventory().getAmount(new Item(acceptableLogs[i])) >= 20)
                    player.getInventory().delete(new Item(acceptableLogs[i], 20));
            }
            player.getPacketSender().sendConfig(beaconData[beaconId][2], 1 << beaconData[beaconId][3]);
            beaconData[beaconId][1]++;
        } else if(getBeaconState(player, beaconId) == 1 && hasBeaconRequirements(player, false) && !hasFinished(player)) {
            player.getPacketSender().sendConfig(beaconData[beaconId][2], 1 << beaconData[beaconId][4]);
            beaconData[beaconId][1]++;
            if(AFU.beaconsLeft == 14) {
                AFU.started = true;
                playerInstance = player;
            }
            AFU.beaconsLeft--;
        }
        if(hasFinished(player) && AFU.timer > 0) {
            player.message("Congratulations, You have completed the All Fired Up Minigame!");
            handleRewards(player, AFU.timer);
            AFU.timer = 0;
            player.getPacketSender().sendInterfaceRemoval();
        }
    }

    private static boolean hasBeaconRequirements(final Player player, boolean needsLogs) {
        boolean hasLogs = false;
        boolean hasLevel = false;
        if (needsLogs) {
            int acceptableLogs[] = player.getMinigameAttributes().getAllFiredUpAttributes().acceptableLogs;
            for (int i = 0; i < acceptableLogs.length; i++) {
                if (player.getInventory().contains(new Item(acceptableLogs[i])) && player.getInventory().getAmount(new Item(acceptableLogs[i])) >= 20)
                    hasLogs = true;
            }
        }
        if (player.getSkills().getLevel(Skill.FIREMAKING) >= 92)
            hasLevel = true;
        else
            player.message("You need a firemaking level of 92 to light this beacon!");
        if (!hasLogs && needsLogs)
            player.message("You do not have the required amount of logs for this beacon!");
        return needsLogs ? ((hasLogs && hasLevel)) : (hasLevel);
    }

    public static int getBeaconState(Player player, int beaconId) {
        int[][] beaconData = player.getMinigameAttributes().getAllFiredUpAttributes().beaconData;
        return beaconData[beaconId][1];
    }

    public static boolean hasFinished(final Player player) {
        int[][] beaconData = player.getMinigameAttributes().getAllFiredUpAttributes().beaconData;
        for(int ID = 0; ID < beaconData.length; ID++)
            if(beaconData[ID][1] != 2)
                return false;
        return true;
    }

    public static boolean handleInterface(Player player) {
        int timeInTicks = (int) player.getMinigameAttributes().getAllFiredUpAttributes().timer;
        int minuteNum = 0;
        int secondsRemaining = 0;
        if(timeInTicks >= 60){
            minuteNum = timeInTicks / 60;
            secondsRemaining = timeInTicks - minuteNum * 60;
        } else {
            minuteNum = 0;
            secondsRemaining = timeInTicks;
        }
        player.getPacketSender().sendString(34234, "Time Left: "+minuteNum+"M "+secondsRemaining+"S ");
        player.getPacketSender().sendString(34235, "Beacons Left: " + player.getMinigameAttributes().getAllFiredUpAttributes().beaconsLeft);
        if(player.getWalkableInterfaceId() != 12312)
            player.send(new WalkableInterface(12312));
        return true;
    }

    static Player playerInstance;

    public static void sequence() {
        if(playerInstance != null) {
            MinigameAttributes.AllFiredUpAttributes AFU = playerInstance.getMinigameAttributes().getAllFiredUpAttributes();
            if (AFU.timer == 0) {
                if(AFU.beaconsLeft > 0)
                    handleFailedAttempt(playerInstance);
                AFU.started = false;
                AFU.timer = 540;
                playerInstance.getPacketSender().sendInterfaceRemoval();
            }
            if (AFU.started && AFU.timer > 0) {
                AFU.timer -= 0.6;//ok that should be good
                handleInterface(playerInstance);
            }
        }
    }

    public static void handleFailedAttempt(Player player) {
        player.message("You have failed to light all the beacons within nine minutes.");
        resetBeacons(player);
    }

    public static void resetBeacons(Player player) {
        int[][] beaconData = player.getMinigameAttributes().getAllFiredUpAttributes().beaconData;
        for(int i = 0; i <= 13; i++) {
            beaconData[i][1] = 0;
            player.getPacketSender().sendConfig(beaconData[i][2], 1 >> beaconData[i][4]);
        }
        player.getMinigameAttributes().getAllFiredUpAttributes().beaconsLeft = 14;
    }

    public static void handleRewards(Player player, double remaining) {
        if (hasFinished(playerInstance)) {
            if (remaining >= 270) {
                player.getSkills().addExperience(Skill.FIREMAKING, 50000);
                //player.getInventory().add(13661, 1);
            }
        }
    }
}
