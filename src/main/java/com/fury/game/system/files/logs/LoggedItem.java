package com.fury.game.system.files.logs;

import com.fury.cache.Revision;
import com.fury.core.model.item.Item;
import com.fury.util.Misc;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;

public class LoggedItem {
    Item item;
    long timestamp;

    public LoggedItem(Item item) {
        this.item = item.copy();
        this.timestamp = Misc.currentTimeMillis();
    }

    public LoggedItem(int id, long timestamp, int amount, Revision revision) {
        item = new Item(id, amount, revision);
        this.timestamp = timestamp;
    }

    public LoggedItem(int id, long timestamp, int amount) {
        item = new Item(id, amount);
        this.timestamp = timestamp;
    }

    public int getId() {
        return item.getId();
    }

    public String getName() {
        return item.getName();
    }

    public int getAmount() {
        return item.getAmount();
    }

    public Revision getRevision() {
        return item.getRevision();
    }

    public long getTimestamp() {
        return timestamp;
    }

    public String getTime() {
        Timestamp stamp = new Timestamp(timestamp);
        Date date = new Date(stamp.getTime());
        String timeFormat = "d/M k:mm:ssa";
        SimpleDateFormat sdf = new SimpleDateFormat(timeFormat);
        return "[" + sdf.format(date) + "] ";
    }
}
