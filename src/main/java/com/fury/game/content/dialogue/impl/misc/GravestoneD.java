package com.fury.game.content.dialogue.impl.misc;

import com.fury.game.content.misc.objects.GravestoneSelection;
import com.fury.game.content.dialogue.Dialogue;
import com.fury.game.content.global.Achievements;
import com.fury.game.entity.character.player.info.DonorStatus;

public class GravestoneD extends Dialogue {
    private GravestoneSelection.GraveType grave;
    @Override
    public void start() {
        int father = 456;
        int graveIndex = (int) parameters[0];
        grave = GravestoneSelection.getGrave(graveIndex);

        if(grave.ordinal() >= GravestoneSelection.GraveType.SARADOMIN.ordinal() && grave.ordinal() <= GravestoneSelection.GraveType.ANCIENT.ordinal()) {//6 - 11
            DonorStatus status = DonorStatus.values()[grave.ordinal() - GravestoneSelection.GraveType.SARADOMIN.ordinal() + 1];
            if(!DonorStatus.isDonor(player, status)) {
                sendNpc(father, "You need to be a " + status.getName(), "donor in order to buy this gravestone.");
                stage = -2;
                return;
            }
        }

        if(grave == GravestoneSelection.GraveType.DWARVEN) {
            for (Achievements.AchievementData d : Achievements.AchievementData.values()) {
                if (!player.getAchievementAttributes().getCompletion()[d.ordinal()]) {
                    sendNpc(father, "You must have completed all", "achievements in order to buy this gravestone.");
                    stage = -2;
                    return;
                }
            }
        }

        if(grave == GravestoneSelection.GraveType.DWARVEN || grave == GravestoneSelection.GraveType.ANGEL) {
            if (!player.getSkills().isMaxed()) {
                sendNpc(father, "You must have maximum level in all", "skills in order to buy this gravestone.");
                stage = -2;
                return;
            }
        }

        sendNpc(father, "Buying this grave will replace your current gravestone.", "If you want to change back you will pay again.", "Are you sure you want to continue?");
    }

    @Override
    public void run(int optionId) {
        if(stage == -1) {
            sendOptions("Yes", "No");
            stage = 0;
        } else if(stage == 0) {
            if(optionId == OPTION_1) {
                if(player.getInventory().removeCoins(grave.getPrice(), "buy this gravestone")) {
                    player.setGravestone(grave.ordinal());
                    player.message("You have successfully bought a new gravestone!");
                }
            }
            end();
        } else
            end();
    }
}
