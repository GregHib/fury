package com.fury.game.system.files.world.increment;

import com.fury.game.system.files.world.WorldSaveFile;

import java.io.*;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public abstract class CounterFile extends WorldSaveFile {

    protected Map<String, Integer> records = new ConcurrentHashMap<>();

    public void init() {
        read();
    }

    public int get(String identifier) {
        if(records.containsKey(identifier))
            return records.get(identifier);
        return 0;
    }

    public boolean has(String identifier) {
        if(records.containsKey(identifier))
            return true;
        return false;
    }

    public void remove(String identifier) {
        records.remove(identifier);
    }

    public void record(String identifier) {
        record(identifier, 1);
    }

    public void record(String identifier, int number) {
        if(records.containsKey(identifier)) {
            int amount = records.get(identifier);
            records.put(identifier, amount + number);
        } else
            records.put(identifier, number);
    }

    @Override
    public void read(String line) {
        String[] parts = line.split(",", 2);
        String identifier = parts[0].trim();
        int count = Integer.parseInt(parts[1].trim());
        records.put(identifier, count);
    }

    @Override
    public void write(BufferedWriter bw) throws IOException {
        for (String key : records.keySet())
            bw.write(key + "," + records.get(key) + System.lineSeparator());
    }
}
