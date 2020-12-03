package com.fury.game.content.dialogue.impl.misc;

import com.fury.game.entity.character.player.content.BankPin;
import com.fury.game.content.dialogue.Expressions;
import com.fury.game.content.dialogue.DialogueManager;
import com.fury.game.content.dialogue.Dialogue;

/**
 * Created by Greg on 04/12/2016.
 */
public class BankPinD extends Dialogue {
    @Override
    public void start() {
        if(player.getBankPinAttributes().hasBankPin()) {
            player.getDialogueManager().sendNPCDialogue(494, Expressions.NORMAL, "You currently have a bank PIN set.", "Would you like to delete it?");
        } else {
            player.getDialogueManager().sendNPCDialogue(494, Expressions.NORMAL, "You do not have a bank PIN set.", "Would you like to set one?");
            stage = 0;
        }

    }

    @Override
    public void run(int optionId) {
        if(stage == -1) {
            player.getDialogueManager().sendOptionsDialogue(DialogueManager.DEFAULT_OPTIONS_TITLE, "Delete bank PIN.", "Cancel.");
            stage = 2;
        } else if(stage == 0) {
            player.getDialogueManager().sendOptionsDialogue(DialogueManager.DEFAULT_OPTIONS_TITLE, "Configure bank PIN.", "Cancel");
            stage = 1;
        } else if(stage == 1) {
            end();
            if(optionId == DialogueManager.OPTION_1)
                BankPin.deletePin(player);
        } else if(stage == 2) {
            end();
            if(optionId == DialogueManager.OPTION_1)
                BankPin.init(player);
        }
    }
}