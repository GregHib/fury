package com.fury.game.system.files.world.single.impl;

import com.fury.game.system.files.Resources;
import com.fury.game.system.files.world.single.TimeStampFile;

public class Compensation extends TimeStampFile {
    private static Compensation compensation = new Compensation();

    public static Compensation get() {
        return compensation;
    }

    @Override
    public String getFile() {
        return Resources.getSaveDirectory("saves") + "world/compensation.txt";
    }
}
