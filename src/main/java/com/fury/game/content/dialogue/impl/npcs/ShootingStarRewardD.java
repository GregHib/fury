package com.fury.game.content.dialogue.impl.npcs;

import com.fury.game.content.dialogue.Expressions;
import com.fury.game.content.dialogue.Dialogue;
import com.fury.core.model.item.Item;

/**
 * Created by Greg on 03/12/2016.
 */
public class ShootingStarRewardD extends Dialogue {

    @Override
    public void start() {
        int[] items = (int[]) parameters[0];
        int[] amount = (int[]) parameters[1];
        player.getDialogueManager().sendNPCDialogue(8091, Expressions.PLAIN_TALKING, "I have rewarded you by making it so you can mine",
                "extra ore for the next 15 minutes. Also, have",
                amount[0] + " cosmic runes, " + amount[1] + " astral runes, " + amount[2] + " gold ore,",
                amount[4] + " " + new Item(items[4]).getName().toLowerCase() + "s and " + amount[3] +" coins.");
    }

    @Override
    public void run(int optionId) {
        end();
    }
}