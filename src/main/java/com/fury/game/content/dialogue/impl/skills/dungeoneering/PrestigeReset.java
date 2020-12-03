package com.fury.game.content.dialogue.impl.skills.dungeoneering;

import com.fury.game.content.dialogue.DialogueManager;
import com.fury.game.content.dialogue.Dialogue;
/**
 * Created by Greg on 19/01/2017.
 */
public class PrestigeReset extends Dialogue {
    @Override
    public void start() {
        player.getDialogueManager().sendDialogue("Are you sure you want to reset your dungeon progress?", "Your previous progress will be set to the number of floors you", "have completed and all floors will be marked as incomplete.", "This cannot be undone.");
    }

    @Override
    public void run(int optionId) {
        if (stage == -1) {
            player.getDialogueManager().sendOptionsDialogue(DialogueManager.DEFAULT_OPTIONS_TITLE, "Yes, reset my progress.", "No, don't reset my progress.");
            stage = 0;
        } else if (stage == 0) {
            if (optionId == DialogueManager.OPTION_1)
                player.getDungManager().resetProgress();
            end();
        }
    }
}