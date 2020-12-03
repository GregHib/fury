package com.fury.game;

public class RegionToPosition {
    public static void main(String[] args) {
        int regionId = 11316;

        int x = (regionId >> 8) * 64;
        int y = (regionId & 0xff) * 64;
        System.out.println(x + ", " + y);
    }
}
