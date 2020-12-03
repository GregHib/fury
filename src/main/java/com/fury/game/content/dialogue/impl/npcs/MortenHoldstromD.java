package com.fury.game.content.dialogue.impl.npcs;

import com.fury.game.content.dialogue.Dialogue;
import com.fury.core.model.item.Item;

public class MortenHoldstromD extends Dialogue {
    @Override
    public void start() {
        if(player.getInventory().containsAmount(new Item(10815, 50))) {
            sendNpc(5510, "I'll trade 50 of those hairs for a shiny helmet?");
            stage = 0;
        } else
            sendNpc(5510, "Bring me 50 noted yak hairs and I'll", "swap it for a nice shiny helmet.");
    }

    @Override
    public void run(int optionId) {
        if(stage == 0) {
            sendOptions("Yes please", "No thanks");
            stage = 1;
        } else if(stage == 1) {
            end();
            if(optionId == OPTION_1)
                if(player.getInventory().delete(new Item(10815, 50)))
                    player.getInventory().addSafe(new Item(10828));
        } else
            end();
    }
}
