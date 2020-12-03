package com.fury.game.content.dialogue.impl.objects;

import com.fury.game.content.controller.impl.Wilderness;
import com.fury.game.content.dialogue.Dialogue;
import com.fury.game.content.dialogue.DialogueManager;
import com.fury.game.entity.object.GameObject;

public class WildernessDitchD extends Dialogue {
    private GameObject ditch;

    @Override
    public void start() {
        ditch = (GameObject) parameters[0];
        player.getDialogueManager().sendDialogue("Warning! Proceeding will enter the Wilderness.", "Other players can attack you.", "You can't teleport above level 20.", "If you drop items, other players may take them immediately.", "You will leave no gravestone behind if you die.");
    }

    @Override
    public void run(int optionId) {
        if(stage == -1) {
            player.getDialogueManager().sendOptionsDialogue("Are you sure you wish to proceed?", "Enter Wilderness", "Don't Enter");
            stage = 0;
        } else if(stage == 0) {
            if (optionId == DialogueManager.OPTION_1)
                Wilderness.crossDitch(player, ditch);
            end();
        }
    }
}