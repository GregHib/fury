package com.fury.game.content.dialogue.impl.npcs;

import com.fury.game.entity.character.player.content.LoyaltyProgramme;
import com.fury.game.content.dialogue.Expressions;
import com.fury.game.content.dialogue.DialogueManager;
import com.fury.game.content.dialogue.Dialogue;

/**
 * Created by Greg on 14/11/2016.
 */
public class XuanD  extends Dialogue {
    int xuan = 13727;

    @Override
    public void start() {
        player.getDialogueManager().sendNPCDialogue(xuan, Expressions.NORMAL, "Good day, good day! How can I help you?");
    }

    @Override
    public void run(int optionId) {
        if(stage == -1) {
            player.getDialogueManager().sendOptionsDialogue(DialogueManager.DEFAULT_OPTIONS_TITLE, "Can you show me your loyalty shop?", "Can you reset my loyalty title for me?", "Will you tell me about loyalty points?", "Cancel");
            stage = 0;
        } else if(stage == 0) {
            if(optionId == DialogueManager.OPTION_1) {
                end();
                LoyaltyProgramme.open(player);
            } else if(optionId == DialogueManager.OPTION_2) {
                LoyaltyProgramme.reset(player);
                end();
            } else if(optionId == DialogueManager.OPTION_3) {
                player.getDialogueManager().sendNPCDialogue(xuan, Expressions.NORMAL, "Why of course!", "Loyalty points are gradually given once every", "few seconds while you are online, the more points", "you have the better rewards you can purchase!");
                stage = 1;
            } else if(optionId == DialogueManager.OPTION_4) {
                end();
            }
        } else if(stage == 1) {
            player.getDialogueManager().sendNPCDialogue(xuan, Expressions.NORMAL, "Is there anything else I can help you with?");
            stage = 2;
        } else if(stage == 2) {
            player.getDialogueManager().sendOptionsDialogue(DialogueManager.DEFAULT_OPTIONS_TITLE, "Can you show me your loyalty shop?", "Can you reset my loyalty title for me?", "Will you tell me about loyalty points?", "Cancel");
            stage = 0;
        }
    }
}
