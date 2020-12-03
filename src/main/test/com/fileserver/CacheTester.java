package com.fileserver;

import com.fury.cache.Revision;
import com.fury.cache.def.Loader;
import com.fury.game.GameLoader;
import com.fury.game.entity.character.npc.drops.Drop;
import com.fury.core.model.item.Item;
import com.fury.game.node.entity.actor.figure.mob.drops.MobDrops;
import com.fury.game.system.files.loaders.item.ItemDefinitions;

import java.util.ArrayList;

public class CacheTester {

    public static void main(String[] args) throws Exception {
        GameLoader.getCache().init();

        Loader.init();

        System.out.println(Loader.getItem(19709, Revision.PRE_RS3).primaryMaleModel);
        System.out.println(Loader.getItem(19709, Revision.PRE_RS3).primaryFemaleModel);
    }

    public static void main2(String[] args) throws Exception {
        GameLoader.getCache().init();

        Loader.init();

        MobDrops.init();

        ItemDefinitions.init();

        Drop[] drops = MobDrops.getDrops(13481);

        if(drops == null) {
            System.err.println("Null drops");
            return;
        }

        ArrayList<Item> items = new ArrayList<>();

        for (Drop drop : drops) {
            items.add(new Item(drop.getItemId(), drop.getMaxAmount(), drop.getRevision()));
        }

        items.sort((first, second) -> (second.getDefinitions().getValue() * second.getAmount()) - (first.getDefinitions().getValue() * first.getAmount()));

        items.forEach(item -> System.out.println(item.getName() + " (" + item.getId() + ") " + item.getDefinitions().getValue() + " " + (item.getDefinitions().getValue() * item.getAmount())));

    }
}
