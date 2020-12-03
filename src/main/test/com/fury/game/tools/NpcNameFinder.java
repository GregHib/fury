package com.fury.game.tools;

import com.fury.cache.Revision;
import com.fury.cache.def.Loader;
import com.fury.cache.def.npc.NpcDefinition;
import com.fury.game.GameLoader;
import com.fury.game.node.entity.actor.figure.mob.drops.MobDrops;

public class NpcNameFinder {
    public static void main(String[] args) throws Exception {
        GameLoader.getCache().init();

        Loader.init();
//Na￯﾿ﾯve
        MobDrops.init();

        String nameToFind = "banshe";

        for (int index = 0; index < Loader.getTotalNpcs(Revision.PRE_RS3); index++) {
            NpcDefinition def = Loader.getNpc(index, Revision.PRE_RS3);

            if(def != null) {
                if(def.getName() != null) {
                    if(def.getName().equalsIgnoreCase(nameToFind) || def.getName().contains(nameToFind))
                        System.out.println(index + " " + def.getName());
                }
            }
        }
    }
}
