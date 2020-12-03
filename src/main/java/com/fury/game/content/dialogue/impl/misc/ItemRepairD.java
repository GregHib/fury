package com.fury.game.content.dialogue.impl.misc;

import com.fury.game.entity.item.content.ItemRepair;
import com.fury.game.content.dialogue.Expressions;
import com.fury.game.content.dialogue.DialogueManager;
import com.fury.game.content.dialogue.Dialogue;
import com.fury.util.Misc;

/**
 * Created by Greg on 26/11/2016.
 */
public class ItemRepairD extends Dialogue {
    @Override
    public void start() {
        int npcId = (int) parameters[0];
        ItemRepair item = (ItemRepair) parameters[1];
        player.getDialogueManager().sendNPCDialogue(npcId, Expressions.NORMAL, "It will cost you " + Misc.insertCommasToNumber(item.getCost()) + "gp (" + Misc.formatAmount(item.getCost()) + ")", "for me to repair your " + item.getName() + ".");
    }

    @Override
    public void run(int optionId) {
        if (stage == -1) {
            player.getDialogueManager().sendOptionsDialogue("Are you sure?", "Yes.", "No.");
            stage = 0;
        } else if (stage == 0) {
            if (optionId == DialogueManager.OPTION_1) {
                end();
                if (player.getInteractingItem() == null)
                    return;
                ItemRepair.handlePurchase(player, player.getInteractingItem());
            }
        }
    }
}