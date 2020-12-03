package com.fury.game.system.files.world;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public abstract class WorldSaveFile implements WorldFile {

    public void read() {
        String fullPath = getFile();
        Path filePath = Paths.get(fullPath);
        if (!Files.exists(filePath)) {
            String directoryPath = fullPath.substring(0, fullPath.lastIndexOf('\\'));
            try {
                Files.createDirectories(Paths.get(directoryPath));
                Files.createFile(filePath);
            } catch (IOException e) {
                throw new RuntimeException("Error creating ["+directoryPath+"]", e);
            }
        }

        try(BufferedReader b = Files.newBufferedReader(filePath)) {
            b.lines().filter(s -> !s.isEmpty()).forEach(this::read);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void save() {
        File file = new File(getFile());
        try {
            if (!file.exists())
                file.createNewFile();

            FileWriter fw = new FileWriter(file.getAbsoluteFile());
            BufferedWriter bw = new BufferedWriter(fw);

            write(bw);

            bw.close();
            fw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
