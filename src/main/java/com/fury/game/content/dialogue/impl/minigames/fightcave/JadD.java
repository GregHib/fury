package com.fury.game.content.dialogue.impl.minigames.fightcave;

import com.fury.game.content.dialogue.Expressions;
import com.fury.game.content.dialogue.Dialogue;

/**
 * Created by Greg on 04/12/2016.
 */
public class JadD extends Dialogue {

    @Override
    public void start() {
        player.getDialogueManager().sendNPCDialogue(2617, Expressions.NORMAL, "Look out, here comes TzTok-Jad!", "You're on your own now JalYt, prepare to fight for", "your life!");
    }

    @Override
    public void run(int optionId) {
        end();
    }
}