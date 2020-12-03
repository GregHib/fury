package com.fury.game.content.dialogue.impl.skills.dungeoneering;

import com.fury.game.content.dialogue.Expressions;
import com.fury.game.content.global.Achievements;
import com.fury.game.content.dialogue.DialogueManager;
import com.fury.game.content.dialogue.Dialogue;
import com.fury.game.content.skill.free.dungeoneering.DungeoneeringRewards;
import com.fury.util.Misc;

/**
 * Created by Greg on 05/01/2017.
 */
public class RewardsTraderD extends Dialogue {
    int trader = 9711;

    @Override
    public void start() {
        player.getDialogueManager().sendNPCDialogue(trader, Expressions.SCARED, "There's m...m..monsters down their!");
    }

    @Override
    public void run(int optionId) {
        if(stage == -1) {
            player.getDialogueManager().sendOptionsDialogue(DialogueManager.DEFAULT_OPTIONS_TITLE, "Who are you?", "Can I see what you have to trade?", "Can you repair my equipment?", "Never mind");
            stage = 0;
        } else if(stage == 0) {
            Achievements.finishAchievement(player, Achievements.AchievementData.TALK_TO_REWARDS_TRADER);
            if(optionId == DialogueManager.OPTION_1) {
                boolean r = Misc.random(100) == 0;
                player.getDialogueManager().sendNPCDialogue(trader, r ? Expressions.PLAIN_TALKING : Expressions.SCARED, r ? "They called me Ma..." : "I don't know...");
                stage = r ? 1 : 100;
            } else if(optionId == DialogueManager.OPTION_2) {
                end();
                DungeoneeringRewards.openInterface(player);
            } else if(optionId == DialogueManager.OPTION_3) {
                player.getDialogueManager().sendNPCDialogue(trader, Expressions.NORMAL, "Nope.");
                stage = 100;
            } else
                end();
        } else if(stage == 1) {
            player.getDialogueManager().sendPlayerDialogue(Expressions.PLAIN_TALKING, "Max?");
            stage = 2;
        } else if(stage == 2) {
            player.getDialogueManager().sendPlayerDialogue(Expressions.PLAIN_TALKING, "Marley?");
            stage = 3;
        } else if(stage == 3) {
            player.getDialogueManager().sendPlayerDialogue(Expressions.PLAIN_TALKING, "Mako?");
            stage = 4;
        } else if(stage == 4) {
            player.getDialogueManager().sendPlayerDialogue(Expressions.PLAIN_TALKING, "Malfoy?");
            stage = 5;
        } else if(stage == 5) {
            player.getDialogueManager().sendPlayerDialogue(Expressions.PLAIN_TALKING, "Mark?");
            stage = 6;
        } else if(stage == 6) {
            player.getDialogueManager().sendNPCDialogue(trader, Expressions.PLAIN_TALKING,"On second thoughts I can't remember...");
            stage = 100;
        } else
            end();
    }
}
