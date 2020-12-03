package com.fury.game.content.dialogue.impl.minigames.warriorsguild;

import com.fury.game.content.dialogue.Expressions;
import com.fury.game.content.dialogue.Dialogue;
import com.fury.game.content.global.minigames.impl.WarriorsGuild;
import com.fury.core.model.item.Item;

/**
 * Created by Greg on 04/12/2016.
 */
public class WarriorsGuildD extends Dialogue {
    @Override
    public void start() {
        int defender = WarriorsGuild.getDefender(player);
        player.getDialogueManager().sendNPCDialogue(2948, Expressions.NORMAL, "I'll release some Cyclops which might drop", "" + new Item(defender).getName().replace(" defender", "") + " Defenders for you.", "Good luck warrior!");
    }

    @Override
    public void run(int optionId) {
        end();
    }
}