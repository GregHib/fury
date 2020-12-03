package com.fury.game.tools;

import com.fury.cache.Revision;
import com.fury.cache.def.Loader;
import com.fury.cache.def.anim.GameAnimation;
import com.fury.cache.def.item.ItemDefinition;
import com.fury.game.GameLoader;

public class FindAnimUsingItem {
    public static void main(String[] args) throws Exception {
        GameLoader.getCache().init();

        Loader.init();

        ItemDefinition itemToFind = Loader.getItem(1351, Revision.RS2);

        for (int index = 0; index < Loader.getTotalAnimations(itemToFind.revision); index++) {

            GameAnimation anim = Loader.getAnimation(index, itemToFind.revision);
            if(anim.playerMainhand != 0 && anim.playerMainhand == itemToFind.id + 512)
                System.out.println("Found: " + index);
            if(anim.playerOffhand != 0 && anim.playerOffhand == itemToFind.id + 512)
                System.out.println("Found: " + index);
        }
    }
}
