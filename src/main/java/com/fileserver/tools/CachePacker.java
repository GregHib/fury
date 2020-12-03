package com.fileserver.tools;

import com.fileserver.cache.impl.Cache;
import com.fury.game.GameLoader;
import com.fury.game.system.files.Resources;

import java.io.File;
import java.io.FileInputStream;

public class CachePacker {

    public static void main(String[] args) throws Exception {
        GameLoader.getCache().init();

        repackCacheIndex(7, GameLoader.getCache().CACHES);
    }

    /**
     *  models = index1
     *  animations = index2
     *  sounds = index3
     *  maps = index4
     *  639 models = index5
     *  osrs models (#154) = index6
     *  728 models = index 7
    */
    public static void repackCacheIndex(int cacheIndex, Cache[] indices) {
        System.out.println("Started repacking index " + cacheIndex + ".");
        File[] file = new File(indexLocation(cacheIndex, -1)).listFiles();
        if (file == null || file.length == 0)
            return;
        try {
            for (int index = 0; index < file.length; index++) {
                if (file[index].isFile()) {
                    int fileIndex = Integer.parseInt(getFileNameWithoutExtension(file[index].toString()));
                    byte[] data = fileToByteArray(cacheIndex, fileIndex);
                    if (data != null && data.length > 0) {
                        try {
                            indices[cacheIndex].writeFile(data.length, data, fileIndex);
                        } catch (Exception ee) {
                            continue;
                        }
                        System.out.println("Repacked Archive: " + cacheIndex + " File: " + fileIndex + ".");
                    } else {
                        System.out.println("Unable to locate index " + fileIndex + ".");
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error packing cache index " + cacheIndex + ".");
        }
        System.out.println("Finished repacking " + cacheIndex + ".");
    }

    private static String indexLocation(int cacheIndex, int index) {
        return Resources.getSaveDirectory("cache") + "index" + cacheIndex + "/" + (index != -1 ? index + ".gz" : "");
    }

    private static String getFileNameWithoutExtension(String fileName) {
        File tmpFile = new File(fileName);
        tmpFile.getName();
        int whereDot = tmpFile.getName().lastIndexOf('.');
        if (0 < whereDot && whereDot <= tmpFile.getName().length() - 2) {
            return tmpFile.getName().substring(0, whereDot);
        }
        return "";
    }

    private static byte[] fileToByteArray(int cacheIndex, int index) {
        try {
            if (indexLocation(cacheIndex, index).length() <= 0 || indexLocation(cacheIndex, index) == null)
                return null;
            File file = new File(indexLocation(cacheIndex, index));
            byte[] fileData = new byte[(int) file.length()];
            FileInputStream fis = new FileInputStream(file);
            fis.read(fileData);
            fis.close();
            return fileData;
        } catch (Exception e) {
            return null;
        }
    }
}
