package com.fury.game.content.dialogue.impl.skills.construction;

import com.fury.game.content.dialogue.DialogueManager;
import com.fury.game.content.dialogue.Dialogue;
import com.fury.game.entity.object.GameObject;

/**
 * Created by Jon on 2/9/2017.
 */
public class RemoveBuildD extends Dialogue {
    GameObject object;

    @Override
    public void start() {
        this.object = (GameObject) parameters[0];
        player.getDialogueManager().sendOptionsDialogue("Really remove it?", "Yes.", "No.");
    }

    @Override
    public void run(int optionId) {
        if (optionId == DialogueManager.OPTION_1) {
            player.getHouse().removeBuild(object);
        }
        end();
    }
}
