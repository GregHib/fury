package com.fury.game.content.dialogue.impl.npcs;

import com.fury.game.content.dialogue.Expressions;
import com.fury.game.content.dialogue.DialogueManager;
import com.fury.game.content.dialogue.Dialogue;

/**
 * Created by Greg on 03/12/2016.
 */
public class StarSpriteQD extends Dialogue {
    private int sprite = 8091;

    @Override
    public void start() {
        player.getDialogueManager().sendOptionsDialogue(DialogueManager.DEFAULT_OPTIONS_TITLE, "What's a star sprite?", "What are you going to do without your star?", "I thought stars were huge balls of burning gas.", "Well, I'm glad you're okay.");
    }

    @Override
    public void run(int optionId) {
        if(stage == -1) {
            if (optionId == DialogueManager.OPTION_1) {
                player.getDialogueManager().sendPlayerDialogue(Expressions.PLAIN_TALKING, "What's a star sprite?");
                stage = 0;
            } else if (optionId == DialogueManager.OPTION_2) {
                player.getDialogueManager().sendPlayerDialogue(Expressions.PLAIN_TALKING, "What are you going to do without your star?");
                stage = 2;
            } else if (optionId == DialogueManager.OPTION_3) {
                player.getDialogueManager().sendPlayerDialogue(Expressions.PLAIN_TALKING, "I thought stars were huge balls of burning gas.");
                stage = 3;
            } else if (optionId == DialogueManager.OPTION_4) {
                player.getDialogueManager().sendPlayerDialogue(Expressions.PLAIN_TALKING, "Well, I'm glad you're okay.");
                stage = 4;
            }

        } else if(stage == 0) {
            player.getDialogueManager().sendNPCDialogue(sprite, Expressions.PLAIN_TALKING, "We're what makes the stars in the sky shine. I made", "this star shine when it was in the sky.");
            stage = 1;
        } else if(stage == 1) {
            player.getDialogueManager().startDialogue(new StarSpriteQD());
        } else if(stage == 2) {
            player.getDialogueManager().sendNPCDialogue(sprite, Expressions.PLAIN_TALKING, "Don't worry about me. I'm sure I'll find some good rocks", "around here and get back up into the sky in no time.");
            stage = 1;
        } else if(stage == 3) {
            player.getDialogueManager().sendNPCDialogue(sprite, Expressions.PLAIN_TALKING, "Most of them are, but a lot of shooting stars on this", "plane of the multiverse are rocks with star sprites in", "them.");
            stage = 1;
        } else if(stage == 4) {
            player.getDialogueManager().sendNPCDialogue(sprite, Expressions.NORMAL, "Thank you");
            stage = 5;
        } else
            end();
    }
}