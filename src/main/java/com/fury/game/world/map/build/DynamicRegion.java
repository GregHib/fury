package com.fury.game.world.map.build;

import com.fury.cache.ByteStream;
import com.fury.cache.Revision;
import com.fury.cache.def.Loader;
import com.fury.cache.def.object.ObjectDefinition;
import com.fury.game.entity.object.GameObject;
import com.fury.game.world.map.Position;
import com.fury.game.world.map.region.Region;
import com.fury.game.world.map.region.RegionIndex;
import com.fury.game.world.map.region.RegionIndexing;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Greg on 10/10/2016.
 */
public class DynamicRegion extends Region {

    private int[][][][] regionCoords;
    private boolean[][][] needsReload;
    private boolean recheckReload;

    public DynamicRegion(int regionId) {
        super(regionId);
        regionCoords = new int[4][8][8][4];
        needsReload = new boolean[4][8][8];
        for (int z = 0; z < 4; z++) {
            for (int x = 0; x < 8; x++) {
                for (int y = 0; y < 8; y++) {
                    needsReload[z][x][y] = true;
                }
            }
        }
        recheckReload = false;
    }

    @Override
    public void checkLoadMap() {
        if (recheckReload) {
            setLoadMapStage(0);
            recheckReload = false;
        }
        super.checkLoadMap();
    }

    @Override
    public void loadRegionMap() {
        for (int dynZ = 0; dynZ < 4; dynZ++) {
            for (int dynX = 0; dynX < 8; dynX++) {
                for (int dynY = 0; dynY < 8; dynY++) {
                    if (!needsReload[dynZ][dynX][dynY])
                        continue;
                    unloadChunk(dynX, dynY, dynZ);
                }
            }
        }
        for (int dynZ = 0; dynZ < 4; dynZ++) {
            for (int dynX = 0; dynX < 8; dynX++) {
                for (int dynY = 0; dynY < 8; dynY++) {
                    if (!needsReload[dynZ][dynX][dynY])
                        continue;
                    needsReload[dynZ][dynX][dynY] = false;

                    int renderChunkX = regionCoords[dynZ][dynX][dynY][0];
                    int renderChunkY = regionCoords[dynZ][dynX][dynY][1];
                    int renderChunkZ = regionCoords[dynZ][dynX][dynY][2];
                    int rotation = regionCoords[dynZ][dynX][dynY][3];
                    int renderLocalChunkX = renderChunkX - ((renderChunkX >> 3) << 3);
                    int renderLocalChunkY = renderChunkY - ((renderChunkY >> 3) << 3);

                    if (renderChunkX == 0 && renderChunkY == 0 && renderChunkZ == 0 && rotation == 0)
                        continue;

                    int regionId = (renderChunkX >> 3) << 8 | (renderChunkY >> 3);

                    RegionIndex regionData = RegionIndexing.get(regionId);

                    if (regionData == null)
                        continue;

                    int landscape = regionData.mapObject;
                    byte[] mapSettingsData = getFile(landscape, regionData.revision);
                    byte[][][] heightMap;
                    if(mapSettingsData != null) {
                        heightMap = new byte[4][64][64];
                        ByteStream groundStream = new ByteStream(mapSettingsData);
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
                                    if ((heightMap[z][x][y] & 1) == 1) {
                                        int height = z;
                                        if ((heightMap[1][x][y] & 2) == 2) {
                                            height--;
                                        }
                                        if (height == renderChunkZ && (x >> 3) == renderLocalChunkX && (y >> 3) == renderLocalChunkY) {
                                            int[] coords = translate(x & 0x7, y & 0x7, rotation);
                                            forceGetRegionMap().addUnwalkable((dynX << 3) | coords[0], (dynY << 3) | coords[1], dynZ);
                                        }
                                    }
                                }
                            }
                        }
                    } else {
                        heightMap = null;
                        for (int z = 0; z < 4; z++) {
                            for (int x = 0; x < 64; x++) {
                                for (int y = 0; y < 64; y++) {
                                    if (z == renderChunkZ && (x >> 3) == renderLocalChunkX && (y >> 3) == renderLocalChunkY) {
                                        int[] coords = translate(x & 0x7, y & 0x7, rotation);
                                        forceGetRegionMap().addUnwalkable((dynX << 3) | coords[0], (dynY << 3) | coords[1], dynZ);
                                    }
                                }
                            }
                        }
                    }


                    int mapObjects = regionData.landscape;
                    byte[] objectsData = getFile(mapObjects, regionData.revision);
                    if (objectsData != null) {
                        ByteStream objectStream = new ByteStream(objectsData);
                        int objectId = -1;
                        int incr;
                        while ((incr = objectStream.getUSmart2()) != 0) {
                            objectId += incr;
                            int location = 0;
                            int incr2;
                            while ((incr2 = objectStream.getUSmart()) != 0) {
                                location += incr2 - 1;
                                int localX = location >> 6 & 0x3f;
                                int localY = location & 0x3f;
                                int height = location >> 12;
                                int objectData = objectStream.getUByte();
                                int type = objectData >> 2;
                                int direction = objectData & 0x3;
                                int realZ = height;

                                if (localX < 0 || localX >= 64 || localY < 0 || localY >= 64)
                                    continue;

                                if (heightMap != null && (heightMap[1][localX][localY] & 2) == 2)
                                    realZ--;
                                if (realZ == renderChunkZ && (localX >> 3) == renderLocalChunkX && (localY >> 3) == renderLocalChunkY) {
                                    ObjectDefinition definition = Loader.getObject(objectId, Revision.getRevision(getRegionId()));
                                    int[] coords = translate(localX & 0x7, localY & 0x7, rotation, definition.xLength(), definition.yLength(), direction);
                                    spawnObject(new GameObject(objectId, new Position((dynX << 3) + coords[0] + ((getRegionId() >> 8) << 6), (dynY << 3) + coords[1] + ((getRegionId() & 0xFF) << 6), dynZ), type, (rotation + direction) & 0x3), (dynX << 3) + coords[0], (dynY << 3) + coords[1], dynZ, true);
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    private void unloadChunk(int chunkX, int chunkY, int chunkZ) {
        for (int x = 0; x < 8; x++) {
            for (int y = 0; y < 8; y++) {
                int fullX = (chunkX << 3) | x;
                int fullY = (chunkY << 3) | y;
                if (objects != null) {
                    for (int slot = 0; slot < 4; slot++) {
                        objects[chunkZ][fullX][fullY][slot] = null;
                    }
                }
                if (map != null)
                    map.setMask(fullX, fullY, chunkZ, 0);
                if (clippedOnlyMap != null)
                    clippedOnlyMap.setMask(fullX, fullY, chunkZ, 0);

                List<GameObject> ro = new ArrayList<>(removedOriginalObjects);
                // List<WorldObject> ao = new
                // ArrayList<WorldObject>(spawnedObjects);
                for (GameObject removed : ro)
                    if (removed.getZ() == chunkZ && removed.getChunkX() == chunkX && removed.getChunkY() == chunkY)
                        removedOriginalObjects.remove(removed);
				/*
				 * for (WorldObject added : ro) if (added.getPlane() == chunkZ
				 * && added.getChunkX() == chunkX && added.getChunkY() ==
				 * chunkY) spawnedObjects.remove(ao);
				 */
            }
        }
    }

    public static int[] translate(int x, int y, int rotation) {
        int[] coords = new int[2];
        if (rotation == 0) {
            coords[0] = x;
            coords[1] = y;
        } else if (rotation == 1) {
            coords[0] = y;
            coords[1] = 7 - x;
        } else if (rotation == 2) {
            coords[0] = 7 - x;
            coords[1] = 7 - y;
        } else {
            coords[0] = 7 - y;
            coords[1] = x;
        }
        return coords;
    }

    public static int[] translate(int x, int y, int mapRotation, int sizeX, int sizeY, int objectRotation) {
        int[] coords = new int[2];
        if ((objectRotation & 0x1) == 1) {
            int prevSizeX = sizeX;
            sizeX = sizeY;
            sizeY = prevSizeX;
        }
        if (mapRotation == 0) {
            coords[0] = x;
            coords[1] = y;
        } else if (mapRotation == 1) {
            coords[0] = y;
            coords[1] = 7 - x - (sizeX - 1);
        } else if (mapRotation == 2) {
            coords[0] = 7 - x - (sizeX - 1);
            coords[1] = 7 - y - (sizeY - 1);
        } else if (mapRotation == 3) {
            coords[0] = 7 - y - (sizeY - 1);
            coords[1] = x;
        }
        return coords;
    }

    public int[][][][] getRegionCoords() {
        return regionCoords;
    }

    public void setReloadObjects(int plane, int x, int y) {
        needsReload[plane][x][y] = true;
        recheckReload = true;
    }
}
