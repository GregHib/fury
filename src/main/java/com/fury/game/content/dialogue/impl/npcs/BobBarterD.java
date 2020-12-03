package com.fury.game.content.dialogue.impl.npcs;

import com.fury.game.content.dialogue.Expressions;
import com.fury.game.content.dialogue.DialogueManager;
import com.fury.game.content.dialogue.Dialogue;
import com.fury.game.content.skill.member.herblore.Drinkables;

public class BobBarterD extends Dialogue {

    private static final int BOB = 6524;

    @Override
    public void start() {
        player.getDialogueManager().sendNPCDialogue(BOB, Expressions.PLAIN_TALKING, "Hello, chum, interested in some potion decanting?");
    }

    @Override
    public void run(int optionId) {
        if(stage == -1) {
            player.getDialogueManager().sendOptionsDialogue(DialogueManager.DEFAULT_OPTIONS_TITLE, "Can you decant things for me?", "Sorry I've got to split.");
            stage = 0;
        } else if(stage == 0) {
            if(optionId == DialogueManager.OPTION_1) {
                player.getDialogueManager().sendNPCDialogue(BOB, Expressions.PLAIN_TALKING, "Why of course my son.");
                stage = 1;
            } else
                end();
        } else if(stage == 1) {
            player.getDialogueManager().sendNPCDialogue(BOB, Expressions.PLAIN_TALKING, "Tis the work of a herblore master.");
            Drinkables.decantPotsInv(player);
            stage = 2;
        } else
            end();
    }
}