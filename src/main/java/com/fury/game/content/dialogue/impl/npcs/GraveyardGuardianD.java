package com.fury.game.content.dialogue.impl.npcs;

import com.fury.game.content.dialogue.Expressions;
import com.fury.game.content.dialogue.Dialogue;

/**
 * Created by Greg on 03/12/2016.
 */
public class GraveyardGuardianD extends Dialogue {
    private int guardian = 3101;

    @Override
    public void start() {
        player.getDialogueManager().sendNPCDialogue(guardian, Expressions.PLAIN_TALKING, "I've cast a spell on the area.", "If you die, you will keep your items.", "Good luck, adventurer.");
    }

    @Override
    public void run(int optionId) {
        end();
    }

}