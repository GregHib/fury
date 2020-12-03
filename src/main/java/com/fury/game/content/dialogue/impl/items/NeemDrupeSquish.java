package com.fury.game.content.dialogue.impl.items;

import com.fury.cache.Revision;
import com.fury.game.content.dialogue.Dialogue;
import com.fury.game.content.skill.member.herblore.Herblore;
import com.fury.core.model.node.entity.actor.figure.player.Player;
import com.fury.core.model.item.Item;
import com.fury.util.Misc;

public class NeemDrupeSquish extends Dialogue {
    @Override
    public void start() {
        boolean hasOil = player.getInventory().contains(new Item(22444));
        if (!hasOil && !player.getInventory().contains(new Item(1935))) {
            player.getDialogueManager().sendItemDialogue(1935, "I should get an empty jug to hold the", "juice before I squish the neem drupe.");
        } else if (!player.getInventory().contains(Herblore.PESTLE_AND_MORTAR))
            player.getDialogueManager().sendItemDialogue(Herblore.PESTLE_AND_MORTAR.getId(), "I should get a pestle and mortar", "before I squish the neem drupe.");
        else {
            if (!hasOil) {
                player.getInventory().delete(new Item(1935));
                player.getInventory().addSafe(new Item(22444));
            }
            int count = player.getInventory().getAmount(new Item(22445));
            player.getInventory().delete(new Item(22445, count));
            player.getInventory().refresh();
            player.getDialogueManager().sendItemDialogue(22444, Revision.PRE_RS3, "You squish the neem drupe into a jug.");
        }
    }

    public static void removeCharge(Player player) {
        if (Misc.random(2000) == 0) {
            player.getInventory().delete(new Item(22444));
            player.getInventory().addSafe(new Item(1935, 1));
            player.message("There is no more oil in the jug.");
        }
    }

    @Override
    public void run(int optionId) {
        end();
    }
}