package com.fury.tools.accounts;

import com.fury.cache.def.Loader;
import com.fury.game.system.files.loaders.item.ItemDefinitions;
import com.fury.core.model.node.entity.actor.figure.player.Player;
import com.fury.core.model.item.Item;
import com.fury.tools.accounts.Utils.SearchLocations;
import com.fury.tools.accounts.Utils.SearchUtils;
import com.fury.util.Misc;

import java.util.List;

/**
 * Created by Greg on 11/09/2016.
 * Detailed information about players item id
 */
public class DetailedSearcher {

    public static void main(String[] args) {
        ItemDefinitions.init();

        int itemId = 1127;
        String usersName = "Greg";

        Player singlePlayer = SearchUtils.getPlayerFromName(usersName);
        String itemName = Loader.getItem(itemId).getName();
        if (SearchUtils.doesPlayerHaveItem(singlePlayer, itemId)) {
            List<SearchLocations> locs = SearchUtils.getWhere(singlePlayer, itemId);
            StringBuilder sb = new StringBuilder();
            int total = SearchUtils.getAmount(singlePlayer, itemId);
            System.out.println("Player has " + (total == 1 ? Misc.anOrA(itemName) : total) + " " + itemName + (total != 1 ? "'s" : "") + " (" + itemId + ")");
            sb.append("They have ");
            for(int i = 0; i < locs.size(); i++) {
                int amount = 0;
                switch (locs.get(i)) {
                    case IN_THEIR_INVENTORY:
                        amount = singlePlayer.getInventory().getAmount(new Item(itemId));
                        break;
                    case EQUIPPED:
                        amount = SearchUtils.getAmountBanked(singlePlayer, itemId);
                        break;
                    case BANKED:
                        amount = SearchUtils.getAmountEquipped(singlePlayer, itemId);
                        break;
                    case IN_FAMILIAR_BOB:
                        amount = SearchUtils.getAmountStored(singlePlayer, itemId);
                        break;
                }
                sb.append(amount + " ");
                sb.append(locs.get(i).name().toLowerCase().replaceAll("_", " "));
                if(locs.size() > 1 && i != locs.size() - 1)
                    sb.append(locs.size() == 2 || i == locs.size() - 2 ? " and " : ", ");
                if(i == locs.size() - 1)
                    sb.append(".");
            }
            System.out.println(sb.toString());
        } else {
            System.err.println("Error: Player doesn't have item.");
        }
        System.out.println("Done");
    }
}
