package com.fury.game.system.files.world.single.impl;

import com.fury.game.system.files.Resources;
import com.fury.game.system.files.world.single.TimeStampFile;

public class Mutes extends TimeStampFile {
    private static Mutes bans = new Mutes();

    public static Mutes get() {
        return bans;
    }

    @Override
    public String getFile() {
        return Resources.getSaveDirectory("saves") + "world/punishment/mutes.txt";
    }
}
