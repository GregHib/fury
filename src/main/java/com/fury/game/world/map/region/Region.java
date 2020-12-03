package com.fury.game.world.map.region;

import com.fury.cache.ByteStream;
import com.fury.cache.Revision;
import com.fury.cache.def.object.ObjectDefinition;
import com.fury.core.model.node.entity.actor.figure.Figure;
import com.fury.core.model.node.entity.actor.figure.Projectile;
import com.fury.core.model.node.entity.actor.figure.player.Player;
import com.fury.engine.task.executor.GameExecutorManager;
import com.fury.game.GameLoader;
import com.fury.game.content.global.CustomObjects;
import com.fury.core.model.node.entity.actor.figure.mob.Mob;
import com.fury.core.model.item.FloorItem;
import com.fury.game.entity.object.GameObject;
import com.fury.game.system.files.loaders.npc.MobSpawns;
import com.fury.game.world.GameWorld;
import com.fury.game.world.map.Position;
import com.fury.game.world.map.build.DynamicRegion;
import com.fury.game.world.region.NewRegion;

import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Created by Greg on 20/12/2016.
 */
public class Region {
    private static final int CHUNK_SIZE = 8;
    public static final int SIZE = CHUNK_SIZE * 8;
    public static final int VIEW_DISTANCE = SIZE / 4 - 1;
    protected int regionId;
    protected RegionMap map;
    protected RegionMap clippedOnlyMap;

    protected List<GameObject> spawnedObjects;
    protected List<GameObject> removedOriginalObjects;
    protected List<Projectile> projectiles;
    protected GameObject[][][][] objects;
    private volatile int loadMapStage;
    private boolean loadedNPCSpawns;
    private boolean loadedObjectSpawns;
    private boolean loadedItemSpawns;

    public NewRegion region;

    public Region(int regionId) {
        this.regionId = regionId;
        region = new NewRegion(regionId);
        this.spawnedObjects = new CopyOnWriteArrayList<>();
        this.removedOriginalObjects = new CopyOnWriteArrayList<>();
        this.projectiles = new CopyOnWriteArrayList<>();
    }

    public void checkLoadMap() {
        if (getLoadMapStage() == 0) {
            setLoadMapStage(1);
            GameExecutorManager.slowExecutor.execute(() -> {
                loadRegionMap();
                setLoadMapStage(2);
                if (!isLoadedObjectSpawns()) {
                    loadObjectSpawns();
                    setLoadedObjectSpawns(true);
                }
                if (!isLoadedNPCSpawns()) {
                    loadNPCSpawns();
                    setLoadedNPCSpawns(true);
                }
                if (!isLoadedItemSpawns()) {
                    loadItemSpawns();
                    setLoadedItemSpawns(true);
                }
            });
        }
    }

    private void loadNPCSpawns() {
        MobSpawns.loadNpcSpawns(regionId);
    }

    private void loadObjectSpawns() {
        CustomObjects.loadObjectSpawns(regionId);
    }

    private void loadItemSpawns() {
        //ItemSpawns.loadItemSpawns(regionId);
    }

    public void unloadMap() {
        if (getLoadMapStage() == 2 && (getPlayerCount() == 0) /*&& (npcIndicies == null || npcIndicies.isEmpty())*/) {
            map = null;
            objects = null;
            setLoadMapStage(0);
        }
    }

    public int getLoadMapStage() {
        return loadMapStage;
    }

    public void setLoadMapStage(int loadMapStage) {
        this.loadMapStage = loadMapStage;
    }

    public boolean isLoadedObjectSpawns() {
        return loadedObjectSpawns;
    }

    public void setLoadedObjectSpawns(boolean loadedObjectSpawns) {
        this.loadedObjectSpawns = loadedObjectSpawns;
    }

    public boolean isLoadedNPCSpawns() {
        return loadedNPCSpawns;
    }

    public void setLoadedNPCSpawns(boolean loadedNPCSpawns) {
        this.loadedNPCSpawns = loadedNPCSpawns;
    }

    public List<GameObject> getSpawnedObjects() {
        return spawnedObjects;
    }

    public List<GameObject> getRemovedOriginalObjects() {
        return removedOriginalObjects;
    }

    public boolean addProjectile(Projectile projectile) {
        return projectiles.add(projectile);
    }

    public List<Projectile> getProjectiles() {
        return projectiles;
    }

    public void removeProjectiles() {
        projectiles.clear();
    }

    public Collection<Player> getPlayers(int height) {
        return region.getPlayers(height);
    }

    public int getPlayerCount() {
        int total = 0;
        for(int i = 0; i < 4; i++) {
            total += region.getPlayers(i).size();
        }
        return total;
    }

    public Collection<Mob> getNpcs(int height) {
        return region.getNpcs(height);
    }

    public Map<Position, Set<FloorItem>> getFloorItems(int height) {
        return region.getFloorItems(height);
    }


    public FloorItem getFloorItem(int id, Position position) {
        return region.getFloorItem(id, position);
    }

    public void addFloorItem(FloorItem floorItem) {
        region.addFloorItem(floorItem);
    }

    public void removeFloorItem(FloorItem floorItem) {
        region.removeFloorItem(floorItem);
    }

    public FloorItem getFloorItem(int id, Position tile, Player player) {
        return region.getFloorItem(id, tile);
    }

    public int getRegionId() {
        return regionId;
    }

    public boolean isLoadedItemSpawns() {
        return loadedItemSpawns;
    }

    public void setLoadedItemSpawns(boolean loadedItemSpawns) {
        this.loadedItemSpawns = loadedItemSpawns;
    }

    public void loadRegionMap() {
        region.loadRegionMap(this);
    }

    public byte[] getFile(int fileId, Revision revision) {
        try {
            return GameLoader.getCache().getDecompressedFile(revision.getMapIndex(), fileId);
        } catch (Throwable throwable) {
            System.out.println("Failed to load region file: " + fileId + " " + revision);
            throwable.printStackTrace();
            return null;
        }
    }

    private byte[][][] loadMapSettings(byte[] data) {
        byte[][][] heightMap;
        if (data != null) {
            heightMap = new byte[4][64][64];
            ByteStream groundStream = new ByteStream(data);
            for (int z = 0; z < 4; z++) {
                for (int tileX = 0; tileX < 64; tileX++) {
                    for (int tileY = 0; tileY < 64; tileY++) {
                        while (true) {
                            int tileType = groundStream.getUByte();
                            if (tileType == 0) {
                                break;
                            } else if (tileType == 1) {
                                groundStream.getUByte();
                                break;
                            } else if (tileType <= 49) {
                                groundStream.getUByte();
                            } else if (tileType <= 81) {
                                heightMap[z][tileX][tileY] = (byte) (tileType - 49);
                            }
                        }
                    }
                }
            }
            for (int z = 0; z < 4; z++) {
                for (int x = 0; x < 64; x++) {
                    for (int y = 0; y < 64; y++) {
                        if ((heightMap[z][x][y] & 0x1) == 1) {
                            int height = z;
                            if ((heightMap[1][x][y] & 0x2) == 2)
                                height--;

                            if (height >= 0 && height <= 3)
                                forceGetRegionMap().addUnwalkable(x, y, height);
                        }
                    }
                }
            }
        } else {
            heightMap = null;
            for (int z = 0; z < 4; z++) {
                for (int x = 0; x < 64; x++) {
                    for (int y = 0; y < 64; y++) {
                        forceGetRegionMap().addUnwalkable(x, y, z);
                    }
                }
            }
        }
        return heightMap;
    }

    private void decodeLandscapes(int regionId, byte[] data, byte[][][] heightMap) {
        ByteStream objectStream = new ByteStream(data);
        int absX = (regionId >> 8) * 64;
        int absY = (regionId & 0xff) * 64;
        int objectId = -1;
        int incr;
        Revision revision = Revision.getRevision(regionId);
        while ((incr = objectStream.getUSmart2()) != 0) {
            objectId += incr;
            int location = 0;
            int locationOffset;
            while ((locationOffset = objectStream.getUSmart()) != 0) {
                location += locationOffset - 1;
                int localX = location >> 6 & 0x3f;
                int localY = location & 0x3f;
                int height = location >> 12;
                int objectData = objectStream.getUByte();
                int type = objectData >> 2;
                int direction = objectData & 0x3;
                if (localX < 0 || localX >= 64 || localY < 0 || localY >= 64)
                    continue;

                if(objectId == 3700 || objectId == 8967)
                    continue;

                if (heightMap != null && (heightMap[1][localX][localY] & 0x2) == 2)
                    height--;

                if (height >= 0 && height <= 3)
                    spawnObject(new GameObject(objectId, new Position(absX + localX, absY + localY, height), type, direction, revision), localX, localY, height, true); // Add object to clipping
            }
        }
    }

    public RegionMap forceGetRegionMapClippedOnly() {
        if (clippedOnlyMap == null)
            clippedOnlyMap = new RegionMap(regionId, true);
        return clippedOnlyMap;
    }

    public RegionMap forceGetRegionMap() {
        if (map == null)
            map = new RegionMap(regionId, false);
        return map;
    }

    public void setMap(RegionMap map) {
        this.map = map;
    }

    /*
    Misc
     */

    public Position getLocalPosition(Position pos) {
        int regionId = pos.getRegionId();
        int regionX = (regionId >> 8) << 6;
        int regionY = (regionId & 0xff) << 6;
        return new Position(pos.getX() - regionX, pos.getY() - regionY);
    }

    public void spawnObject(GameObject object, int localX, int localY, int z, boolean original) {
        if (objects == null)
            objects = new GameObject[4][64][64][4];

        int slot = object.getType() >= OBJECT_SLOTS.length ? 3 : OBJECT_SLOTS[object.getType()];
        if (original) {
            objects[z][localX][localY][slot] = object;
            clip(object, localX, localY);
            return;
        } else {
            GameObject spawned = getSpawnedObjectWithSlot(localX, localY, z, slot);
            if (spawned != null) {
                spawnedObjects.remove(spawned);
                unclip(spawned, localX, localY);
            }

            GameObject removed = getRemovedObjectWithSlot(localX, localY, z, slot);
            if (removed != null) {
                object = removed;
                removedOriginalObjects.remove(object);
            } else if (objects[z][localX][localY][slot] != object) {
                spawnedObjects.add(object);
                if (objects[z][localX][localY][slot] != null)
                    unclip(objects[z][localX][localY][slot], localX, localY);
            } else if (spawned == null) {
                return;
            }
        }
        clip(object, localX, localY);

        if (!original) {
            for (Player p2 : GameWorld.getRegions().getLocalPlayers(object)) {
                if (p2 == null || !p2.hasStarted() || p2.getFinished())
                    continue;
                p2.getPacketSender().sendObject(object);
            }
        }
    }

    public void removeObject(GameObject object, int localX, int localY, int z) {
        if (objects == null)
            objects = new GameObject[4][64][64][4];
        int slot = OBJECT_SLOTS[object.getType()];
        GameObject removed = getRemovedObjectWithSlot(localX, localY, z, slot);
        if (removed != null) {
            removedOriginalObjects.remove(object);
            clip(removed, localX, localY);
        }

        GameObject original = null;
        GameObject spawned = getSpawnedObjectWithSlot(localX, localY, z, slot);
        if (spawned != null) {
            object = spawned;
            spawnedObjects.remove(object);
            unclip(object, localX, localY);
            GameObject o = objects[z][localX][localY][slot];
            if (o != null) {
                clip(o, localX, localY);
                original = o;
            }
        } else if (objects[z][localX][localY][slot] == object) { // removes original
            unclip(object, localX, localY);
            removedOriginalObjects.add(object);
        } else {
            //System.out.println("Requested object to remove wasn't found.(Shouldn't happen)");
            return;
        }


        for (Player p2 : GameWorld.getRegions().getLocalPlayers(object)) {
            if (p2 == null || !p2.hasStarted() || p2.getFinished())
                continue;
            if (original != null)
                p2.getPacketSender().sendObject(original);
            else
                p2.getPacketSender().sendObjectRemoval(object);
        }
    }

    public void clip(GameObject object, int x, int y) {
        if (object.getId() == -1)
            return;

        int z = object.getZ();
        int type = object.getType();
        int rotation = object.getDirection();

        if (x < 0 || y < 0 || x >= 64 || y >= 64)
            return;

        ObjectDefinition definition = object.getDefinition();

        if (object.getType() == 22 ? definition.getSolid() != 1 : definition.getSolid() == 0)
            return;

        if (type >= 0 && type <= 3) {
            if (!definition.ignoreClipOnAlternativeRoute)
                forceGetRegionMap().addWall(x, y, z, object.getType(), object.getDirection(), definition.isProjectileClipped(), !definition.ignoreClipOnAlternativeRoute);
            if (definition.isProjectileClipped())
                forceGetRegionMapClippedOnly().addWall(x, y, z, object.getType(), object.getDirection(), definition.isProjectileClipped(), !definition.ignoreClipOnAlternativeRoute);

        } else if (type >= 9 && type <= 21) {
            int sizeX;
            int sizeY;
            if (rotation != 1 && rotation != 3) {
                sizeX = definition.xLength();
                sizeY = definition.yLength();
            } else {
                sizeY = definition.xLength();
                sizeX = definition.yLength();
            }

            forceGetRegionMap().addObject(x, y, z, sizeX, sizeY, definition.isProjectileClipped(), !definition.ignoreClipOnAlternativeRoute);
            if (definition.isProjectileClipped())
                forceGetRegionMapClippedOnly().addObject(x, y, z, sizeX, sizeY, definition.isProjectileClipped(), !definition.ignoreClipOnAlternativeRoute);

        } else if (type == 22)
            map.addFloor(x, y, z);
    }

    public void unclip(GameObject object, int x, int y) {
        if (object.getId() == -1)
            return;

        int z = object.getZ();
        int type = object.getType();
        int rotation = object.getDirection();

        if (x < 0 || y < 0 || x >= 64 || y >= 64)
            return;

        ObjectDefinition definition = object.getDefinition();

        if (type == 22 ? definition.getSolid() != 1 : definition.getSolid() == 0)
            return;

        if (type >= 0 && type <= 3) {
            forceGetRegionMap().removeWall(x, y, z, object.getType(), object.getDirection(), definition.isProjectileClipped(), !definition.ignoreClipOnAlternativeRoute);
            if (definition.isProjectileClipped())
                forceGetRegionMapClippedOnly().removeWall(x, y, z, object.getType(), object.getDirection(), definition.isProjectileClipped(), !definition.ignoreClipOnAlternativeRoute);

        } else if (type >= 9 && type <= 21) {
            int sizeX;
            int sizeY;
            if (rotation != 1 && rotation != 3) {
                sizeX = definition.xLength();
                sizeY = definition.yLength();
            } else {
                sizeY = definition.xLength();
                sizeX = definition.yLength();
            }

            forceGetRegionMap().removeObject(x, y, z, sizeX, sizeY, definition.isProjectileClipped(), !definition.ignoreClipOnAlternativeRoute);
            if (definition.isProjectileClipped())
                forceGetRegionMapClippedOnly().removeObject(x, y, z, sizeX, sizeY, definition.isProjectileClipped(), !definition.ignoreClipOnAlternativeRoute);

        } else if (type == 22)
            map.removeFloor(x, y, z);
    }

    public boolean containsObjectWithId(int id, int x, int y, int z) {
        GameObject object = getObjectWithId(id, x, y, z);
        return object != null && object.getId() == id;
    }

    public GameObject getObjectWithId(int id, int x, int y, int z) {
        if (objects == null)
            return null;
        for (GameObject object : removedOriginalObjects) {
            if (object.getId() == id && object.getXInRegion() == x && object.getYInRegion() == y && object.getZ() == z)
                return null;
        }
        for (int i = 0; i < 4; i++) {
            GameObject object = objects[z][x][y][i];
            if (object != null && object.getId() == id) {
                GameObject spawned = getSpawnedObjectWithSlot(x, y, z, OBJECT_SLOTS[object.getType()]);
                return spawned == null ? object : spawned;
            }
        }
        /*if (forceGetRegionMap().tileExists(x, y, z)) {
            GameObject object = forceGetRegionMap().getTile(x, y, z).getFloorObject();
            if (object == null || object.getId() != id)
                object = forceGetRegionMap().getTile(x, y, z).getWallObject();
            if (object == null || object.getId() != id)
                object = forceGetRegionMap().getTile(x, y, z).getFloorDecoration();
            if (object == null || object.getId() != id)
                object = forceGetRegionMap().getTile(x, y, z).getWallDecoration();
            if (object != null && object.getId() == id) {
                GameObject spawned = getSpawnedObjectWithSlot(x, y, z, OBJECT_SLOTS[object.getType()]);
                return spawned == null ? object : spawned;
            }
        }*/
        for (GameObject object : spawnedObjects) {
            if (object.getId() == id && object.getXInRegion() == x && object.getYInRegion() == y && object.getZ() == z)
                return object;
        }
        return null;
    }

    public GameObject getSpawnedObjectWithSlot(int x, int y, int z, int slot) {
        for (GameObject object : spawnedObjects) {
            if (object.getXInRegion() == x && object.getYInRegion() == y && object.getZ() == z && OBJECT_SLOTS[object.getType()] == slot)
                return object;
        }
        return null;
    }

    public GameObject getObjectWithSlot(int x, int y, int z, int slot) {
        if (objects == null)
            return null;
        GameObject o = getSpawnedObjectWithSlot(x, y, z, slot);
        if (o == null) {
            if (getRemovedObjectWithSlot(x, y, z, slot) != null)
                return null;
            return objects[z][x][y][slot];
        }
        return o;
    }

    public GameObject getRealObject(int x, int y, int plane, int type) {
        return objects[plane][x][y][OBJECT_SLOTS[type]];
    }

    public GameObject getRemovedObjectWithSlot(int x, int y, int z, int slot) {
        for (GameObject object : removedOriginalObjects) {
            if (object.getXInRegion() == x && object.getYInRegion() == y && object.getZ() == z && OBJECT_SLOTS[object.getType()] == slot)
                return object;
        }
        return null;
    }

    public GameObject getObjectWithType(int x, int y, int z, int type) {
        GameObject object = getObjectWithSlot(x, y, z, OBJECT_SLOTS[type]);
        return object != null && object.getType() == type ? object : null;
    }

    public static GameObject getGameObject(Position position) {
        return GameWorld.getRegions().get(position.getRegionId()).getStandardObject(position.getXInRegion(), position.getYInRegion(), position.getZ());
    }

    public GameObject[] getAllObjects(int x, int y, int plane) {
        if (objects == null)
            return null;
        return objects[plane][x][y];
    }

    public List<GameObject> getAllObjects() {
        if (objects == null)
            return null;
        List<GameObject> list = new ArrayList<>();
        for (int z = 0; z < 4; z++)
            for (int x = 0; x < 64; x++)
                for (int y = 0; y < 64; y++) {
                    if (objects[z][x][y] == null)
                        continue;
                    for (GameObject o : objects[z][x][y])
                        if (o != null)
                            list.add(o);
                }
        return list;
    }

    public GameObject getStandardObject(int x, int y, int z) {
        return getObjectWithSlot(x, y, z, OBJECT_SLOT_FLOOR);
    }

    public int getMask(int localX, int localY, int z) {
        if (map == null || getLoadMapStage() != 2)
            return -1; // clipped tile
        return map.getMask(localX, localY, z);
    }

    public int getMaskClippedOnly(int localX, int localY, int z) {
        if (clippedOnlyMap == null || getLoadMapStage() != 2)
            return -1; // clipped tile
        return clippedOnlyMap.getMask(localX, localY, z);
    }

    public void clearObjects() {
        //objects = null;
    }

    public static final int[] OBJECT_SLOTS = new int[]
            {0, 0, 0, 0, 1, 1, 1, 1, 1, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 3};
    public static final int OBJECT_SLOT_WALL = 0;
    public static final int OBJECT_SLOT_WALL_DECORATION = 1;
    public static final int OBJECT_SLOT_FLOOR = 2;
    public static final int OBJECT_SLOT_FLOOR_DECORATION = 3;

    public void removeEntity(Figure figure) {
        region.removeEntity(figure);
    }

    public void addEntity(Figure figure) {
        region.addEntity(figure);
    }

    public final void sendGameObjects(Player player) {
        for (GameObject object : removedOriginalObjects)
            player.getPacketSender().sendObjectRemoval(object);
        for (GameObject object : spawnedObjects)
            player.getPacketSender().sendObject(object);
    }

    public final void sendFloorItems(Player player) {
        region.sendFloorItems(player);
    }

    public boolean isDynamic() {
        return this instanceof DynamicRegion;
    }

    private Optional<Region[]> surroundingRegions = Optional.empty();

    public Optional<Region[]> getSurroundingRegions() {
        return surroundingRegions;
    }

    public void setSurroundingRegions(Optional<Region[]> surroundingRegions) {
        this.surroundingRegions = surroundingRegions;
    }

    public boolean containsGroundItem(FloorItem floorItem) {
        return region.containsFloorItem(floorItem);
    }



    public void skip(GameObject gameObject) {
        region.getBlock(gameObject.getZ()).skip(gameObject);
    }

}
