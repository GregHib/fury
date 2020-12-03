package com.fury.game.content.dialogue.impl.npcs;

import com.fury.game.content.dialogue.Dialogue;
import com.fury.core.model.item.Item;

/**
 * Created by Greg on 22/02/2017.
 */
public class WeirdOldManD extends Dialogue {
    @Override
    public void start() {
        if(player.hasItemOnThem(952))
            sendNpc(1152, "I like hills, do you like hills too?");
        else {
            stage = 0;
            sendNpc(1152, "Oo look I found a spade for you");
        }
    }

    @Override
    public void run(int optionId) {
        if(stage == 0)
            player.getInventory().addSafe(new Item(952));
        end();
    }
}