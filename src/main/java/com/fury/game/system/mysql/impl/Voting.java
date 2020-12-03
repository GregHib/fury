package com.fury.game.system.mysql.impl;

import com.fury.core.model.node.entity.actor.figure.player.Player;
import com.fury.game.GameSettings;
import com.fury.game.content.global.Achievements;
import com.fury.game.content.global.Achievements.AchievementData;
import com.fury.core.model.item.Item;
import com.fury.game.system.files.logs.PlayerLogs;
import com.fury.game.system.mysql.motivote.MotivoteHandler;
import com.fury.game.system.mysql.motivote.Vote;
import com.fury.game.world.FloorItemManager;
import com.fury.game.world.GameWorld;
import com.fury.game.world.World;
import com.fury.util.FontUtils;
import com.fury.util.Misc;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

public class Voting extends MotivoteHandler<Vote> {

    public static Queue<Player> voteRewards = new ConcurrentLinkedQueue<>();

    private static int VOTES;

    @Override
    public void onCompletion(Vote reward) {
        Player p = World.getPlayerByName(Misc.formatText(reward.username()));
        if (p != null) {
            reward.complete();
			
		/*	if(reward.ip() != null && !reward.ip().equals(p.getIpAddress())) {
				p.message("<col=900000>Warning! Our anti-cheat system has detected an invalid vote for your account.");
				PlayerLogs.log(p.getUsername(), "Anti-cheat system detected invalid vote attempt!");
			} else  {*/
            voteRewards.add(p); //Needs to be synchronized with game tick
            //	}
        }
    }

    public static void sequence() {
        for (int i = 0; i < GameSettings.VOTE_REWARDING_THRESHOLD; i++) {
            Player player = voteRewards.poll();
            if (player == null)
                break;
            Voting.handleQueuedReward(player);
        }
    }

    public static void handleQueuedReward(Player player) {
        player.getPacketSender().sendInterfaceRemoval();
		player.message("Thanks for voting, enjoy your reward! Make sure to vote every 12 hours!");
        if (player.isInDungeoneering() || player.getDungManager().isInside()) {
            player.getBank().tab().add(new Item(20712));
            player.message("Your reward has been placed into your bank.");
        } else {
            if (player.getInventory().getSpaces() >= 1) {
                player.getInventory().add(new Item(20712));
            } else {
                if (player.getGameMode().isIronMan()) {
                    FloorItemManager.addGroundItem(new Item(20712), player.copyPosition(), player);
                } else {
                    player.getBank().tab().add(new Item(20712));
                    player.message("Your reward has been placed into your bank.");
                }
            }
        }
        Achievements.doProgress(player, AchievementData.VOTE_100_TIMES);
        PlayerLogs.log(player.getUsername(), "Player received vote reward!");
        if (VOTES >= 10) {
            GameWorld.sendBroadcast(FontUtils.imageTags(535) + " Another " + FontUtils.add("10", 0x800000) + " votes have been claimed! Vote now using the " + FontUtils.add("::vote", 0x800000) + " command!", 0x008fb2);
            VOTES = 0;
        }
        VOTES++;
    }
}