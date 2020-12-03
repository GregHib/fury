package com.fury.game.content.dialogue.impl.misc;

import com.fury.game.content.dialogue.Expressions;
import com.fury.game.content.dialogue.DialogueManager;
import com.fury.game.content.dialogue.Dialogue;
import com.fury.game.content.dialogue.input.impl.EnterFriendToRefer;

public class ReferAFriendD extends Dialogue {

    boolean failed = false;

    @Override
    public void start() {
        failed = (Boolean) getParameters()[0];

        if(failed) {
            player.getDialogueManager().sendNPCDialogue(949, Expressions.NORMAL, "I'm sorry a player with that name doesn't exist", "would you like to try again?");
            stage = -1;
        } else {
            end();
            player.setInputHandling(new EnterFriendToRefer());
            player.getPacketSender().sendEnterInputPrompt("Enter name of friend to refer:");
        }
    }

    @Override
    public void run(int optionId) {
        switch (stage) {
            case -1:
                player.getDialogueManager().sendOptionsDialogue(DialogueManager.DEFAULT_OPTIONS_TITLE, "Yes, please", "No thanks");
                stage = 0;
                break;
            case 0:
                end();
                if(optionId == DialogueManager.OPTION_1) {
                    player.getDialogueManager().startDialogue(new ReferAFriendD(), false);
                } else {
                    player.getDialogueManager().startDialogue(new GamemodeD());
                }
                break;
        }
    }

}
