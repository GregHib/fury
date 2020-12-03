package com.fury.game.system.files.world;

import java.io.BufferedWriter;
import java.io.IOException;

public interface WorldFile {

    String getFile();

    void read(String line);

    void write(BufferedWriter bw) throws IOException;
}
