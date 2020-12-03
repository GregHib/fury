package com.fury.game.world.map.region;

/**
 * Created by Greg on 30/10/2016.
 */
public class RegionChunk {

    public static final int DIRECTION_NORMAL = 0;
    public static final int DIRECTION_CW_0 = 0;
    public static final int DIRECTION_CW_90 = 1;
    public static final int DIRECTION_CW_180 = 2;
    public static final int DIRECTION_CW_270 = 3;

    private int chunkX;
    private int chunkY;
    private int chunkZ;
    private int rotation;
    private boolean dynamic;

    public RegionChunk(int chunkX, int chunkY, int chunkZ) {
        this(chunkX, chunkY, chunkZ, false);
    }

    public RegionChunk(int chunkX, int chunkY, int chunkZ, boolean dynamic) {
        this(dynamic, chunkX, chunkY, chunkZ, DIRECTION_NORMAL);
    }

    public RegionChunk(boolean dynamic, int chunkX, int chunkY, int chunkZ, int dynamicRot) {
        this.dynamic = dynamic;
        this.chunkX = chunkX;
        this.chunkY = chunkY;
        this.chunkZ = chunkZ;
        this.rotation = dynamicRot;
    }

    public int getChunkX() {
        return chunkX;
    }

    public int getChunkY() {
        return chunkY;
    }

    public int getChunkZ() {
        return chunkZ % 4;
    }

    public int getRotation() {
        return rotation % 4;
    }

    public boolean isDynamic() {
        return dynamic;
    }
}
