package com.fury.game.content.dialogue.impl.npcs;

import com.fury.game.content.dialogue.Expressions;
import com.fury.game.content.dialogue.Dialogue;

/**
 * Created by Greg on 03/12/2016.
 */
public class UriD extends Dialogue {
    @Override
    public void start() {
        player.getDialogueManager().sendNPCDialogue(5143, Expressions.NORMAL, "I do not believe we have any business, Comrade.");
    }

    @Override
    public void run(int optionId) {
        end();
    }
}