package com.fury.game.content.dialogue.impl.minigames.barrows;

import com.fury.game.content.dialogue.Dialogue;
import com.fury.game.content.dialogue.DialogueManager;

/**
 * Created by Greg on 04/12/2016.
 */
public class TunnelD extends Dialogue {

    @Override
    public void start() {
        player.getDialogueManager().sendStatement("You've found a hidden tunnel! Would you like to enter?");
    }

    @Override
    public void run(int optionId) {
        if(stage == -1) {
            stage = 0;
            player.getDialogueManager().sendOptionsDialogue(DialogueManager.DEFAULT_OPTIONS_TITLE, "Enter", "Cancel");
        } else if(stage == 0) {
            end();
            if(optionId == DialogueManager.OPTION_1) {
                if (player.getMinigameAttributes().getBarrowsMinigameAttributes().getKillcount() < 5) {
                    player.message("You must have a killcount of at least 5 to enter the tunnel.");
                    return;
                }
                player.moveTo(3552, 9692);
            }
        }
    }
}