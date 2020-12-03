package com.fury.game.content.dialogue.impl.npcs;

import com.fury.game.content.dialogue.Expressions;
import com.fury.game.content.dialogue.DialogueManager;
import com.fury.game.content.dialogue.Dialogue;

public class MaverickD extends Dialogue {
    private int maverick = 13824;
    @Override
    public void start() {
        player.getDialogueManager().sendPlayerDialogue(Expressions.PLAIN_TALKING, "Who are you?");
    }

    @Override
    public void run(int optionId) {
        if(stage == -1) {
            player.getDialogueManager().sendNPCDialogue(maverick, Expressions.GOOFY_LAUGH, "I am Maverick, explorer of the great unknown!");
            stage = 0;
        } else if(stage == 0) {
            player.getDialogueManager().sendOptionsDialogue(DialogueManager.DEFAULT_OPTIONS_TITLE, "What's so great about here?", "Nice to meet you.");
            stage = 1;
        } else if(stage == 1) {
            if (optionId == DialogueManager.OPTION_1) {
                player.getDialogueManager().sendPlayerDialogue(Expressions.TALK_SWING, "What's so great about here?");
                stage = 2;
            } else {
                player.getDialogueManager().sendPlayerDialogue(Expressions.PLAIN_TALKING, "Nice to meet you.");
                stage = -2;
            }
        } else if(stage == 2) {
            player.getDialogueManager().sendNPCDialogue(maverick, Expressions.PLAIN_TALKING, "These creatures are most spectacular,", "they are connected with the very walls", "and roots you see around you.");
            stage = 3;
        } else if(stage == 3) {
            player.getDialogueManager().sendNPCDialogue(maverick, Expressions.PLAIN_TALKING, "They all work to serve and protect the queen. However", "some of them mutate and seek to harm the queen.");
            stage = 4;
        } else if(stage == 4) {
            player.getDialogueManager().sendNPCDialogue(maverick, Expressions.PLAIN_TALKING, "You can help the queen by killing mutated", "creatures and cutting the mutated roots.");
            stage = 5;
        } else if(stage == 5) {
            player.getDialogueManager().sendNPCDialogue(maverick, Expressions.PLAIN_TALKING, "She'll give you favours in return, which", "you can exchange for rewards at the offering stone.");
            stage = 6;
        } else if(stage == 6) {
            player.getDialogueManager().sendNPCDialogue(maverick, Expressions.PLAIN_TALKING, "Anyway, I must get back to my research", "Good luck adventurer!");
            stage = -2;
        } else
            end();
    }
}