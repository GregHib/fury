package com.fury.game.content.dialogue.impl.npcs;

import com.fury.game.content.dialogue.Dialogue;
import com.fury.game.content.dialogue.DialogueManager;

public class NexEntranceD extends Dialogue {
    @Override
    public void start() {
        player.getDialogueManager().sendDialogue("The room beyond this point is a prison!", "There is no way out other than death or teleport.", "Only those who endure dangerous encounters should proceed.");
    }

    @Override
    public void run(int optionId) {
        if (stage == -1) {
            stage = 0;
//            player.getDialogueManager().sendOptionsDialogue("There are currently " + ZarosGodwars.getPlayers().size() + " people fighting.<br>Do you wish to join them?", "Climb down.", "Stay here.");
        } else if (stage == 0) {
            if (optionId == DialogueManager.OPTION_1) {
                player.moveTo(2911, 5204);
                player.getControllerManager().startController("ZGDControler");
            }
            end();
        }
    }
}