package com.fury.game.content.dialogue.impl.minigames.pest;

import com.fury.game.content.dialogue.DialogueManager;
import com.fury.game.content.dialogue.Dialogue;

public class LanderD extends Dialogue {
    @Override
    public void start() {
        player.getDialogueManager().sendDialogue("Are you sure you would like to leave the lander?");
    }

    @Override
    public void run(int optionId) {
        if(stage == -1) {
            stage = 0;
            player.getDialogueManager().sendOptionsDialogue(DialogueManager.DEFAULT_OPTIONS_TITLE, "Yes, get me out of here!", "No, I want to stay.");
        } else if(stage == 0) {
            if(optionId == DialogueManager.OPTION_1)
                player.getControllerManager().forceStop();
            end();
        }
    }
}