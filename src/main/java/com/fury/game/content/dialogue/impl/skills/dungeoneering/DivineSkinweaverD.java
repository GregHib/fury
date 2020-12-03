package com.fury.game.content.dialogue.impl.skills.dungeoneering;

import com.fury.game.content.dialogue.Expressions;
import com.fury.game.content.dialogue.Dialogue;

/**
 * Created by Greg on 04/12/2016.
 */
public class DivineSkinweaverD extends Dialogue {
    @Override
    public void start() {
        player.getDialogueManager().sendNPCDialogue(7969, Expressions.NORMAL,"We can talk later, kill the skeletons now.");
    }

    @Override
    public void run(int optionId) {
        end();
    }
}