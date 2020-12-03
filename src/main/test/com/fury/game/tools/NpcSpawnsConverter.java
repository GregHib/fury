package com.fury.game.tools;

import com.fury.game.system.files.Resources;
import com.fury.game.world.map.Position;
import com.fury.util.Misc;

import java.io.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class NpcSpawnsConverter {

    static Pattern pattern = Pattern.compile("^spawn[ \\t]+=[ \\t]+(\\d+)[ \\t]+(\\d+)[ \\t]+(\\d+)[ \\t]+(\\d+)[ \\t]+(\\d+).*");
    static Pattern pattern2 = Pattern.compile("(\\d+)[ \\t]+-[ \\t]+(\\d+)[ \\t]+(\\d+)[ \\t]+(\\d+).*");
    public static void main(String[] args) {
        Resources.init();
        try {
            BufferedReader in = new BufferedReader(new FileReader(System.getProperty("user.dir") + "\\src\\main\\test\\com\\fury\\game\\tools\\spawns.txt"));
            while (true) {
                String line = in.readLine();
                if (line == null)
                    break;
                if (line.startsWith("//") || line.startsWith("RSBOT"))
                    continue;

                Matcher matcher = pattern2.matcher(line);
                if(matcher.matches()) {
                    String[] values = Misc.getValues(matcher);

                    Position tile = new Position(Integer.parseInt(values[2]), Integer.parseInt(values[3]), Integer.parseInt(values[4]));
                    addNPCSpawn(Integer.parseInt(values[1]), tile, /*Integer.parseInt(values[5])*/1);
                }
            }
            in.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private static final void addNPCSpawn(int id, Position position, int walk) {
        System.out.println(Resources.getDirectory("spawns") + position.getRegionId() + ".txt");
//        if(true)
//            return;
        File file = new File(Resources.getDirectory("spawns") + position.getRegionId() + ".txt");
        BufferedWriter bw = null;
        FileWriter fw = null;

        boolean isNew = false;
        try {

            // if file doesnt exists, then create it
            if (!file.exists()) {
                file.createNewFile();
                isNew = true;
            }

            // true = append file
            fw = new FileWriter(file.getAbsoluteFile(), true);
            bw = new BufferedWriter(fw);

            if(isNew)
                bw.write("//id  - x      y      z   walk face   revision \n");

            bw.write(id + " - " + position.getX() + "   " + position.getY() + "   " + position.getZ() + "   " + walk + "   NORTH   0\n");

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (bw != null)
                    bw.close();

                if (fw != null)
                    fw.close();

            } catch (IOException ex) {

                ex.printStackTrace();

            }
        }
    }
}
