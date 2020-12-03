package com.fury.tools.accounts.Utils;

import com.fury.core.model.node.entity.actor.figure.player.Player;
import com.fury.game.entity.character.player.PlayerLoading;
import com.fury.core.model.item.Item;
import com.fury.network.login.LoginResponses;
import com.fury.util.Misc;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Greg on 16/12/2016.
 */
public class SearchUtils {

    public static List<SearchLocations> getWhere(Player player, int itemId) {
        List<SearchLocations> locations = new ArrayList<>();
        if(player.getInventory().contains(new Item(itemId)))
            locations.add(SearchLocations.IN_THEIR_INVENTORY);

        if(hasBanked(player, itemId))
            locations.add(SearchLocations.BANKED);

        if(hasEquipped(player, itemId))
            locations.add(SearchLocations.EQUIPPED);

        if(familiarHasItem(player, itemId))
            locations.add(SearchLocations.IN_FAMILIAR_BOB);

        return locations;
    }

    public static int getAmount(Player player, int itemId) {
        int total = 0;
        if(player.getInventory().contains(new Item(itemId)))
            total += player.getInventory().getAmount(new Item(itemId));

        if(hasBanked(player, itemId))
            total += getAmountBanked(player, itemId);

        if(hasEquipped(player, itemId))
            total += getAmountEquipped(player, itemId);

        if(familiarHasItem(player, itemId))
            total += getAmountStored(player, itemId);

        return total;
    }

    public static int getAmountStored(Player player, int itemId) {
        int total = 0;
        Item item = new Item(itemId);
        if(player.getFamiliar() != null && player.getFamiliar().getBeastOfBurden() != null && player.getFamiliar().getBeastOfBurden().contains(item))
            total += player.getFamiliar().getBeastOfBurden().getAmount(item);
        return total;
    }

    public static int getAmountEquipped(Player player, int itemId) {
        int total = 0;
        for(int i = 0; i < 15; i++)
            if(player.getEquipment().get(i).getId() == itemId)
                total += player.getEquipment().get(i).getAmount();
        return total;
    }

    public static int getAmountBanked(Player player, int itemId) {
        return player.getBank().contains(new Item(itemId)) ? player.getBank().get(new Item(itemId)).getAmount() : 0;
    }

    public static Player getPlayerFromName(String name) {
        if(Misc.playerExists(name)) {
            Player player = new Player();
            player.setUsername(name);
            if (PlayerLoading.getResult(player) == LoginResponses.LOGIN_COULD_NOT_COMPLETE)
                return null;
            return player;
        }
        return null;
    }

    public static boolean doesPlayerHaveItem(Player player, int itemId) {
        if(player.getInventory().contains(new Item(itemId)))
            return true;

        if(hasBanked(player, itemId))
            return true;

        if(hasEquipped(player, itemId))
            return true;

        if(familiarHasItem(player, itemId))
            return true;

        return false;
    }

    public static boolean hasEquipped(Player player, int itemId) {
        for(int i = 0; i < 15; i++)
            if(player.getEquipment().get(i).getId() == itemId)
                return true;
        return false;
    }

    public static boolean hasBanked(Player player, int itemId) {
        return player.getBank().contains(new Item(itemId));
    }

    public static boolean familiarHasItem(Player player, int itemId) {
        if(player.getFamiliar() != null && player.getFamiliar().getBeastOfBurden() != null && player.getFamiliar().getBeastOfBurden().contains(new Item(itemId)))
            return true;
        return false;
    }
}
