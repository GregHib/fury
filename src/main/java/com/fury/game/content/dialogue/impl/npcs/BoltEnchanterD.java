package com.fury.game.content.dialogue.impl.npcs;

import com.fury.game.content.dialogue.Expressions;
import com.fury.game.content.dialogue.DialogueManager;
import com.fury.game.content.dialogue.Dialogue;
import com.fury.game.content.dialogue.input.impl.EnterAmountToEnchant;
import com.fury.core.model.item.Item;

/**
 * Created by Greg on 22/02/2017.
 */
public class BoltEnchanterD extends Dialogue {
    @Override
    public void start() {
        player.getDialogueManager().sendNPCDialogue(8239, Expressions.NORMAL, "I can enchant any type of bolts for a small fee...");
    }

    @Override
    public void run(int optionId) {
        int boltId = -1;
        if(stage == -1) {
            player.getDialogueManager().sendNPCDialogue(8239, Expressions.NORMAL,"What bolts would you like me to enchant?");
            stage = 0;
        } else if(stage == 0) {
            player.getDialogueManager().sendOptionsDialogue(DialogueManager.DEFAULT_OPTIONS_TITLE, "Opal - 100gp", "Sapphire - 200gp", "Jade - 300gp", "Pearl - 400gp", "More");
            stage = 1;
        } else if(stage == 1) {
            if(optionId == DialogueManager.OPTION_1) {
                boltId = 879;
            } else if(optionId == DialogueManager.OPTION_2) {
                boltId = 9337;
            } else if(optionId == DialogueManager.OPTION_3) {
                boltId = 9335;
            } else if(optionId == DialogueManager.OPTION_4) {
                boltId = 880;
            } else if(optionId == DialogueManager.OPTION_5) {
                player.getDialogueManager().sendOptionsDialogue(DialogueManager.DEFAULT_OPTIONS_TITLE, "Emerald - 500gp", "Red Topaz - 500gp", "Ruby - 700gp", "Diamond - 800gp", "More");
                stage = 2;
            }
        } else if(stage == 2) {
            if(optionId == DialogueManager.OPTION_1) {
                boltId = 9338;
            } else if(optionId == DialogueManager.OPTION_2) {
                boltId = 9336;
            } else if(optionId == DialogueManager.OPTION_3) {
                boltId = 9339;
            } else if(optionId == DialogueManager.OPTION_4) {
                boltId = 9340;
            } else if(optionId == DialogueManager.OPTION_5) {
                player.getDialogueManager().sendOptionsDialogue(DialogueManager.DEFAULT_OPTIONS_TITLE, "Dragonstone - 900gp", "Onyx - 1000gp", "", "", "Back");
                stage = 3;
            }
        } else if(stage == 3) {
            if(optionId == DialogueManager.OPTION_1) {
                boltId = 9341;
            } else if(optionId == DialogueManager.OPTION_2) {
                boltId = 9342;
            } else if(optionId == DialogueManager.OPTION_5) {
                player.getDialogueManager().sendOptionsDialogue(DialogueManager.DEFAULT_OPTIONS_TITLE, "Opal - 100gp", "Sapphire - 200gp", "Jade - 300gp", "Pearl - 400gp", "More");
                stage = 1;
            }
        }
        if(boltId != -1) {
            player.getTemporaryAttributes().put("bolt_enchant_selection", boltId);
            end();
            player.getPacketSender().sendEnterAmountPrompt("How many " + new Item(boltId).getName() + " would you like to enchant?");
            player.setInputHandling(new EnterAmountToEnchant());
        }
    }
}