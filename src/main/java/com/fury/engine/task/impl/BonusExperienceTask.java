package com.fury.engine.task.impl;

import com.fury.core.task.Task;
import com.fury.core.model.node.entity.actor.figure.player.Player;
import com.fury.game.entity.character.player.content.PlayerPanel;
import com.fury.game.world.GameWorld;
import com.fury.util.Colours;
import com.fury.util.FontUtils;

public class BonusExperienceTask extends Task {

	public BonusExperienceTask(final Player player) {
		super(false, 100);this.player = player;
	}

    final Player player;
    int msg;

    @Override
    public void run() {
        if (player == null || !player.isRegistered()) {
            stop();
            return;
        }
        player.setMinutesBonusExp(-1, true);
        int newMinutes = player.getMinutesBonusExp();
        if (newMinutes < 0) {
            player.getPacketSender().sendMessage(FontUtils.imageTags(535) + " Your bonus experience has run out.", Colours.BLUE);
            player.setMinutesBonusExp(-1, false);
            stop();
        } else if (newMinutes == 10 || newMinutes == 5) {
            player.getPacketSender().sendMessage(FontUtils.imageTags(535) + " You have " + player.getMinutesBonusExp() + " minutes of bonus experience left.", Colours.BLUE);
        } else if (newMinutes == 1) {
            player.getPacketSender().sendMessage(FontUtils.imageTags(535) + " You have " + player.getMinutesBonusExp() + " minute of bonus experience left!", Colours.BLUE);
        }
        PlayerPanel.refreshPanel(player);
        msg++;
    }

	public static void addBonusXp(final Player player, int minutes) {
		boolean startEvent = player.getMinutesBonusExp() == -1;
		int bonus = startEvent ? minutes + 1 : minutes;
		bonus = (bonus  * 1000) / 600;
		player.setMinutesBonusExp(bonus, true);
		player.getPacketSender().sendMessage(FontUtils.imageTags(535) + " You gain a bonus " + minutes + " minutes bonus exp starting now. "+player.getMinutesBonusExp()+" minutes left.", Colours.BLUE);
		if(startEvent) {
			GameWorld.schedule(new Task( 100) {
                int msg;

                @Override
                public void run() {
                    if (!player.isRegistered()) {
                        stop();
                        return;
                    }
                    player.setMinutesBonusExp(-1, true);
                    int newMinutes = player.getMinutesBonusExp();
                    if (newMinutes < 0) {
                        player.getPacketSender().sendMessage(FontUtils.imageTags(535) + " Your bonus experience has run out.", Colours.BLUE);
                        player.setMinutesBonusExp(-1, false);
                        stop();
                    } else if (newMinutes == 10 || newMinutes == 5) {
                        player.getPacketSender().sendMessage(FontUtils.imageTags(535) + " You have " + player.getMinutesBonusExp() + " minutes of bonus experience left.", Colours.BLUE);
                    } else if (newMinutes == 1) {
                        player.getPacketSender().sendMessage(FontUtils.imageTags(535) + " You have " + player.getMinutesBonusExp() + " minute of bonus experience left!", Colours.BLUE);
                    }
                    PlayerPanel.refreshPanel(player);
                    msg++;
                }
            });
		}
	}
}
