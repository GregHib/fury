package com.fury.game.content.dialogue.impl.npcs;

import com.fury.game.GameSettings;
import com.fury.game.content.dialogue.Expressions;
import com.fury.game.entity.character.player.content.PlayerPanel;
import com.fury.game.content.dialogue.DialogueManager;
import com.fury.game.content.dialogue.Dialogue;

/**
 * Created by Greg on 04/12/2016.
 */
public class RFDReturnD extends Dialogue {
    @Override
    public void start() {
        player.getDialogueManager().sendNPCDialogue(3385, Expressions.SAD, "Please! You must help me, adventurer!", "The Culinaromancer is back!", "He's going to destroy " + GameSettings.NAME + "!", "Will you help defeat him?");
    }

    @Override
    public void run(int optionId) {
        if(stage == -1) {
            stage = 0;
            player.getDialogueManager().sendOptionsDialogue(DialogueManager.DEFAULT_OPTIONS_TITLE, "Yes", "No, maybe later.");
        } else if(stage == 0) {
            end();
            if(optionId == DialogueManager.OPTION_1) {
                player.getDialogueManager().startDialogue(new RFDD());
                player.getMinigameAttributes().getRecipeForDisasterAttributes().setPartFinished(0, true);
                PlayerPanel.refreshPanel(player);
            }
        }
    }
}