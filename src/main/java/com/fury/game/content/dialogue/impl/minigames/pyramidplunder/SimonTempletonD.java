package com.fury.game.content.dialogue.impl.minigames.pyramidplunder;

import com.fury.game.content.global.minigames.impl.PyramidPlunder;
import com.fury.game.content.dialogue.Expressions;
import com.fury.game.content.dialogue.DialogueManager;
import com.fury.game.content.dialogue.Dialogue;
import com.fury.core.model.item.Item;

/**
 * Created by Greg on 05/12/2016.
 */
public class SimonTempletonD extends Dialogue {
    @Override
    public void start() {
        boolean contains = false;

        for(Item item : player.getInventory().getItems()) {
            if(item == null)
                continue;
            PyramidPlunder.UrnRewards urn = PyramidPlunder.UrnRewards.forId(item.getId());
            if (urn == null)
                continue;
            contains = true;
            break;
        }

        if(contains) {
            player.getDialogueManager().sendNPCDialogue(3123, Expressions.PLAIN_TALKING, "Ooo artifacts! Can I buy them off of you?");
            stage = 0;
        } else {
            player.getDialogueManager().sendNPCDialogue(3123, Expressions.PLAIN_TALKING, "Bring me artifacts! I'll buy them!", "1k for each Ivory, 5k - Pottery, 10k - Stone", "20k - Gold and 500k - Jewelled");
        }
    }

    @Override
    public void run(int optionId) {
        if(stage == -1) {
            end();
        } else if(stage == 0) {
            player.getDialogueManager().sendOptionsDialogue(DialogueManager.DEFAULT_OPTIONS_TITLE, "Yes, sure!", "No, they're mine!");
            stage = 1;
        } else if(stage == 1) {
            if(optionId == DialogueManager.OPTION_1) {
                for(Item item : player.getInventory().getItems()) {
                    if(item == null)
                        continue;
                    PyramidPlunder.UrnRewards urn = PyramidPlunder.UrnRewards.forId(item.getId());
                    if(urn == null)
                        continue;
                    int price = 0;
                    switch (urn) {
                        case IVORY_COMB:
                            price = 1000;
                            break;
                        case POTTERY_SCARAB:
                        case POTTERY_STATUETTE:
                            price = 5000;
                            break;
                        case STONE_SEAL:
                        case STONE_STATUETTE:
                            price = 10000;
                            break;
                        case GOLD_SEAL:
                        case GOLDEN_SCARAB:
                        case GOLDEN_STATUETTE:
                            price = 20000;
                            break;
                        case JEWELLED_GOLDEN_STATUETTE:
                            price = 500000;
                            break;
                    }
                    int coins = price * item.getAmount();
                    player.getInventory().delete(item);
                    player.getInventory().addSafe(new Item(995, coins));
                }
                stage = 2;
                player.getDialogueManager().sendNPCDialogue(3123, Expressions.UNSURE, "Thanks!");
            } else
                end();
        } else
            end();
    }
}