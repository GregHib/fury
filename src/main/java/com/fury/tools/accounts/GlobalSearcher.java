package com.fury.tools.accounts;

import com.fury.cache.def.Loader;
import com.fury.game.system.files.loaders.item.ItemDefinitions;
import com.fury.core.model.node.entity.actor.figure.player.Player;
import com.fury.game.entity.character.player.PlayerLoading;
import com.fury.game.system.files.Resources;
import com.fury.tools.accounts.Utils.SearchUtils;
import com.fury.util.Misc;

import java.io.File;

/**
 * Created by Greg on 11/09/2016.
 * Searches all accounts for an item
 */
public class GlobalSearcher {

    public static void main(String[] args) {
        Resources.init();
        ItemDefinitions.init();

        int itemId = 1127;

        String itemName = Loader.getItem(itemId).getName();
        File folder = new File(Resources.getSaveDirectory("characters"));
        File[] listOfFiles = folder.listFiles();

        for (int i = 0; i < listOfFiles.length; i++) {
            if (listOfFiles[i].isFile()) {
                String name = listOfFiles[i].getName();
                Player player = new Player();
                player.setUsername(name.substring(0, name.length()-5));
                PlayerLoading.getResult(player);
                boolean has = SearchUtils.doesPlayerHaveItem(player, itemId);
                if(has)
                    System.out.println("Player " + player.getUsername() + " has " + Misc.anOrA(itemName) + " " + itemName);
            }
        }
        System.out.println("Done");
    }
}
