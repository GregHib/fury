package com.fury.game.system.files.loaders.item;

import com.fury.game.GameSettings;
import com.fury.core.model.item.Item;
import com.fury.game.system.files.Resources;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.HashMap;

public class ItemWeights {

    private final static HashMap<Integer, Double> itemWeights = new HashMap<>();
    private static final int[] NEGATIVE_WEIGHT_ITEMS = {88, 10553, 10069, 10071, 24210, 24208, 24206, 14936, 14938, 24560, 24561, 24562, 24563, 24564};

    public static final void init() {
        if(GameSettings.PACK)
            loadUnpackedItemWeights();
        if (new File(Resources.getFile("packedWeights")).exists())
            loadPackedItemWeights();
    }

    public static final double getWeight(Item item, boolean equiped) {
        if (item.getDefinition().isNoted())
            return 0;
        Double weight = itemWeights.get(item.getId());
        if(weight == null)
            return 0;

        if(equiped)
            for(int i : NEGATIVE_WEIGHT_ITEMS)
                if(i == item.getId())
                    return -weight;

        return weight;
    }

    private static void loadPackedItemWeights() {
        try {
            RandomAccessFile in = new RandomAccessFile(Resources.getFile("packedWeights"), "r");
            FileChannel channel = in.getChannel();
            ByteBuffer buffer = channel.map(FileChannel.MapMode.READ_ONLY, 0, channel.size());
            while (buffer.hasRemaining()) {
                itemWeights.put(buffer.getShort() & 0xffff, buffer.getDouble());
            }
            channel.close();
            in.close();
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    private static void loadUnpackedItemWeights() {
        try {
            BufferedReader in = new BufferedReader(new FileReader(Resources.getFile("unpackedWeights")));
            @SuppressWarnings("resource")
            DataOutputStream out = new DataOutputStream(new FileOutputStream(Resources.getFile("packedWeights")));
            while (true) {
                String line = in.readLine();
                if (line == null)
                    break;
                if (line.startsWith("//"))
                    continue;
                line = line.replace("﻿", "");
                String[] splitedLine = line.split(" - ", 2);
                if (splitedLine.length < 2) {
                    in.close();
                    throw new RuntimeException("Invalid list for item weight line: " + line);
                }
                int itemId = Integer.valueOf(splitedLine[0]);
                double weight = Double.valueOf(splitedLine[1]);
                out.writeShort(itemId);
                out.writeDouble(weight);
                itemWeights.put(itemId, weight);
            }
            in.close();
            out.flush();
            out.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
