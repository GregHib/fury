package com.fileserver.tools;

import com.fileserver.cache.impl.Cache;
import com.fury.game.GameLoader;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class CacheDumper {

    public static void main(String[] args) throws Exception {
        GameLoader.getCache().init();
        dumpArchive(1, GameLoader.getCache().CACHES);
    }

    public static void dumpArchive(int archive, Cache[] indices) {
        try {
            new File(System.getProperty("user.dir") + "/data/cache/dump" + archive + "/").mkdirs();
            System.out.println(indices[archive].getFileCount() + " files found.");
            for (int i = 0; i < indices[archive].getFileCount(); i++) {
                byte[] data = null;
                try {
                    data = indices[archive].get(i).array();
                } catch (NullPointerException e1) {
                    continue;
                }
                if (data == null)
                    continue;
                File map = new File(System.getProperty("user.dir") + "/data/cache/dump" + archive + "/" + i + (true ? ".dat" : ".gz"));
                FileOutputStream fos = new FileOutputStream(map);
                fos.write(data);
                fos.close();
                System.out.println("Dumped file: " + i);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
