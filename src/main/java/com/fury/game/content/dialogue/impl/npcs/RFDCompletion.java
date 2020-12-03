package com.fury.game.content.dialogue.impl.npcs;

import com.fury.game.content.dialogue.Expressions;
import com.fury.game.content.dialogue.Dialogue;

/**
 * Created by Greg on 04/12/2016.
 */
public class RFDCompletion extends Dialogue {
    @Override
    public void start() {
        player.getDialogueManager().sendNPCDialogue(3385, Expressions.NORMAL, "You did it!", "You defeated the Culinaromancer!", "Thank you so much!");
    }

    @Override
    public void run(int optionId) {
        if(stage == -1) {
            stage = 0;
            player.getDialogueManager().sendStatement("You have now unlocked everything in Culinaromancer's chest!");
        } else if(stage == 0) {
            end();
        }
    }
}