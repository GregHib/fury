package com.fury.game.content.dialogue.impl.misc;

import com.fury.game.content.dialogue.Dialogue;
import com.fury.game.content.dialogue.DialogueManager;
import com.fury.game.entity.character.player.content.LoyaltyProgramme;
import com.fury.core.model.item.Item;

/**
 * Created by Greg on 03/12/2016.
 */
public class VeteranCapeD extends Dialogue {
    @Override
    public void start() {
        player.getDialogueManager().sendOptionsDialogue(DialogueManager.DEFAULT_OPTIONS_TITLE, "I'd like to buy a Veteran Cape (15m)", "Cancel");
    }

    @Override
    public void run(int optionId) {
        if(optionId == DialogueManager.OPTION_1) {
            player.getPacketSender().sendInterfaceRemoval();
            if (!player.getUnlockedLoyaltyTitles()[LoyaltyProgramme.LoyaltyTitles.VETERAN.ordinal()]) {
                player.message("You must have unlocked the 'Veteran' Loyalty Title in order to buy this cape.");
                return;
            }

            if(player.getInventory().removeCoins(15000000)) {
                player.getInventory().add(new Item(20763));
                player.message("You've purchased a Veteran cape.");
                player.getDialogueManager().startDialogue(new VeteranTitleD());
            }
        }
        end();
    }

}