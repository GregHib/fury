package com.fury.game.cache;

import com.fury.cache.Revision;
import com.fury.cache.def.Loader;
import com.fury.cache.def.item.ItemDefinition;
import com.fury.game.GameLoader;

public class ItemFinder {
    public static void main(String[] args) throws Exception {

        GameLoader.getCache().init();

        Loader.init();

        Revision revision = Revision.PRE_RS3;
        String name = "dragon dagger";

        for (int index = 0; index < Loader.getTotalItems(revision); index++) {
            ItemDefinition def = Loader.getItem(index, revision);

            if (matches(def, name) && def.equipable())
                System.out.println("Found " + index + " " + def.getName().toLowerCase());
        }

        System.out.println("Done");
    }

    private static boolean matches(ItemDefinition def, String name) {
        return def != null && !def.noted && def.getName() != null && def.getName().toLowerCase().contains(name);
    }
}
