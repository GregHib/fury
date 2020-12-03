package com.fury.tools;

import java.io.File;
import java.io.IOException;
import java.nio.file.CopyOption;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

/**
 * Created by Greg on 01/04/2017.
 */
public class GeneralTester {
    public static void main(String[] args) throws IOException {
        CopyOption[] options = new CopyOption[]{
                StandardCopyOption.REPLACE_EXISTING,
                StandardCopyOption.COPY_ATTRIBUTES
        };
        int[] ids = new int[65330];
        File[] files = new File("C:\\Users\\Greg\\Desktop\\zaros\\639 Model Dump\\").listFiles();
        for (File file : files) {
            if(file.isFile()) {
                if(file.getName().endsWith(".gz")) {
                    int id = Integer.parseInt(file.getName().substring(0, file.getName().length() - 3));
                    ids[id] = 1;
                }
            }
        }

        for(int i = 63154; i < ids.length; i++) {
            if(ids[i] != 1) {
                String source = "C:\\Users\\Greg\\Documents\\Projects\\Games\\Runescape Private Servers\\RSPS\\Data\\667\\667 Model Dump\\";
                String missing = "C:\\Users\\Greg\\Desktop\\zaros\\639 Model Dump\\Missing\\";
                Path file = new File(source + i + ".gz").toPath();
                Path to = new File(missing).toPath();
                Files.copy(file, to.resolve(file.getFileName()));
            }
        }
    }
}
