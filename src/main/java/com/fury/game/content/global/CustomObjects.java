package com.fury.game.content.global;

import com.fury.cache.Revision;
import com.fury.game.entity.object.GameObject;
import com.fury.game.node.entity.actor.object.ObjectManager;
import com.fury.game.system.files.Resources;
import com.fury.game.world.map.Position;

import java.io.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Handles customly spawned objects (mostly global but also privately for players)
 *
 * @author Gabriel Hannason
 */
public class CustomObjects {
    private static final Pattern pattern = Pattern.compile("^(-?\\d+)\\s(\\d+)\\s(\\d+)\\s(\\d+)\\s(\\d+)(?:\\s(\\d+))?.*");

    public static void loadObjectSpawns(int region) {
        File file = new File(Resources.getDirectory("custom") + region);

        if(!file.exists())
            return;


        try {
            FileInputStream stream = new FileInputStream(file.getPath());
            BufferedReader br = new BufferedReader(new InputStreamReader(stream));

            String line;

            while ((line = br.readLine()) != null) {
                Matcher m = pattern.matcher(line);

                if (m.find()) {
                    int id = Integer.valueOf(m.group(1));
                    int x = Integer.valueOf(m.group(2));
                    int y = Integer.valueOf(m.group(3));
                    int z = Integer.valueOf(m.group(4));
                    int face = Integer.valueOf(m.group(5));
                    Revision revision = Revision.RS2;
                    if(m.group(6) != null)
                        revision = Revision.values()[Integer.valueOf(m.group(6))];

                    GameObject object = new GameObject(id, new Position(x, y, z), face, revision);
                    ObjectManager.spawnObject(object);
                }
            }
            br.close();
            stream.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
