package com.fury.game.content.dialogue.impl.npcs;

import com.fury.game.content.misc.objects.DwarfMultiCannon;
import com.fury.game.content.dialogue.Dialogue;
import com.fury.game.content.dialogue.Expressions;
import com.fury.core.model.item.Item;

public class NulodionD extends Dialogue {

    private static final int nulodion = 209;

    @Override
    public void start() {
        sendPlayerChat(Expressions.SAD, "I've lost my cannon.");
    }

    @Override
    public void run(int optionId) {
        if(stage == -1) {
            if(player.getDecayedCannons().isEmpty()) {
                sendNpc(nulodion, "Oh dear, I'm only allowed to replace cannons", "that were stolen in action. I'm sorry,", "but you'll have to buy a new set.");
                stage = -2;
            } else {
                sendNpc(nulodion, "Lucky for you I have a spare right here.");
                stage = 0;
            }
        } else if(stage == 0) {
            if(player.getInventory().getSpaces() < 4) {
                player.getInventory().full();
                end();
                return;
            }
            sendStatement("The dwarf hands you some replacement items.");
            DwarfMultiCannon.CannonType type = player.getDecayedCannons().poll();
            for(int piece : type.getPieces())
                player.getInventory().add(new Item(piece));
            stage = -2;
        } else
            end();
    }
}