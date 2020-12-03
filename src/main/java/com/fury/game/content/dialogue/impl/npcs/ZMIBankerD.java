package com.fury.game.content.dialogue.impl.npcs;

import com.fury.game.content.dialogue.DialogueManager;
import com.fury.game.content.dialogue.Dialogue;
import com.fury.game.content.skill.free.runecrafting.Runecrafting;

/**
 * Created by Greg on 03/12/2016.
 */
public class ZMIBankerD extends Dialogue {
    @Override
    public void start() {
        player.getDialogueManager().sendOptionsDialogue(DialogueManager.DEFAULT_OPTIONS_TITLE, "Elemental runes", "Combat runes", "Other runes", "Never mind.");
    }

    @Override
    public void run(int optionId) {
        if(stage == -1) {
            if(optionId == DialogueManager.OPTION_1) {
                player.getDialogueManager().sendOptionsDialogue(DialogueManager.DEFAULT_OPTIONS_TITLE, "Fire runes", "Water runes", "Air runes", "Earth runes", "Cancel");
                stage = 0;
            } else if(optionId == DialogueManager.OPTION_2) {
                player.getDialogueManager().sendOptionsDialogue(DialogueManager.DEFAULT_OPTIONS_TITLE, "Mind runes", "Chaos runes", "Death runes", "Blood runes", "Cancel");
                stage = 1;
            } else if(optionId == DialogueManager.OPTION_3) {
                player.getDialogueManager().sendOptionsDialogue(DialogueManager.DEFAULT_OPTIONS_TITLE, "Body runes", "Nature runes", "Law runes", "Cosmic runes", "Cancel");
                stage = 2;
            } else
                end();

        } else if(stage == 0) {
            end();
            if(optionId == DialogueManager.OPTION_1) {
                Runecrafting.zmiBank(player, 554);
            } else if(optionId == DialogueManager.OPTION_2) {
                Runecrafting.zmiBank(player, 555);
            } else if(optionId == DialogueManager.OPTION_3) {
                Runecrafting.zmiBank(player, 556);
            } else if(optionId == DialogueManager.OPTION_4) {
                Runecrafting.zmiBank(player, 557);
            }
        } else if(stage == 1) {
            end();
            if(optionId == DialogueManager.OPTION_1) {
                Runecrafting.zmiBank(player, 558);
            } else if(optionId == DialogueManager.OPTION_2) {
                Runecrafting.zmiBank(player, 562);
            } else if(optionId == DialogueManager.OPTION_3) {
                Runecrafting.zmiBank(player, 560);
            } else if(optionId == DialogueManager.OPTION_4) {
                Runecrafting.zmiBank(player, 565);
            }
        } else if(stage == 2) {
            end();
            if(optionId == DialogueManager.OPTION_1) {
                Runecrafting.zmiBank(player, 559);
            } else if(optionId == DialogueManager.OPTION_2) {
                Runecrafting.zmiBank(player, 561);
            } else if(optionId == DialogueManager.OPTION_3) {
                Runecrafting.zmiBank(player, 563);
            } else if(optionId == DialogueManager.OPTION_4) {
                Runecrafting.zmiBank(player, 564);
            } else if(optionId == DialogueManager.OPTION_5) {
            }
        }
    }
}