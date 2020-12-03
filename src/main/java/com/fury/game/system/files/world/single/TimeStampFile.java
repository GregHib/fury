package com.fury.game.system.files.world.single;

import com.fury.game.system.files.world.WorldSaveFile;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public abstract class TimeStampFile extends WorldSaveFile {
    protected Map<String, Long> records = new ConcurrentHashMap<>();

    public void init() {
        records.clear();
        read();
    }

    public long get(String identifier) {
        if (identifier != null && records.containsKey(identifier))
            return records.get(identifier);
        return 0;
    }

    public boolean has(String identifier) {
        if (identifier != null && records.containsKey(identifier))
            return true;
        return false;
    }

    public void record(String identifier, long number) {
        if(identifier == null)
            return;

        if (records.containsKey(identifier)) {
            long amount = records.get(identifier);
            records.put(identifier, amount + number);
        } else
            records.put(identifier, number);
    }

    public void remove(String identifier) {
        if (identifier != null && records.containsKey(identifier))
            records.remove(identifier);
    }

    @Override
    public void read(String line) {
        if(line == null)
            return;

        String[] parts = line.split(",", 2);
        String identifier = parts[0].trim();
        long count = Long.parseLong(parts[1].trim());
        records.put(identifier, count);
    }

    @Override
    public void write(BufferedWriter bw) throws IOException {
        for (String key : records.keySet())
            bw.write(key + "," + records.get(key) + System.lineSeparator());
    }
}
