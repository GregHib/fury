package com.fury.game.entity.character.combat.equipment;

import com.fury.game.GameSettings;
import com.fury.game.system.files.Resources;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.Set;
import java.util.TreeMap;

public class ItemBonuses {
    private static TreeMap<Integer, int[]> itemBonuses;

    public static final void reload() {
        itemBonuses.clear();
        init();
    }

    public static final void init() {
        long start = System.currentTimeMillis();
        if (GameSettings.PACK)
            packItemBonuses();
        if (new File(Resources.getFile("bonuses")).exists())
            loadItemBonuses();
        else
            throw new RuntimeException("Missing item bonuses.");

        if (GameSettings.DEBUG)
            System.out.println("Item bonuses loaded in " + (System.currentTimeMillis() - start) + "ms");
    }

    public static final int[] getItemBonuses(int itemId) {
        return itemBonuses.get(itemId);
    }

    private static final void loadItemBonuses() {
        try {
            RandomAccessFile in = new RandomAccessFile(Resources.getFile("bonuses"), "r");
            FileChannel channel = in.getChannel();
            ByteBuffer buffer = channel.map(FileChannel.MapMode.READ_ONLY, 0, channel.size());
            itemBonuses = new TreeMap<>();
            while (buffer.hasRemaining()) {
                int itemId = buffer.getShort() & 0xffff;
                int[] bonuses = new int[18];
                for (int index = 0; index < bonuses.length; index++)
                    bonuses[index] = buffer.getShort();
                itemBonuses.put(itemId, bonuses);
            }
            channel.close();
            in.close();
            buffer.clear();
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    private static void packItemBonuses() {
        try {
            BufferedReader br = new BufferedReader(new FileReader(Resources.getDirectory("items") + "unpackedBonuses.txt"));
            DataOutputStream out = new DataOutputStream(new FileOutputStream(Resources.getFile("bonuses")));
            try {
                String line;

                while ((line = br.readLine()) != null) {
                    if (line.startsWith("//"))
                        continue;

                    String[] parts = line.split(" ");
                    int id = Integer.parseInt(parts[0]);
                    out.writeShort(id);
                    for (int i = 0; i < 18; i++)
                        out.writeShort(Integer.parseInt(parts[1 + i]));
                }
            } finally {
                br.close();
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    private static void unpackItemBonuses() {
        try {
            FileWriter fw = new FileWriter(Resources.getDirectory("items") + "unpackedBonuses.txt");
            BufferedWriter out = new BufferedWriter(fw);

            out.write("//id att_stab att_slash att_crush att_magic att_range def_stab def_slash def_crush def_magic def_range def_summoning strength_bonus ranged_str_bonus magic_damage prayer_bonus absorb_melee absorb_magic absorb_range" + System.lineSeparator());

            Set<Integer> keys = itemBonuses.keySet();
            for (int itemId : keys) {
                int[] bonuses = itemBonuses.get(itemId);
                out.write(itemId + " ");
                for (int i = 0; i < bonuses.length; i++)
                    out.write(bonuses[i] + " ");
                out.write(System.lineSeparator());
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    private ItemBonuses() {

    }
}
