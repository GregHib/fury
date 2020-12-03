package com.fury.game.content.dialogue.impl.minigames.fightcave;

import com.fury.game.content.dialogue.Expressions;
import com.fury.game.content.dialogue.Dialogue;

/**
 * Created by Greg on 04/12/2016.
 */
public class JadDefeatD extends Dialogue {
    @Override
    public void start() {
        player.getDialogueManager().sendNPCDialogue(2617, Expressions.NORMAL, "You even defeated TzTok-Jad, I am most impressed!", "Please take this gift as a reward.");
    }

    @Override
    public void run(int optionId) {
        end();
    }
}