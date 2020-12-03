package com.fury.game.content.dialogue.impl.misc;

import com.fury.game.content.dialogue.Expressions;
import com.fury.game.content.dialogue.DialogueManager;
import com.fury.game.content.dialogue.Dialogue;
import com.fury.game.entity.character.player.info.PlayerRights;

/**
 * Created by Greg on 03/12/2016.
 */
public class VeteranTitleD extends Dialogue {
    @Override
    public void start() {
        player.getDialogueManager().sendNPCDialogue(0, Expressions.PLAIN_TALKING, "I can see that you've become a veteran!", "Would you like the Veteran rank?", "Warning! It will reset your current rank.");
    }

    @Override
    public void run(int optionId) {
        if(stage == -1) {
            stage = 0;
            player.getDialogueManager().sendOptionsDialogue(DialogueManager.DEFAULT_OPTIONS_TITLE, "Yes, sure!", "No thanks.");
        } else if(stage == 0) {
            if(optionId == DialogueManager.OPTION_1) {
                player.getPacketSender().sendInterfaceRemoval();
                if (player.getRights().isStaff()) {
                    player.message("You cannot change your rank.");
                    return;
                }
                player.setRights(PlayerRights.VETERAN);
                player.getPacketSender().sendRights();
            }
            end();
        }
    }
}