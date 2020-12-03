package com.fury.game.content.dialogue.impl.misc;

import com.fury.game.content.dialogue.DialogueManager;
import com.fury.game.content.dialogue.Dialogue;
import com.fury.core.model.item.Item;
import com.fury.game.content.misc.items.random.impl.imps.MysteryBoxTimedGen;

/**
 * Created by Greg on 04/02/2017.
 */
public class EmptyInventoryD extends Dialogue {
    @Override
    public void start() {
        player.getDialogueManager().sendDialogue("Are you sure you want to clear your inventory?", "Once done there is no getting your items back.");
    }

    @Override
    public void run(int optionId) {
        if (stage == -1) {
            player.getDialogueManager().sendOptionsDialogue("Are you sure you want to do this?", "Yes", "No");
            stage = 0;
        } else if (stage == 0) {
            end();

            boolean giveBox = false;
            if (optionId == DialogueManager.OPTION_1) {
                for (Item item : player.getInventory().getItems())
                    if (item != null) {
                        if (item.getId() == MysteryBoxTimedGen.getBoxId()) {
                            if (player.getBank().tab().hasRoom()) {
                                player.getBank().tab().add(item);
                                player.message("Your mystery box has been put in your bank.");
                            } else
                                giveBox = true;
                        }
                        player.getLogger().addEmpty(item);
                    }

                player.message("You clear your inventory.", true);
                player.stopAll(false);
                player.getInventory().clear();

                if (giveBox) {
                    player.getInventory().add(new Item(MysteryBoxTimedGen.getBoxId()));
                    player.message("Your bank was full, so your mystery box was preserved.");
                }
                player.getInventory().refresh();


            }
        }
    }
}
