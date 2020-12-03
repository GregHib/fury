package com.fury.game.content.dialogue.impl.npcs;

import com.fury.game.GameSettings;
import com.fury.game.container.impl.shop.ShopManager;
import com.fury.game.content.dialogue.Expressions;
import com.fury.game.content.dialogue.DialogueManager;
import com.fury.game.content.dialogue.Dialogue;
import com.fury.game.content.global.Achievements;

/**
 * Created by Greg on 14/11/2016.
 */
public class KingHealthorgD extends Dialogue {
    int kingHealthorg = 4646;

    @Override
    public void start() {
        player.getDialogueManager().sendOptionsDialogue(DialogueManager.DEFAULT_OPTIONS_TITLE, "I'd like to view the Vote Rewards Store", "Tell me more about voting", "Cancel");
    }

    @Override
    public void run(int optionId) {
        if(stage == -1) {
            Achievements.finishAchievement(player, Achievements.AchievementData.TALK_TO_KING_HEALTHORG);
            if(optionId == DialogueManager.OPTION_2) {
                player.getDialogueManager().sendNPCDialogue(kingHealthorg, Expressions.NORMAL, "Every 12 hours you can vote on various websites.", "Every site you vote on you will receive a vote book.");
                stage = 0;
            } else {
                end();
                if(optionId == DialogueManager.OPTION_1)
                    ShopManager.getShops().get(player.getGameMode().isIronMan() ? 120 : 27).open(player);
            }
        } else if(stage == 0) {
            player.getDialogueManager().sendNPCDialogue(kingHealthorg, Expressions.NORMAL, "Vote books can be opened for temporary experience boosts", "1 vote point and a random cash amount or item reward.");
            stage = 1;
        } else if(stage == 1) {
            player.getDialogueManager().sendNPCDialogue(kingHealthorg, Expressions.NORMAL, "You can vote using the ::vote command or by visiting", GameSettings.WEBSITE + "/vote");
            stage = 2;
        } else if(stage == 2) {
            player.getDialogueManager().startDialogue(new KingHealthorgD());
        } else
            end();
    }
}