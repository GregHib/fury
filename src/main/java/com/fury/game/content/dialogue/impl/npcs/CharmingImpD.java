package com.fury.game.content.dialogue.impl.npcs;

import com.fury.game.content.dialogue.DialogueManager;
import com.fury.game.content.dialogue.Dialogue;
import com.fury.game.content.skill.member.summoning.CharmingImp;

/**
 * Created by Greg on 03/12/2016.
 */
public class CharmingImpD extends Dialogue {
    @Override
    public void start() {
        player.getDialogueManager().sendOptionsDialogue(DialogueManager.DEFAULT_OPTIONS_TITLE, "Configure Gold Charms", "Configure Green Charms", "Configure Crimson Charms", "Configure Blue Charms", "Cancel");
    }

    @Override
    public void run(int optionId) {
        if(stage == -1) {
            if(optionId == DialogueManager.OPTION_5) {
                end();
                return;
            }
            player.getDialogueManager().sendOptionsDialogue(DialogueManager.DEFAULT_OPTIONS_TITLE, "Place all charms into my inventory", "Convert charms into summoning experience", "Cancel");
            if(optionId == DialogueManager.OPTION_1) {
                stage = 0;
            } else if(optionId == DialogueManager.OPTION_2) {
                stage = 1;
            } else if(optionId == DialogueManager.OPTION_3) {
                stage = 2;
            } else if(optionId == DialogueManager.OPTION_4) {
                stage = 3;
            }
        } else if(stage == 0) {
            end();
            if(optionId == DialogueManager.OPTION_1) {
                CharmingImp.changeConfig(player, 0, 0);
            } else if(optionId == DialogueManager.OPTION_2) {
                CharmingImp.changeConfig(player, 0, 1);
            }
        } else if(stage == 1) {
            end();
            if(optionId == DialogueManager.OPTION_1) {
                CharmingImp.changeConfig(player, 1, 0);
            } else if(optionId == DialogueManager.OPTION_2) {
                CharmingImp.changeConfig(player, 1, 1);
            }
        } else if(stage == 2) {
            end();
            if(optionId == DialogueManager.OPTION_1) {
                CharmingImp.changeConfig(player, 2, 0);
            } else if(optionId == DialogueManager.OPTION_2) {
                CharmingImp.changeConfig(player, 2, 1);
            }
        } else if(stage == 3) {
            end();
            if(optionId == DialogueManager.OPTION_1) {
                CharmingImp.changeConfig(player, 3, 0);
            } else if(optionId == DialogueManager.OPTION_2) {
                CharmingImp.changeConfig(player, 3, 1);
            }
        }
    }
}