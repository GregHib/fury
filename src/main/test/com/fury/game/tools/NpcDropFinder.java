package com.fury.game.tools;

import com.fury.cache.Revision;
import com.fury.cache.def.Loader;
import com.fury.cache.def.npc.NpcDefinition;
import com.fury.game.GameLoader;
import com.fury.game.content.skill.member.slayer.SlayerTask;
import com.fury.game.entity.character.npc.drops.Drop;
import com.fury.game.node.entity.actor.figure.mob.drops.MobDrops;
import com.fury.game.system.files.Resources;
import com.fury.game.system.files.loaders.npc.MobSpawns;
import com.fury.game.world.map.Position;
import com.fury.game.world.update.flag.block.Direction;
import com.fury.util.Logger;
import com.fury.util.Misc;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;

public class NpcDropFinder {
    public static void main(String[] args) throws Exception {
        GameLoader.getCache().init();

        Loader.init();
//Na￯﾿ﾯve
        MobDrops.init();

        Map<Integer, Object[]> defs = new HashMap<>();
        File[] spawns = new File(Resources.getDirectory("spawns")).listFiles();
        for(File file : spawns) {
            try {
                BufferedReader in = new BufferedReader(new FileReader(file));
                int count = 0;
                while (true) {
                    count++;
                    String line = in.readLine();
                    if (line == null)
                        break;
                    if (line.startsWith("//") || line.startsWith("RSBOT"))
                        continue;
                    Matcher matcher = MobSpawns.pattern.matcher(line);
                    if (matcher.matches()) {
                        //process using values
                        String[] values = Misc.getValues(matcher);

                        if (values.length != 8) {
                            in.close();
                            throw new RuntimeException("Invalid Npc Spawn line: " + line + " , line number: " + count);
                        }

                        int id = Integer.parseInt(values[1]);
                        Position position = new Position(Integer.parseInt(values[2]), Integer.parseInt(values[3]), Integer.parseInt(values[4]));
                        int walk = Integer.parseInt(values[5]);
                        Direction direction = Direction.valueOf(values[6]);
                        Revision revision = Revision.values()[Integer.parseInt(values[7])];

                        NpcDefinition def = Loader.getNpc(id, revision);
                        if (def != null) {
                            if (def.getName() != null) {
                                if (valid(def.getName()))
                                    defs.put(id, new Object[]{def, position});
                            }
                        }
                    } else {
                        in.close();
                        throw new RuntimeException("Invalid Npc Spawn line: " + line + " , line number: " + count);
                    }
                }
                in.close();
            } catch (Throwable e) {
                Logger.handle(e);
            }
        }

        for(int id : defs.keySet()) {
            Drop[] drops = MobDrops.getDrops(id, Revision.RS2);
            if(drops == null)
                continue;
            for(Drop drop : drops) {
                if(drop.getItemId() == 2513)
                    System.out.println(id);
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
