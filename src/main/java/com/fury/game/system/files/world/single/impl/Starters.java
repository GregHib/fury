package com.fury.game.system.files.world.single.impl;

import com.fury.game.system.files.Resources;
import com.fury.game.system.files.world.increment.CounterFile;

public class Starters extends CounterFile {
    private static Starters starters = new Starters();

    public static Starters get() {
        return starters;
    }

    @Override
    public String getFile() {
        return Resources.getSaveDirectory("saves") + "world/starters.txt";
    }
}
