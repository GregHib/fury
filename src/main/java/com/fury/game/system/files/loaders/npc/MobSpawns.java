package com.fury.game.system.files.loaders.npc;

import com.fury.cache.Revision;
import com.fury.core.model.node.entity.actor.figure.mob.Mob;
import com.fury.game.GameSettings;
import com.fury.game.content.controller.impl.PuroPuro;
import com.fury.game.system.files.Resources;
import com.fury.game.world.GameWorld;
import com.fury.game.world.map.Position;
import com.fury.game.world.update.flag.block.Direction;
import com.fury.util.Logger;
import com.fury.util.Misc;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MobSpawns {

    private static final List<MobSpawn>[][] spawns = new ArrayList[256][256];
    public static final Pattern pattern = Pattern.compile("^(\\d+)\\s-\\s(\\d+)[ \\t]+(\\d+)[ \\t]+(\\d+)[ \\t]+(-?\\d+)[ \\t]+(\\w+)[ \\t]+(\\d+)");

    public static List<MobSpawn>[][] getSpawns() {
        return spawns;
    }

    public static void init() {
        PuroPuro.spawn();
        loadAll();
    }

    public static void loadAll() {
        long start = System.currentTimeMillis();
        File[] files = new File(Resources.getDirectory("spawns")).listFiles();
        for (File file : files)
            if (file.isFile() && file.getName().endsWith(".txt"))
                loadRegion(Integer.parseInt(file.getName().substring(0, file.getName().length() - 4)));

        if (GameSettings.DEBUG)
            System.out.println(files.length + " npc spawn regions loaded in " + (System.currentTimeMillis() - start) + "ms.");
    }

    public static void loadRegion(int region) {
        String path = Resources.getDirectory("spawns") + region + ".txt";
        File file = new File(path);

        if (!file.exists())
            return;

        loadSpawnsFile(file);
    }

    private static final void loadSpawnsFile(File file) {
        try {
            BufferedReader in = new BufferedReader(new FileReader(file));
            int count = 0;
            while (true) {
                count++;
                String line = in.readLine();
                if (line == null)
                    break;
                if (line.startsWith("//") || line.startsWith("RSBOT") || line.isEmpty())
                    continue;
                Matcher matcher = pattern.matcher(line);
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

                    addSpawn(id, position, walk > 0, direction, revision);
                } else {
                    in.close();
                    throw new RuntimeException("Invalid Npc Spawn line: " + file + " , line number: " + count);
                }
            }
            in.close();
        } catch (Throwable e) {
            Logger.handle(e);
        }
    }

    public static final void loadNpcSpawns(int regionId) {
        int x = (regionId >> 8);
        int y = (regionId & 0xff);
        if (x < 0 || x > spawns.length || y > spawns[x].length || spawns[x][y] == null)
            return;

        for (MobSpawn spawn : spawns[x][y]) {
            Mob mob = GameWorld.getMobs().spawn(spawn.getId(), spawn.getRevision(), spawn.getTile(), false);
            if (!spawn.isWalk())
                mob.getMovement().lock();
            mob.getDirection().setDirection(spawn.getDirection());
            mob.setRespawnDirection(spawn.getDirection());
        }
        spawns[x][y] = null;
    }

    private static final void addSpawn(int id, Position tile, boolean walk, Direction direction, Revision revision) {
        int x = tile.getRegionX();
        int y = tile.getRegionY();

        if (spawns[x][y] == null)
            spawns[x][y] = new ArrayList<>();
        spawns[x][y].add(new MobSpawn(id, tile, walk, direction, revision));
    }
}
