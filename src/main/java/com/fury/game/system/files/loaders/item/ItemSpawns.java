package com.fury.game.system.files.loaders.item;

import com.fury.core.model.item.FloorItem;
import com.fury.core.model.item.Item;
import com.fury.engine.task.executor.GameExecutorManager;
import com.fury.game.network.packet.out.FloorItemSpawn;
import com.fury.game.system.files.Resources;
import com.fury.game.world.GameWorld;
import com.fury.game.world.map.Position;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import static java.nio.file.Files.*;

public class ItemSpawns {
    private static final HashMap<Position, Item> FLOOR_ITEMS = new HashMap<>();
    private static final Map<Item, ScheduledFuture<?>> SCHEDULED_RESPAWNS = new HashMap<>();
    private static final Map<Item, Integer> DELAYS = new HashMap<>();

    public static void init() {
        File file = new File(Resources.getDirectory("items") + "spawns.txt");
        try (BufferedReader reader = newBufferedReader(Paths.get(Resources.getDirectory("items") + "spawns.txt"))) {
            reader.readLine(); // reads the comment at the top
            reader.lines()
                    .filter(l -> !l.startsWith("//"))
                    .map(l -> l.split(",\\s"))
                    .forEach(ItemSpawns::createSpawm);
        }catch(IOException e) {
            e.printStackTrace();
        }
    }

    public static void scheduleRespawnIfNeeded(Item item, Position tile) {
        if(item == null || tile == null)
            return;

        Item supportedItem = FLOOR_ITEMS.get(tile);
        if(supportedItem == null || !supportedItem.isEqual(item))
            return;

        if(SCHEDULED_RESPAWNS.containsKey(item))
            SCHEDULED_RESPAWNS.get(item).cancel(true);

        Integer delay = DELAYS.get(item);
        Item spawnedItem = new Item(item.getId(), item.getAmount());
        SCHEDULED_RESPAWNS.put(item, GameExecutorManager.slowExecutor.schedule(() -> spawnItem(spawnedItem, tile), delay, TimeUnit.SECONDS));
    }

    private static void createSpawm(String[] lineData) {
        if(lineData.length < 6) {
            return;
        }

        int id = Integer.parseInt(lineData[0]);
        int x = Integer.parseInt(lineData[1]);
        int y = Integer.parseInt(lineData[2]);
        int z = Integer.parseInt(lineData[3]);
        int amount = Integer.parseInt(lineData[4]);
        int delay = Integer.parseInt(lineData[5]);

        Position tile = new Position(x, y, z);
        Item item = new Item(id, amount);

        FLOOR_ITEMS.put(tile, item);
        DELAYS.put(item, delay);

        spawnItem(item, tile);
    }

    private static void spawnItem(Item item, Position tile) {
        FloorItem floorItem = new FloorItem(item, tile,null, true, true);
        GameWorld.getRegions().getRegion(tile).addFloorItem(floorItem);
        GameWorld.getRegions().getRegion(tile).getPlayers(tile.getZ()).stream()
                .filter(player -> player.hasStarted() && !player.getFinished())
                .forEach(player -> player.send(new FloorItemSpawn(floorItem)));
    }
}
