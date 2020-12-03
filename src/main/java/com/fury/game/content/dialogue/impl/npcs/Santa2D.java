package com.fury.game.content.dialogue.impl.npcs;

import com.fury.cache.Revision;
import com.fury.game.content.dialogue.Expressions;
import com.fury.game.content.dialogue.DialogueManager;
import com.fury.game.content.dialogue.Dialogue;
import com.fury.core.model.item.Item;

public class Santa2D extends Dialogue {

    private static int santa = 9400;

    @Override
    public void start() {
        player.getDialogueManager().sendNPCDialogue(santa, Expressions.PLAIN_TALKING, "Thanks for collecting all those snowflakes!");
    }

    @Override
    public void run(int optionId) {
        if (stage == -1) {
            if(player.getInventory().contains(new Item(33596, Revision.RS3))) {
                player.getDialogueManager().sendNPCDialogue(santa, Expressions.PLAIN_TALKING, "I'll take those off of you for 10k each");
                stage = 0;
            } else
                end();
        } else if (stage == 0) {
            player.getDialogueManager().sendOptionsDialogue(DialogueManager.DEFAULT_OPTIONS_TITLE, "Yes please", "No thanks");
            stage = 1;
        } else if(stage == 1) {
            if (optionId == DialogueManager.OPTION_1) {
                int amount = player.getInventory().getAmount(new Item(33596, Revision.RS3));
                if(player.getInventory().addCoins(10000 * amount)) {
                    player.getInventory().delete(new Item(33596, amount, Revision.RS3));
                    player.message("Santa exchanges your snowflakes for " + (amount * 10) + "k.");
                }
            }
            end();
        }
    }
}