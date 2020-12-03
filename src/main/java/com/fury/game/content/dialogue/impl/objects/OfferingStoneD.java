package com.fury.game.content.dialogue.impl.objects;

import com.fury.game.content.controller.impl.JadinkoLair;
import com.fury.game.content.dialogue.DialogueManager;
import com.fury.game.content.dialogue.Dialogue;
import com.fury.game.content.dialogue.input.impl.EnterRewardsToWithdraw;
import com.fury.core.model.item.Item;
import com.fury.game.node.entity.actor.figure.player.Points;

public class OfferingStoneD extends Dialogue {
    @Override
    public void start() {
        boolean skip = (boolean) parameters[0];
        if (player.getPoints().get(Points.FAVOUR) <= 0)
            player.getDialogueManager().sendDialogue("The table contains nothing of value for you yet.");
        else {
            if (skip) {
                player.getDialogueManager().sendOptionsDialogue(DialogueManager.DEFAULT_OPTIONS_TITLE, "Random mix (10 points)", "Fruits only (13 points)", "Seeds only (40 point)", "Herb Pouch (2000 points)");
                stage = 0;
            } else
                player.getDialogueManager().sendDialogue("The table has several options available...");
        }
    }

    @Override
    public void run(int optionId) {
        if (stage == -1) {
            if (player.getPoints().get(Points.FAVOUR) <= 0)
                end();
            else {
                player.getDialogueManager().sendOptionsDialogue(DialogueManager.DEFAULT_OPTIONS_TITLE, "Random mix (10 points)", "Fruits only (13 points)", "Seeds only (15 points)", "Herb Pouch (2000 points)");
                stage = 0;
            }
        } else if (stage == 0) {
            if (optionId == DialogueManager.OPTION_1) {
                end();
                player.setInputHandling(new EnterRewardsToWithdraw(0));
                player.getPacketSender().sendEnterAmountPrompt("Enter amount of rewards to withdraw:");
            } else if (optionId == DialogueManager.OPTION_2) {
                end();
                player.setInputHandling(new EnterRewardsToWithdraw(1));
                player.getPacketSender().sendEnterAmountPrompt("Enter amount of rewards to withdraw:");
            } else if (optionId == DialogueManager.OPTION_3) {
                end();
                player.setInputHandling(new EnterRewardsToWithdraw(2));
                player.getPacketSender().sendEnterAmountPrompt("Enter amount of rewards to withdraw:");
            } else if (optionId == DialogueManager.OPTION_4) {
                if (player.getPoints().has(Points.FAVOUR, 2000)) {
                    if (player.getInventory().getSpaces() <= 0) {
                        player.getInventory().full();
                        end();
                    } else {
                        JadinkoLair.removePoints(player, 2000);
                        player.getInventory().addSafe(new Item(14701));
                        player.message("A mystical bag appears on the table, it can hold up to 30 of each type of herb.");
                        end();
                    }
                } else {
                    player.getDialogueManager().sendDialogue("The stone wont let you take that item without more favours.");
                }
            } else
                end();
        } else
            end();
    }
}