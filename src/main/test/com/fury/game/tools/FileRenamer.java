package com.fury.game.tools;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Comparator;

import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;

public class FileRenamer {
    public static void main(String[] args) throws IOException {
        int renameTo = 1717;
        File path = new File("C:\\Users\\Greg\\.fury\\cache\\hitsplats");
        File toDir = new File("C:\\Users\\Greg\\.fury\\cache\\hitsplats\\copy");
        Path copy = toDir.toPath();

        if(!toDir.exists())
            toDir.mkdir();

        File[] files = path.listFiles(filesOnly);
        Arrays.sort(files, numerical);
        for (int i = 0; i < files.length; i++) {
            File file = files[i];
            Path from = file.toPath();
            if(Files.exists(from) && Files.isRegularFile(from)) {
                Path to = copy.resolve(renameTo + i + ".png");
                Files.copy(from, to, REPLACE_EXISTING);
                System.out.println("Copied: " + from);
            }
        }
    }

    static FileFilter filesOnly = pathname -> {
        String name = pathname.getName().toLowerCase();
        return name.endsWith(".png") && pathname.isFile();
    };

    static Comparator<File> numerical = new Comparator<File>() {
        @Override
        public int compare(File o1, File o2) {
            int n1 = extractNumber(o1.getName());
            int n2 = extractNumber(o2.getName());
            return n1 - n2;
        }

        private int extractNumber(String name) {
            int i = 0;
            try {
                int s = name.indexOf('_')+1;
                int e = name.lastIndexOf('.');
                String number = name.substring(s, e);
                i = Integer.parseInt(number);
            } catch(Exception e) {
                i = 0; // if filename does not match the format
                // then default to 0
            }
            return i;
        }
    };
}
