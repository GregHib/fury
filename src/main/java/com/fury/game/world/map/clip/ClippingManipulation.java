package com.fury.game.world.map.clip;

import com.fury.game.world.GameWorld;
import com.fury.game.world.map.Position;
import com.fury.game.world.map.region.RegionMap;

/**
 * Created by Greg on 06/11/2016.
 */
public class ClippingManipulation {

    public static int getRegionId(Position pos) {
        int regionX = pos.getX() >> 3;
        int regionY = pos.getY() >> 3;
        int regionId = (regionX / 8 << 8) + regionY / 8;
        return regionId;
    }

    public static int getRegionId(int x, int y) {
        int regionX = x >> 3;
        int regionY = y >> 3;
        int regionId = (regionX / 8 << 8) + regionY / 8;
        return regionId;
    }

    public static int[][] copyClippingChunk(int regionId, int chunkX, int chunkY) {
        RegionMap region = GameWorld.getRegions().get(regionId).forceGetRegionMap();
        //int[][] clipping = region.masks[0];

        int[][] clip = new int[8][8];
        /*for(int x = 0; x < 8; x++) {
            for(int y = 0; y < 8; y++) {
                int offsetX = x + chunkX;
                int offsetY = y + chunkY;
                clip[x][y] = clipping[offsetX][offsetY];
            }
        }*/
        return clip;
    }

    public static int[][] insertClippingChunk(int[][] from, int[][] to, int startX, int startY) {
        for(int x = 0; x < 8; x++) {
            for(int y = 0; y < 8; y++) {
                to[startX + x][startY + y] = from[x][y];
            }
        }
        return to;
    }

    /**
     * Rotates a 2D clipping array (90 * rotation) degrees clockwise.
     * @param roomClipping
     * @param rotation
     * @return Clipping array
     */
    public static int[][] rotateClippingChunk(int[][] roomClipping, int rotation) {
        for(int r = 0; r < rotation; r++)
            roomClipping = rotateClippingChunk(roomClipping);
        return roomClipping;
    }

    /**
     * Rotates a 2D clipping array 90 degrees clockwise.
     * @param roomClipping
     * @return Clipping array
     */
    private static int[][] rotateClippingChunk(int[][] roomClipping) {
        int[][] copy = new int[roomClipping.length][roomClipping[0].length];
        for(int y = roomClipping.length-1; y >= 0 ; y--) {
            for(int x = 0; x > -roomClipping.length; x--) {
                int changeX = x + y;
                int changeY = (x + roomClipping.length-1) + (-y);
                copy[Math.abs(x) + changeX][y + changeY] = roomClipping[Math.abs(x)][y];
            }
        }
        return copy;
    }
}
