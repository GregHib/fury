package com.fury.game.system.files.world.increment.timer.impl;

import com.fury.game.system.files.world.increment.timer.TimedCleanFile;
import com.fury.game.system.files.Resources;

public class DailySnowflake extends TimedCleanFile {

    private static DailySnowflake dailyDonor = new DailySnowflake();

    public static DailySnowflake get() {
        return dailyDonor;
    }

    @Override
    public String getFile() {
        return Resources.getSaveDirectory("saves") + "dailysnowflake.txt";
    }

    @Override
    public long getPeriod() {
        return TWENTY_FOUR_HOURS;
    }

}
