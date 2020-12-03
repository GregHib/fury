package com.fury.game.content.dialogue.impl.npcs;

import com.fury.game.content.dialogue.Expressions;
import com.fury.game.content.dialogue.Dialogue;
import com.fury.core.model.item.Item;

public class IronManD extends Dialogue {
    @Override
    public void start() {
        player.getDialogueManager().sendNPCDialogue(792, Expressions.PLAIN_TALKING, "Welcome to the ironman zone!");
    }

    @Override
    public void run(int optionId) {
        if(stage == -1 && !player.hasItem(16)) {
            player.getDialogueManager().sendNPCDialogue(792, Expressions.PLAIN_TALKING, "Take this whistle for quick access back here.");
            player.getInventory().addSafe(new Item(16));
            stage = 0;
        } else
            end();
    }
}