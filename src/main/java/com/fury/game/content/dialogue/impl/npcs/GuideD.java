package com.fury.game.content.dialogue.impl.npcs;

import com.fury.game.content.dialogue.Expressions;
import com.fury.game.content.dialogue.DialogueManager;
import com.fury.game.content.dialogue.Dialogue;
import com.fury.game.content.dialogue.impl.misc.ChangeGamemodeD;
import com.fury.game.content.dialogue.impl.misc.GamemodeD;
import com.fury.game.content.dialogue.input.impl.ChangePassword;
import com.fury.game.content.global.Achievements;
import com.fury.game.entity.character.player.content.BankPin;

/**
 * Created by Greg on 15/11/2016.
 */
public class GuideD extends Dialogue {
    int guide = 6782;

    @Override
    public void start() {
        player.getDialogueManager().sendNPCDialogue(guide, Expressions.NORMAL, "Hello there.", "I have the ability to help you with your", "account settings. What would you like to do?");
    }

    @Override
    public void run(int optionId) {
        if(stage == -1) {
            stage = 0;
            Achievements.finishAchievement(player, Achievements.AchievementData.TALK_WITH_GUIDE);
            player.getDialogueManager().sendOptionsDialogue(DialogueManager.DEFAULT_OPTIONS_TITLE, "Set bank-pin (used when attempting to open the bank)", "Change Password", "Change to Regular Game Mode", "Cancel");
        } else if(stage == 0) {
            if(optionId == DialogueManager.OPTION_1) {
                if (player.getBankPinAttributes().hasBankPin()) {
                    stage = 1;
                    player.getDialogueManager().sendOptionsDialogue(DialogueManager.DEFAULT_OPTIONS_TITLE, "Delete bank PIN.", "Cancel.");
                } else {
                    end();
                    BankPin.init(player);
                }
            } else if(optionId == DialogueManager.OPTION_2) {
                end();
                player.setInputHandling(new ChangePassword());
                player.getPacketSender().sendEnterInputPrompt("Enter a new password:");
            } else if(optionId == DialogueManager.OPTION_3) {
                end();
                player.getDialogueManager().startDialogue(new ChangeGamemodeD());
            }else
                end();
        } else if(stage == 1) {
            end();
            if(optionId == DialogueManager.OPTION_1) {
                BankPin.deletePin(player);
            } else if(optionId == DialogueManager.OPTION_2) {
                player.getBank().open();
            }
        } else if(stage == 2) {
            end();
            if(optionId == DialogueManager.OPTION_1) {
                player.getMovement().lock();
                player.getDialogueManager().startDialogue(new GamemodeD());
            }
        }

    }
}