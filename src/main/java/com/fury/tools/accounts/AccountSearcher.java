package com.fury.tools.accounts;

import com.fury.cache.def.Loader;
import com.fury.game.system.files.loaders.item.ItemDefinitions;
import com.fury.core.model.node.entity.actor.figure.player.Player;
import com.fury.tools.accounts.Utils.SearchUtils;

/**
 * Created by Greg on 11/09/2016.
 * Searches a single account for an item
 */
public class AccountSearcher {

    public static void main(String[] args) {
        ItemDefinitions.init();
        int itemId = 1127;
        String usersName = "Greg";

        Player singlePlayer = SearchUtils.getPlayerFromName(usersName);
        String itemName = Loader.getItem(itemId).getName();
        System.out.println("Player " + singlePlayer.getUsername() +  " " + (SearchUtils.doesPlayerHaveItem(singlePlayer, itemId) ? "does" : "doesn't") + " have item " + itemId + " (" + itemName + ")");
        System.out.println("Done");
    }
}
