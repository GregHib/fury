package com.fury.game.system.files.world.increment.timer.impl;

import com.fury.game.system.files.Resources;
import com.fury.game.system.files.world.increment.timer.TimedCleanFile;

public class DailyDonor extends TimedCleanFile {

    private static DailyDonor dailyDonor = new DailyDonor();

    public static DailyDonor get() {
        return dailyDonor;
    }

    @Override
    public String getFile() {
        return Resources.getSaveDirectory("saves") + "dailydonor.txt";
    }

    @Override
    public long getPeriod() {
        return TWENTY_FOUR_HOURS;
    }

}
