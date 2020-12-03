package com.fury.game.content.dialogue.impl.items;

import com.fury.game.content.dialogue.Expressions;
import com.fury.game.content.dialogue.DialogueManager;
import com.fury.game.content.dialogue.Dialogue;
import com.fury.game.content.skill.member.herblore.Drinkables;

public class DecantD extends Dialogue {
    private int size;
    private int cost = 4000;

    @Override
    public void start() {
        size = (int) parameters[0];
        player.getDialogueManager().sendNPCDialogue(6524, Expressions.PLAIN_TALKING, "That'll be "  + (cost/1000 * size) +  "k.");
    }

    @Override
    public void run(int optionId) {
        if(stage == -1) {
            player.getDialogueManager().sendOptionsDialogue(DialogueManager.DEFAULT_OPTIONS_TITLE, "Yes", "No thanks");
            stage = 0;
        } else if(stage == 0) {
            if(optionId == DialogueManager.OPTION_1)
                if(Drinkables.hasSpaceToDecant(player, size)) {
                    if (player.getInventory().removeCoins(cost * size))
                        Drinkables.decantDoses(player, size);
                } else
                    player.message("You don't have enough inventory slots to perform this action.");
            end();
        }
    }
}