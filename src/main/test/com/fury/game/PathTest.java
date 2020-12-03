package com.fury.game;

import com.fury.game.world.map.clip.Flags;

public class PathTest {
    public static void main(String[] args) {
        System.out.println(0x10000);
        System.out.println(Integer.toHexString(256));
        System.out.println((1073742080 & (Flags.FLOOR_BLOCKSWALK | Flags.FLOORDECO_BLOCKSWALK | Flags.OBJ)) == 0);
    }
}
