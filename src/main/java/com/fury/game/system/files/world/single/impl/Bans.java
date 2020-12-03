package com.fury.game.system.files.world.single.impl;

import com.fury.game.system.files.Resources;
import com.fury.game.system.files.world.single.TimeStampFile;

public class Bans extends TimeStampFile {
    private static Bans bans = new Bans();

    public static Bans get() {
        return bans;
    }

    @Override
    public String getFile() {
        return Resources.getSaveDirectory("saves") + "world/punishment/bans.txt";
    }
}
