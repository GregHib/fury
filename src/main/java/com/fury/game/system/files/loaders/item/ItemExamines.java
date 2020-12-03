package com.fury.game.system.files.loaders.item;

import com.fury.game.GameSettings;
import com.fury.core.model.item.Item;
import com.fury.game.system.files.Resources;
import com.fury.util.Misc;
import com.fury.util.Utils;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.HashMap;

public class ItemExamines {
    private final static HashMap<Integer, String> itemExamines = new HashMap<Integer, String>();


    public static final void init() {
        if(GameSettings.PACK)
            loadUnpackedItemExamines();
        if (new File(Resources.getFile("packedExamines")).exists())
            loadPackedItemExamines();
    }

    public static final String getExamine(Item item) {
        String name = item.getDefinition().getName();
        if (item.getAmount() >= 100000)
            return Utils.getFormattedNumber(item.getAmount()) + " x " + name + ".";
        if (item.getDefinition().isNoted())
            return "Swap this note at any bank for the equivalent item.";
        String examine = itemExamines.get(item.getId());
        if (examine != null)
            return examine;
        return "It's " + Misc.anOrA(name) + " " + name.toLowerCase() + ".";
    }

    private static void loadPackedItemExamines() {
        try {
            RandomAccessFile in = new RandomAccessFile(Resources.getFile("packedExamines"), "r");
            FileChannel channel = in.getChannel();
            ByteBuffer buffer = channel.map(FileChannel.MapMode.READ_ONLY, 0, channel.size());
            while (buffer.hasRemaining()) {
                int id = buffer.getShort() & 0xffff;
                String text = readString(buffer);
                itemExamines.put(id, text);
            }
            channel.close();
            in.close();
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    private static void loadUnpackedItemExamines() {
        try {
            BufferedReader in = new BufferedReader(new FileReader(Resources.getFile("unpackedExamines")));
            DataOutputStream out = new DataOutputStream(new FileOutputStream(Resources.getFile("packedExamines")));
            while (true) {
                String line = in.readLine();
                if (line == null)
                    break;
                if (line.startsWith("//"))
                    continue;
                line = line.replace("﻿", "");
                String[] splitLine = line.split(" - ", 2);
                if (splitLine.length < 2) {
                    in.close();
                    throw new RuntimeException("Invalid list for item examine line: " + line);
                }
                int itemId = Integer.valueOf(splitLine[0]);
                if (splitLine[1].length() > 255)
                    continue;
                out.writeShort(itemId);
                writeString(out, splitLine[1]);
                itemExamines.put(itemId, splitLine[1]);
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

    public static String readString(ByteBuffer buffer) {
        int count = buffer.get() & 0xff;
        byte[] bytes = new byte[count];
        buffer.get(bytes, 0, count);
        return new String(bytes);
    }

    public static void writeString(DataOutputStream out, String string) throws IOException {
        byte[] bytes = string.getBytes();
        out.writeByte(bytes.length);
        out.write(bytes);
    }
}
