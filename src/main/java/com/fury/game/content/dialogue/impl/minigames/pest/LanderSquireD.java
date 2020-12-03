package com.fury.game.content.dialogue.impl.minigames.pest;

import com.fury.game.GameSettings;
import com.fury.game.content.dialogue.Expressions;
import com.fury.game.content.dialogue.DialogueManager;
import com.fury.game.content.dialogue.Dialogue;
import com.fury.game.content.global.minigames.impl.Lander;

public class LanderSquireD extends Dialogue {
    private int npcId;

    @Override
    public void start() {
        npcId = (int) this.parameters[0];
        player.getDialogueManager().sendNPCDialogue(npcId, Expressions.PLAIN_TALKING, "Hi, how can I help you?");
    }

    @Override
    public void run(int optionId) {
        if(stage == -1) {
            player.getDialogueManager().sendOptionsDialogue(DialogueManager.DEFAULT_OPTIONS_TITLE, "Who are you?", "Where does this ship go?", "I'd like to go to your outpost.", "I'm fine thanks.");
            stage = 0;
        } else if(stage == 0) {
            if(optionId == DialogueManager.OPTION_1) {
                player.getDialogueManager().sendPlayerDialogue(Expressions.PLAIN_TALKING, "Who are you?");
                stage = 1;
            } else if(optionId == DialogueManager.OPTION_2) {
                player.getDialogueManager().sendPlayerDialogue(Expressions.PLAIN_TALKING, "Where does this ship go?");
                stage = 17;
            } else if(optionId == DialogueManager.OPTION_3) {
                player.getDialogueManager().sendPlayerDialogue(Expressions.PLAIN_TALKING, "I'd like to go to your outpost.");
                stage = 20;
            } else {
                player.getDialogueManager().sendPlayerDialogue(Expressions.PLAIN_TALKING, "I'm fine thanks.");
                stage = -2;
            }
        } else if(stage == 1) {
            player.getDialogueManager().sendNPCDialogue(npcId, Expressions.PLAIN_TALKING, "I'm a squire for the Void Knights.");
            stage = 2;
        } else if(stage == 2) {
            player.getDialogueManager().sendPlayerDialogue(Expressions.PLAIN_TALKING, "The who?");
            stage = 3;
        } else if(stage == 3) {
            player.getDialogueManager().sendNPCDialogue(npcId, Expressions.PLAIN_TALKING, "The Void Knights,", "they are great warriors of balance who do", "Guthix's work here in " + GameSettings.NAME + ".");
            stage = 4;
        } else if(stage == 4) {
            player.getDialogueManager().sendOptionsDialogue(DialogueManager.DEFAULT_OPTIONS_TITLE, "Wow, can I join?", "What kind of work?", "What's '" + GameSettings.NAME + "'?", "Uh huh, sure.");
            stage = 5;
        } else if(stage == 5) {
            if(optionId == DialogueManager.OPTION_1) {
                player.getDialogueManager().sendPlayerDialogue(Expressions.PLAIN_TALKING, "Wow, can I join?");
                stage = 6;
            } else if(optionId == DialogueManager.OPTION_2) {
                player.getDialogueManager().sendPlayerDialogue(Expressions.PLAIN_TALKING, "What kind of work?");
                stage = 9;
            } else if(optionId == DialogueManager.OPTION_3) {
                player.getDialogueManager().sendPlayerDialogue(Expressions.PLAIN_TALKING, "What's '" + GameSettings.NAME + "'?");
                stage = 16;
            } else {
                player.getDialogueManager().sendPlayerDialogue(Expressions.PLAIN_TALKING, "Uh huh, sure.");
                stage = -2;
            }
        } else if(stage == 6) {
            player.getDialogueManager().sendNPCDialogue(npcId, Expressions.PLAIN_TALKING, "Entry is strictly invite only,", "however we do need help continuing Guthix's work.");
            stage = 7;
        } else if(stage == 7) {
            player.getDialogueManager().sendOptionsDialogue(DialogueManager.DEFAULT_OPTIONS_TITLE, "What kind of work?", "Good luck with that.");
            stage = 8;
        } else if(stage == 8) {
            if(optionId == DialogueManager.OPTION_1) {
                player.getDialogueManager().sendPlayerDialogue(Expressions.PLAIN_TALKING, "What kind of work?");
                stage = 9;
            } else {
                player.getDialogueManager().sendPlayerDialogue(Expressions.PLAIN_TALKING, "Good luck with that.");
                stage = -2;
            }
        } else if(stage == 9) {
            player.getDialogueManager().sendNPCDialogue(npcId, Expressions.PLAIN_TALKING, "Ah well you see we try to keep " + GameSettings.NAME + " as Guthix intended,", "it's very challenging. Actually we've been", "having some problems recently, maybe you could help us?");
            stage = 10;
        } else if(stage == 10) {
            player.getDialogueManager().sendPlayerDialogue(Expressions.PLAIN_TALKING, "What's the problem?");
            stage = 11;
        } else if(stage == 11) {
            player.getDialogueManager().sendNPCDialogue(npcId, Expressions.PLAIN_TALKING, "Well the order has become quite diminished over the years,", "it's a very long process to learn the skills of a Void Knight.", "Recently there have been breaches into our realm from", "somewhere else, and strange creatures have been...");
            stage = 12;
        } else if(stage == 12) {
            player.getDialogueManager().sendNPCDialogue(npcId, Expressions.PLAIN_TALKING, "pouring through. We can't let that happen,", "and we'd be very grateful if you'd help us.");
            stage = 13;
        } else if(stage == 13) {
            player.getDialogueManager().sendOptionsDialogue(DialogueManager.DEFAULT_OPTIONS_TITLE, "How can I help?", "Sorry, but I can't");
            stage = 14;
        } else if(stage == 14) {
            if(optionId == DialogueManager.OPTION_1) {
                player.getDialogueManager().sendPlayerDialogue(Expressions.PLAIN_TALKING, "How can I help?");
                stage = 15;
            } else {
                player.getDialogueManager().sendPlayerDialogue(Expressions.PLAIN_TALKING, "Sorry, but I can't");
                stage = -2;
            }
        } else if(stage == 15) {
            player.getDialogueManager().sendNPCDialogue(npcId, Expressions.PLAIN_TALKING, "We send launchers from our outpost to the nearby islands.", "If you go and wait in the lander there then that'd really help.");
            stage = -2;
        } else if(stage == 16) {
            player.getDialogueManager().sendNPCDialogue(npcId, Expressions.PLAIN_TALKING, "It is the name that Guthix gave to this world,", "so we honour him with its use.");
            stage = -2;
        } else if(stage == 17) {
            player.getDialogueManager().sendNPCDialogue(npcId, Expressions.PLAIN_TALKING, "To the Void Knight outpost.", "It's a small island just off the coast of Karamja.");
            stage = 18;
        } else if(stage == 18) {
            player.getDialogueManager().sendOptionsDialogue(DialogueManager.DEFAULT_OPTIONS_TITLE, "I'd like to go to your outpost.", "That's nice.");
            stage = 19;
        } else if(stage == 19) {
            if(optionId == DialogueManager.OPTION_1) {
                player.getDialogueManager().sendPlayerDialogue(Expressions.PLAIN_TALKING, "I'd like to go to your outpost.");
                stage = 20;
            } else {
                player.getDialogueManager().sendPlayerDialogue(Expressions.PLAIN_TALKING, "That's nice.");
                stage = -2;
            }
        } else if(stage == 20) {
            player.getDialogueManager().sendNPCDialogue(npcId, Expressions.PLAIN_TALKING, "Certainly, right this way.");
            stage = 21;
        } else if(stage == 21) {
            end();
            Lander.canEnter(player, getIndex());
        } else
            end();
    }


    private int getIndex() {
        switch (npcId) {
            case 3802:
                return 0;
            case 6140:
                return 1;
            case 6141:
                return 2;
        }
        return -1;
    }
}