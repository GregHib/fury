package com.fury.game.content.dialogue.impl.npcs;

import com.fury.game.content.dialogue.Expressions;
import com.fury.game.content.dialogue.DialogueManager;
import com.fury.game.content.dialogue.Dialogue;

/**
 * Created by Greg on 03/12/2016.
 */
public class SpiritTreeD extends Dialogue {
    @Override
    public void start() {
        //"Hello, gnome-friend. Would you like to travel to the
        //home of the tree gnomes?
        player.getDialogueManager().sendNPCDialogue(3636, Expressions.PLAIN_TALKING, "If you are a friend of the gnome people,", "you are a friend of mine. Do you wish to travel, ", "or do you wish to ask about the evil tree?");
    }

    @Override
    public void run(int optionId) {
        if(stage == -1) {
            player.getDialogueManager().sendOptionsDialogue(DialogueManager.DEFAULT_OPTIONS_TITLE, "Travel", "Ask about the Evil Tree.", "Nothing");
            stage = 0;
        } else if(stage == 0) {
            if(optionId == DialogueManager.OPTION_1) {
                player.getDialogueManager().startDialogue(new SpiritTreeTravelD());
            } else if(optionId == DialogueManager.OPTION_2) {
                player.getDialogueManager().startDialogue(new EvilTreeD());
            } else
                end();
        }
    }
}