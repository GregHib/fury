package com.fury.game.system.files.loaders.npc;

import com.fury.cache.Revision;
import com.fury.game.GameSettings;
import com.fury.game.system.files.Resources;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.HashMap;

/**
 * Created by Greg on 26/12/2016.
 */
public class MobBonuses {
    private final static HashMap<Integer, int[]> bonuses = new HashMap<>();

    public static void init() {
        if(GameSettings.PACK)
            loadUnpackedBonuses();

        if (new File(Resources.getFile("packedBonuses")).exists())
            loadPackedBonuses();
    }

    public static int[] getBonuses(int id, Revision revision) {
        //TODO add revision to bonuses
        return bonuses.get(id);
    }

    private static void loadUnpackedBonuses() {
        //Logger.log("NpcBonuses", "Packing npc bonuses...");
        try {
            DataOutputStream out = new DataOutputStream(new FileOutputStream(Resources.getFile("packedBonuses")));
            BufferedReader in = new BufferedReader(new FileReader(Resources.getFile("unpackedBonuses")));
            while (true) {
                String line = in.readLine();
                if (line == null)
                    break;
                if (line.startsWith("//"))
                    continue;
                String[] splitedLine = line.split(" - ", 2);
                if (splitedLine.length != 2)
                    throw new RuntimeException("Invalid Mob Bonuses line: "
                            + line);
                int npcId = Integer.parseInt(splitedLine[0]);
                String[] splitedLine2 = splitedLine[1].split(" ", 10);
                if (splitedLine2.length != 10)
                    throw new RuntimeException("Invalid Mob Bonuses line: "
                            + line);
                int[] bonuses = new int[10];
                out.writeShort(npcId);
                for (int i = 0; i < bonuses.length; i++) {
                    bonuses[i] = Integer.parseInt(splitedLine2[i]);
                    out.writeShort(bonuses[i]);
                }
                MobBonuses.bonuses.put(npcId, bonuses);
            }
            in.close();
            out.close();
        } catch (Throwable e) {
            //Logger.handle(e);
            e.printStackTrace();
        }
    }

    private static void loadPackedBonuses() {
        try {
//            File file = new File(System.getProperty("user.dir") + "./data/def/npcs/unpackedBonuses.txt");
//            if(!file.exists())
//                file.createNewFile();
//            BufferedWriter out = new BufferedWriter(new FileWriter(file));
            RandomAccessFile in = new RandomAccessFile(Resources.getFile("packedBonuses"), "r");
            FileChannel channel = in.getChannel();
            ByteBuffer buffer = channel.map(FileChannel.MapMode.READ_ONLY, 0,
                    channel.size());
            while (buffer.hasRemaining()) {
                int npcId = buffer.getShort() & 0xffff;
                int[] bonuses = new int[10];
                for (int i = 0; i < bonuses.length; i++)
                    bonuses[i] = buffer.getShort();
//                out.write(npcId + " - " + bonuses[0] + " " + bonuses[1] + " " + bonuses[2] + " " + bonuses[3] + " " + bonuses[4] + " " + bonuses[5] + " " + bonuses[6] + " " + bonuses[7] + " " + bonuses[8] + " " + bonuses[9]);
//                out.newLine();
                MobBonuses.bonuses.put(npcId, bonuses);
            }
            channel.close();
            in.close();
//            out.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private MobBonuses() {

    }
}
