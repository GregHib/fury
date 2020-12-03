package com.fury.game.content.dialogue.impl.npcs;

import com.fury.game.content.dialogue.Expressions;
import com.fury.game.content.dialogue.DialogueManager;
import com.fury.game.content.dialogue.Dialogue;
import com.fury.game.content.skill.free.woodcutting.BirdNests;
import com.fury.core.model.item.Item;
import com.fury.util.Misc;

public class WysonD extends Dialogue {
    @Override
    public void start() {
        if(player.getInventory().contains(new Item(7416)) || player.getInventory().contains(new Item(7418))) {
            player.getDialogueManager().sendNPCDialogue(36, Expressions.PLAIN_TALKING, "Ooo I see you have some mole remains;", "would you be interested in trading them", "I can offer some bird's nests I have?");
        } else {
            player.getDialogueManager().sendNPCDialogue(36, Expressions.PLAIN_TALKING, "Happy gardening!");
            stage = -2;
        }
    }

    @Override
    public void run(int optionId) {
        if(stage == -1) {
            player.getDialogueManager().sendOptionsDialogue(DialogueManager.DEFAULT_OPTIONS_TITLE, "Yes please", "I'm not interested thank you.");
            stage = 0;
        } else if(stage == 0) {
            if(optionId == DialogueManager.OPTION_1) {
                for(int i = 0; i < player.getInventory().capacity(); i++) {
                    Item item = player.getInventory().get(i);
                    if(item == null)
                        continue;

                    if(item.getId() == 7416 || item.getId() == 7418)
                        player.getInventory().set(new Item(BirdNests.BIRD_NEST_IDS[Misc.random(BirdNests.BIRD_NEST_IDS.length - 1)]), i);
                }
            }
            end();
        } else
            end();
    }
}