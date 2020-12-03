package com.fury.game.content.dialogue.impl.npcs;

import com.fury.game.content.dialogue.Expressions;
import com.fury.game.content.dialogue.DialogueManager;
import com.fury.game.content.dialogue.Dialogue;
import com.fury.game.content.global.Achievements;
import com.fury.core.model.item.Item;

/**
 * Created by Greg on 15/11/2016.
 */
public class MaxD extends Dialogue {
    int max = 3705;

    @Override
    public void start() {
        player.getDialogueManager().sendOptionsDialogue(DialogueManager.DEFAULT_OPTIONS_TITLE, "Talk about Completionist.", "I'd like you buy a Completionist Cape [25m]", "I'd like to buy a Max Cape [3m]", "Cancel");
    }

    @Override
    public void run(int optionId) {
        if(stage == -1) {
            if(optionId == DialogueManager.OPTION_1) {
                stage = 0;
                player.getDialogueManager().sendNPCDialogue(max, Expressions.PLAIN_TALKING, "Once you've reached level 99 in every skill,", "required to achieve the max cape. You can begin", "working towards the completionist cape.");
            } else {
                end();
            }
            if(optionId == DialogueManager.OPTION_2) {
                player.getPacketSender().sendInterfaceRemoval();
                if(player.getInventory().getSpaces() == 0) {
                    player.getInventory().full();
                    return;
                }
                for (Achievements.AchievementData d : Achievements.AchievementData.values()) {
                    if (!player.getAchievementAttributes().getCompletion()[d.ordinal()]) {
                        player.message("You must have completed all achievements in order to buy this cape.");
                        return;
                    }
                }
                boolean skillsReq = player.getSkills().isMaxed();
                if (!skillsReq) {
                    player.message("You must have maximum level in all skills in order to buy this cape.");
                    return;
                }
                if(player.getInventory().removeCoins(25000000)) {
                    player.getInventory().add(new Item(20769));
                    player.message("You've purchased a Completionist cape.");
                }
                end();
            } else if(optionId == DialogueManager.OPTION_3) {
                player.getPacketSender().sendInterfaceRemoval();
                if(player.getInventory().getSpaces() == 0) {
                    player.getInventory().full();
                    return;
                }
                if (!player.getSkills().isMaxed()) {
                    player.message("You must have 99 in all skills in order to buy this cape.");
                    return;
                }
                if(player.getInventory().removeCoins(3000000)) {
                    player.getInventory().add(new Item(20767));
                    player.message("You've purchased a Max cape.");
                }
                end();
            }
        } else if(stage == 0) {
            stage = 1;
            player.getDialogueManager().sendNPCDialogue(max, Expressions.PLAIN_TALKING, "The completionist cape requires 120 dungeoneering", "completion of all quests", "and all tasks in the achievement diary.");
        } else if(stage == 1) {
           player.getDialogueManager().startDialogue(new MaxD());
        } else {
            end();
        }
    }
}
