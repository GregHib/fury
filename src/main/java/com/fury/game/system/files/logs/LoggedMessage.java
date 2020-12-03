package com.fury.game.system.files.logs;

import com.fury.util.Misc;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;

public class LoggedMessage {

    String message;
    String to;
    long timestamp;

    public LoggedMessage(String message, String to) {
        this(Misc.currentTimeMillis(), message, to);
    }

    public LoggedMessage(long timestamp, String message, String to) {
        this.message = message;
        this.to = to;
        this.timestamp = timestamp;
    }

    public String getMessage() {
        return message;
    }

    public String getTo() {
        return to;
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
