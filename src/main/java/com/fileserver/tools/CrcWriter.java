package com.fileserver.tools;

import com.fury.game.GameLoader;
import com.fury.game.system.files.Resources;

import java.io.DataOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.CRC32;

public class CrcWriter {
    private static final CRC32 crc32 = new CRC32();
    private static int[][] crcs;

    public static void main(String[] args) throws Exception {
        GameLoader.getCache().init();

        writeAll();
    }

    public static void writeAll() {
        int indexes = 7;
        System.out.println("Cache Indexes: " + indexes);
        crcs = new int[indexes][];
        for(int i = 0; i < crcs.length; i++) {
            writeChecksumList(i);
            writeVersionList(i);
        }
        System.out.println("Complete.");
    }

    private static String getName(int type) {
        switch (type) {
            case 0:
                return "model_659";
            case 1:
                return "model_742";
            case 2:
                return "model_830";
            case 3:
                return "anim_659";
            case 4:
                return "anim_742";
            case 5:
                return "map_659";
            case 6:
                return "map_742";
            case 7:
                return "midi";
        }
        return "";
    }

    private static int getSize(int type) {
        //These should auto grab from cache
        switch (type) {
            case 0:
                return 65535;
            case 1:
                return 78513;
            case 2:
                return 101597;//getModelCount();
            case 3:
                return 3696;
            case 4:
                return 4193;
            case 5:
                return 6827;
            case 6:
                return 8487;
            case 7:
                return 645;//midiIndices.length;
            /*case 8:
                return 0;//getAnimCount();
            case 9:
                return 0;//getMapAmount();
            case 10:
                return 34026;
            case 11:
                return 3159;
            case 12:
                return 1956;*/
        }
        return 0;
    }


    public static void writeChecksumList(int type) {
        try {
            DataOutputStream out = new DataOutputStream(new FileOutputStream(Resources.getSaveDirectory("cache") + "crc/" + getName(type) + "_crc"));
            int total = 0;
            int size = getSize(type);//GameLoader.getCache().CACHES[type + 1].getFileCount()
            System.out.println("File count: " + size);
            for (int index = 0; index < size; index++) {
                int sum = getChecksum(type, index);
                out.writeInt(sum);
                total++;
            }
            System.out.println(type + "-" + total);
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    /**
     * Grabs the checksum of a file from the cache.
     *
     * @param type The type of file (0 = model, 1 = anim, 2 = midi, 3 = map).
     * @param id The id of the file.
     * @return
     */
    public static int getChecksum(int type, int id) {
        int crc = 0;
        byte[] data = null;
        try {
            data = GameLoader.getCache().CACHES[type + 1].decompress(id);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (data != null && data.length > 2) {
            int length = data.length - 2;
            crc32.reset();
            crc32.update(data, 0, length);
            crc = (int) crc32.getValue();
        }
        return crc;
    }

    public static void writeVersionList(int type) {
        try {
            DataOutputStream out = new DataOutputStream(new FileOutputStream(Resources.getSaveDirectory("cache") + "crc/" + getName(type) + "_version"));
            for (int index = 0; index < GameLoader.getCache().CACHES[type + 1].getFileCount(); index++) {
                out.writeShort(getVersion(type, index));
            }
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Grabs the version of a file from the cache.
     *
     * @param type The type of file (0 = model, 1 = anim, 2 = midi, 3 = map).
     * @param id The id of the file.
     * @return
     */
    public static int getVersion(int type, int id) {
        int version = 1;
        byte[] data = null;
        try {
            data = GameLoader.getCache().CACHES[type + 1].decompress(id);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (data != null && data.length > 2) {
            int length = data.length - 2;
            version = ((data[length] & 0xff) << 8) + (data[length + 1] & 0xff);
        }
        return version;
    }
}
