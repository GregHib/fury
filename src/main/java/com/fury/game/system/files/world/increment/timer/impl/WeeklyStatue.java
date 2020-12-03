package com.fury.game.system.files.world.increment.timer.impl;

import com.fury.core.model.node.entity.actor.figure.player.Player;
import com.fury.game.system.files.Resources;
import com.fury.game.system.files.world.increment.timer.TimedCleanFile;
import com.fury.game.world.GameWorld;

import java.util.Calendar;
import java.util.Iterator;

public class WeeklyStatue extends TimedCleanFile {

    private static WeeklyStatue weeklyStatue = new WeeklyStatue();

    public static WeeklyStatue get() {
        return weeklyStatue;
    }

    @Override
    public String getFile() {
        return Resources.getSaveDirectory("saves") + "statue.txt";
    }

    @Override
    public long getPeriod() {
        return TWENTY_FOUR_HOURS * 7;
    }

    @Override
    public Calendar getResetTime() {
        Calendar date = Calendar.getInstance();
        date.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
        date.set(Calendar.HOUR, 0);
        date.set(Calendar.MINUTE, 0);
        date.set(Calendar.SECOND, 0);
        date.set(Calendar.MILLISECOND, 0);
        return date;
    }

    @Override
    public void action(Calendar date) {
        Iterator<Player> players = GameWorld.getPlayers().iterator();
        while(players.hasNext()) {
            Player player = players.next();
            if(player != null) {
                if(get(player.getUsername()) == 15) {
                    records.remove(player.getUsername());
                    player.getStatue().clearSkills();
                }
            }
        }

        records.entrySet().removeIf(pair -> pair.getValue() == 15);
    }
}
