package com.fury.game.content.dialogue.impl.skills.construction;

import com.fury.game.content.dialogue.DialogueManager;
import com.fury.game.content.dialogue.Dialogue;
import com.fury.game.entity.object.GameObject;

/**
 * Created by Jon on 2/9/2017.
 */
public class ChallengeModeLeverD extends Dialogue {
    private GameObject object;

    @Override
    public void start() {
        object = (GameObject) this.parameters[0];
        player.getDialogueManager().sendOptionsDialogue(DialogueManager.DEFAULT_OPTIONS_TITLE, "Activate challenge mode.", "Activate pvp mode.", "Nevermind.");
    }

    @Override
    public void run(int optionId) {
        if (optionId != DialogueManager.OPTION_3) {
            player.getHouse().switchChallengeMode(optionId == DialogueManager.OPTION_2);
            player.getHouse().sendPullLeverEmote(object);
        }
        end();
    }
}
