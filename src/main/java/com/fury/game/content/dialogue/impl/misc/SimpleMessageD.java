package com.fury.game.content.dialogue.impl.misc;

import com.fury.game.content.dialogue.Dialogue;

/**
 * Created by Greg on 23/11/2016.
 */
public class SimpleMessageD extends Dialogue {
    @Override
    public void start() {
        String[] messages = new String[parameters.length];
        for (int i = 0; i < messages.length; i++)
            messages[i] = (String) parameters[i];
        player.getDialogueManager().sendDialogue(messages);
    }

    @Override
    public void run(int optionId) {
        end();
    }

}
