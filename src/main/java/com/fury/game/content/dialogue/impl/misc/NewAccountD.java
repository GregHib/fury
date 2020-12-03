package com.fury.game.content.dialogue.impl.misc;

import com.fury.game.GameSettings;
import com.fury.game.content.dialogue.Expressions;
import com.fury.game.content.dialogue.Dialogue;

/**
 * Created by Greg on 03/12/2016.
 */
public class NewAccountD extends Dialogue {
    @Override
    public void start() {
        player.getDialogueManager().sendNPCDialogue(6139, Expressions.PLAIN_TALKING, "That mode will suite you well. If you want ", "I can give you a tour of " + GameSettings.NAME + ", speak to me", "south of Edgeville and I'll show you around.");
    }

    @Override
    public void run(int optionId) {
        end();
    }

}