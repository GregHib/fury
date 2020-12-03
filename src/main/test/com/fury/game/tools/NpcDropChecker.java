package com.fury.game.tools;

import com.fury.cache.Revision;
import com.fury.cache.def.Loader;
import com.fury.game.GameLoader;
import com.fury.game.entity.character.npc.drops.Drop;
import com.fury.core.model.item.Item;
import com.fury.game.node.entity.actor.figure.mob.drops.MobDrops;
import com.fury.game.system.files.Resources;
import com.fury.game.world.map.Position;
import com.fury.game.world.update.flag.block.Direction;
import com.fury.util.JsonLoader;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.io.File;

public class NpcDropChecker {
    public static void main(String[] args) throws Exception {
        GameLoader.getCache().init();

        Loader.init();
//Na￯﾿ﾯve
        MobDrops.init();

        int idToFind = 2572;
        File[] spawns = new File(Resources.getDirectory("spawns")).listFiles();
        for(File file : spawns) {

            new JsonLoader() {
                @Override
                public void load(JsonObject reader, Gson builder) {
                    int id = reader.get("npc-id").getAsInt();
                    Position position = builder.fromJson(reader.get("position").getAsJsonObject(), Position.class);
                    int walk = reader.get("walk").getAsInt();
                    Direction direction = Direction.valueOf(reader.get("face").getAsString());
                    Revision revision = Revision.RS2;
                    if (reader.has("revision"))
                        revision = Revision.valueOf(reader.get("revision").getAsString());

                    Drop[] drops = MobDrops.getDrops(id, revision);
                    if (drops == null)
                        return;

                    for(Drop drop : drops) {
                        if(drop.getItemId() == idToFind) {
                            Item item = new Item(drop.getItemId());
//                    if (!item.getDefinition().isStackable() && (drop.getMinAmount() >= 100 || drop.getMaxAmount() >= 100)) {
                            System.out.println(item.getName() + " " + drop.getMaxAmount() + " " + Loader.getNpc(id, Revision.RS2).getName() + " " + id);
//                    }
                        }
                    }
                }

                @Override
                public String filePath() {
                    return file.getPath();
                }
            }.load();
        }

        for (int index = 0; index < Loader.getTotalNpcs(Revision.PRE_RS3); index++) {

            Drop[] drops = MobDrops.getDrops(index, Revision.PRE_RS3);
            if (drops == null)
                continue;
            for(Drop drop : drops) {
                if(drop.getItemId() == idToFind) {
                    Item item = new Item(drop.getItemId());
//                    if (!item.getDefinition().isStackable() && (drop.getMinAmount() >= 100 || drop.getMaxAmount() >= 100)) {
                    System.out.println(item.getName() + " " + drop.getMaxAmount() + " " + Loader.getNpc(index, Revision.PRE_RS3).getName() + " " + index);
//                    }
                }
            }
        }
    }
}
