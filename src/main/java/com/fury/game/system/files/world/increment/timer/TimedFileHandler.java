package com.fury.game.system.files.world.increment.timer;

import com.fury.game.system.files.world.increment.timer.impl.DailyDonor;
import com.fury.game.system.files.world.increment.timer.impl.DailySnowflake;
import com.fury.game.system.files.world.increment.timer.impl.DailyVotes;
import com.fury.game.system.files.world.increment.timer.impl.WeeklyStatue;

import java.time.LocalTime;

public class TimedFileHandler {

    public static void init() {
        DailyVotes.get().init();
        DailyDonor.get().init();
        DailySnowflake.get().init();
        WeeklyStatue.get().init();
    }

    public static void save() {
        DailyVotes.get().save();
        DailyDonor.get().save();
        DailySnowflake.get().save();
        WeeklyStatue.get().save();
    }
}
