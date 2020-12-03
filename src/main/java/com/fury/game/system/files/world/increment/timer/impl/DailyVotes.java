package com.fury.game.system.files.world.increment.timer.impl;

import com.fury.core.model.node.entity.actor.figure.player.Player;
import com.fury.game.system.files.world.increment.timer.TimedCleanFile;
import com.fury.game.system.files.Resources;

import java.time.Instant;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalUnit;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class DailyVotes extends TimedCleanFile {

    private static DailyVotes dailyVotes = new DailyVotes();

    public static DailyVotes get() {
        return dailyVotes;
    }

    private static final Calendar MIDNIGHT = Calendar.getInstance();
    private static final Calendar AFTERNOON = Calendar.getInstance();
    static {
        MIDNIGHT.set(Calendar.HOUR, 12);
        MIDNIGHT.set(Calendar.MINUTE, 0);
        MIDNIGHT.set(Calendar.SECOND, 0);
        MIDNIGHT.set(Calendar.MILLISECOND, 0);
        AFTERNOON.set(Calendar.HOUR, 0);
        AFTERNOON.set(Calendar.MINUTE, 0);
        AFTERNOON.set(Calendar.SECOND, 0);
        AFTERNOON.set(Calendar.MILLISECOND, 0);
    }

    @Override
    public Calendar getResetTime() {
        Calendar now = Calendar.getInstance();
        return AFTERNOON.compareTo(now) < 0 ? MIDNIGHT : AFTERNOON;
    }

    @Override
    public String getFile() {
        return Resources.getSaveDirectory("saves") + "votes.txt";
    }

    @Override
    public long getPeriod() {
        return TWELVE_HOURS;
    }

    public boolean canClaim(String address) {
        if(has(address) && get(address) >= 4)
            return false;
        return true;
    }

    public void handleLogin(Player player) {
        String voteMessage;
        if(canClaim(player.getLogger().getHardwareId()))
            voteMessage = "You can vote now! Type ::vote";
        else {
            Calendar now = Calendar.getInstance();
            int hour = now.get(Calendar.HOUR_OF_DAY);
            int minute = now.get(Calendar.MINUTE);
            int second = now.get(Calendar.SECOND);
            minute = minute == 0 ? 0 : 60 - minute;
            second = second == 0 ? 0 : 60 - second;

            if(AFTERNOON.compareTo(now) < 0) {
                hour = 24 - now.get(Calendar.HOUR_OF_DAY);
            } else {
                hour = 12 - hour;
            }
            voteMessage = "You can vote in "+hour+"h "+minute+"m "+second+"s.";
        }

        player.message(voteMessage);
    }
}
