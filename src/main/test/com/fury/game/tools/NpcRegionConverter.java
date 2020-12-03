package com.fury.game.tools;

import com.fury.cache.Revision;
import com.fury.cache.def.Loader;
import com.fury.game.GameLoader;
import com.fury.game.node.entity.actor.figure.mob.drops.MobDrops;
import com.fury.game.system.files.Resources;
import com.fury.game.world.map.Position;
import com.fury.game.world.update.flag.block.Direction;
import com.fury.util.JsonLoader;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class NpcRegionConverter {
    public static void main(String[] args) throws Exception {
        GameLoader.getCache().init();

        Loader.init();
//Na￯﾿ﾯve
        MobDrops.init();

        File[] spawns = new File(Resources.getDirectory("spawns")).listFiles();
        for (File file : spawns) {
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


                    File file = new File(Resources.getDirectory("regions") + position.getRegionId() + ".txt");
                    BufferedWriter bw = null;
                    FileWriter fw = null;

                    boolean isNew = false;
                    try {

                        // if file doesnt exists, then create it
                        if (!file.exists()) {
                            file.createNewFile();
                            isNew = true;
                        }

                        // true = append file
                        fw = new FileWriter(file.getAbsoluteFile(), true);
                        bw = new BufferedWriter(fw);

                        if(isNew)
                            bw.write("//id  - x      y      z   walk face   revision \n");

                        bw.write(id + " - " + position.getX() + "   " + position.getY() + "   " + position.getZ() + "   " + walk + "   " + direction + "   " + revision.ordinal() + "\n");

                    } catch (IOException e) {
                        e.printStackTrace();
                    } finally {
                        try {
                            if (bw != null)
                                bw.close();

                            if (fw != null)
                                fw.close();

                        } catch (IOException ex) {

                            ex.printStackTrace();

                        }
                    }
                }

                @Override
                public String filePath() {
                    return file.getPath();
                }
            }.load();
        }
    }
}
