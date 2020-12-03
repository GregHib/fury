package com.fury.game.tools;

import com.fury.cache.def.Loader;
import com.fury.game.GameLoader;
import com.fury.game.content.skill.member.slayer.SlayerTask;
import com.fury.game.system.files.Resources;
import com.fury.game.system.files.loaders.npc.MobSpawn;
import com.fury.game.system.files.loaders.npc.MobSpawns;

import java.io.File;

public class NpcSpawnFinder {
    public static void main(String[] args) throws Exception {
        GameLoader.getCache().init();

        Loader.init();

        MobSpawns.loadAll();

        int npcId = 1360;

        File[] files = new File(Resources.getDirectory("spawns")).listFiles();
        for (File file : files)
            if (file.isFile() && file.getName().endsWith(".txt")) {
                int regionid = Integer.parseInt(file.getName().substring(0, file.getName().length() - 4));
                int x = (regionid >> 8);
                int y = (regionid & 0xff);
                if (MobSpawns.getSpawns()[x][y] == null)
                    return;

                for (MobSpawn spawn : MobSpawns.getSpawns()[x][y]) {
                    if(spawn.getId() == npcId)
                        System.out.println(spawn.getTile());
                }
            }
    }

    private static boolean check(String name, SlayerTask task) {
        if (name.toLowerCase().contains(task.toString().replace("_", " ").toLowerCase()))
            return true;
        return false;
    }

    private static boolean valid(String name) {
        for (SlayerTask task : SlayerTask.values()) {
            if (name.toLowerCase().contains(task.toString().replace("_", " ").toLowerCase()))
                return true;
        }
        return false;
    }
}
