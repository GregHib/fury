package com.fury.game.content.dialogue.impl.minigames;

import com.fury.game.content.controller.impl.Rule;
import com.fury.game.content.dialogue.DialogueManager;
import com.fury.game.content.dialogue.Dialogue;

public class ForfeitD extends Dialogue {

    @Override
    public void start() {
        player.getDialogueManager().sendOptionsDialogue("Forfeit Duel?", "Yes.", "No.");
    }

    @Override
    public void run(int optionId) {
        if (optionId == DialogueManager.OPTION_1) {
            if (!player.getDuelConfigurations().getRule(Rule.NO_FORFEIT)) {
                player.getDuelConfigurations().endDuel(player, false, false);
                player.getDuelConfigurations().endDuel(player.getDuelConfigurations().getOther(player), false, false);
            } else {
                player.getDialogueManager().sendDialogue("You can't forfeit during this duel.");
            }
        }
        end();
    }
}