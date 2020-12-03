package com.fury.game.content.dialogue.impl.npcs;

import com.fury.game.content.dialogue.Expressions;
import com.fury.game.content.dialogue.Dialogue;

/**
 * Created by Greg on 04/12/2016.
 */
public class RFDD extends Dialogue {
    @Override
    public void start() {
        player.getDialogueManager().sendPlayerDialogue(Expressions.PLAIN_TALKING, "How do I defeat the Culinaromancer?");
    }

    @Override
    public void run(int optionId) {
        if(stage == -1) {
            stage = 0;
            player.getDialogueManager().sendNPCDialogue(3385, Expressions.PLAIN_TALKING, "Enter the portal and gain the Culinaromancer's attention", "by defeating all his servants.");
        } else if(stage == 0) {
            stage = 1;
            player.getDialogueManager().sendNPCDialogue(3385, Expressions.PLAIN_TALKING, "Once you've defeated all of them, the Culinaromancer", "will probably fight you himself. You must slay him!");
        } else if(stage == 1) {
            stage = 2;
            player.getDialogueManager().sendNPCDialogue(3385, Expressions.PLAIN_TALKING, "Every servant that you defeat will unlock deeper access", "into the Culinaromancer's chest which is located next to me.");
        } else if(stage == 2) {
            stage = 3;
            player.getDialogueManager().sendNPCDialogue(3385, Expressions.PLAIN_TALKING, "Thank you for helping adventurer!", "I wish you the best of luck!");
        } else
            end();
    }
}