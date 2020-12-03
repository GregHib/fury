package com.fury.game.system.files.world.increment.timer;

import com.fury.game.GameSettings;
import com.fury.game.system.files.world.increment.CounterFile;

import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.*;
import java.util.function.Consumer;

public abstract class TimedCleanFile extends CounterFile {

    public static final long HOUR = 1000 * 60 * 60;
    public static final long TWELVE_HOURS = HOUR * 12;
    public static final long TWENTY_FOUR_HOURS = TWELVE_HOURS * 2;
    public static final long WEEK = TWENTY_FOUR_HOURS * 7;

    public abstract long getPeriod();


    public Calendar getResetTime() {
        Calendar date = Calendar.getInstance();
        Calendar now = Calendar.getInstance();
        date.set(Calendar.HOUR, 0);
        date.set(Calendar.MINUTE, 0);
        date.set(Calendar.SECOND, 0);
        date.set(Calendar.MILLISECOND, 0);
        if(date.compareTo(now) < 0)
           date.add(Calendar.DATE, 1);
        return date;
    }

    public void init() {
        super.init();
        Timer timer = new Timer();
        Calendar date = getResetTime();
        timer.schedule(
                new TimerTask() {
                    @Override
                    public void run() {
                        action(date);
                    }
                },
                date.getTime(), getPeriod()
        );
    }

    public void action(Calendar date) {
        if(GameSettings.DEBUG)
            System.out.println(date.getTime() + " Cleaning timer records: " + getFile());
        records.clear();
        save();
    }
}
