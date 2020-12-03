package com.fury.game.content.dialogue.impl.misc;

import com.fury.game.content.controller.impl.ZarosGodwars;
import com.fury.game.content.dialogue.Dialogue;
import com.fury.game.content.dialogue.DialogueManager;

/**
 * Created by Greg on 03/12/2016.
 */
public class NexEnterD extends Dialogue {
    @Override
    public void start() {
        player.getDialogueManager().sendDialogue("The room beyond this point is a prison!",
                "There is no way out other than death or teleport.",
                "Only those who endure dangerous encounters should proceed.");
    }

    @Override
    public void run(int optionId) {
        if(stage == -1) {
            stage = 0;
            player.getDialogueManager().sendOptionsDialogue("Are you sure about this?", "Continue", "Cancel");
        } else if(stage == 0) {
            if(optionId == DialogueManager.OPTION_1) {
                player.message("You enter Nex's lair..");
                player.moveTo(2911, 5204);
                player.getControllerManager().startController(new ZarosGodwars());
            }
            end();
        }
    }
}