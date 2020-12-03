package com.fury.game.system.files.logs;

import java.util.Comparator;

public class LogComparator implements Comparator<LoggedItem> {
    @Override
    public int compare(LoggedItem item1, LoggedItem item2) {
        long t1 = item1.getTimestamp();
        long t2 = item2.getTimestamp();
        return t2 > t1 ? 1 : t1 > t2 ? -1 : 0;
    }
}
