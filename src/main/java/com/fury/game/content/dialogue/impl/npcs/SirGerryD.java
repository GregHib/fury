package com.fury.game.content.dialogue.impl.npcs;

import com.fury.game.content.dialogue.Expressions;
import com.fury.game.content.dialogue.Dialogue;
import com.fury.game.content.global.config.ConfigConstants;
import com.fury.core.model.item.Item;

public class SirGerryD extends Dialogue {
    @Override
    public void start() {
        player.getDialogueManager().sendNPCDialogue(6201, Expressions.LOOK_DOWN, "Be careful adventurer", "these are slippery slopes you step.");
    }

    @Override
    public void run(int optionId) {
        if(stage == -1) {
            if (player.getConfig().get(ConfigConstants.GODWARS_ENTRANCE) <= 0)
                player.getConfig().send(ConfigConstants.GODWARS_ENTRANCE, 0);
            if(!player.hasItemOnThem(954)) {
                player.getDialogueManager().sendNPCDialogue(6201, Expressions.PLAIN_TALKING, "Take this to help you on your journey.");
                stage = 0;
                player.getInventory().addSafe(new Item(954));
            } else
                end();
        } else
            end();
    }
}