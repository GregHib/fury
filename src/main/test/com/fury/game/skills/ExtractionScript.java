package com.fury.game.skills;

import com.fury.game.content.skill.Skill;
import com.fury.util.Misc;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ExtractionScript {
    /*
    Skill - info script - tab script

    prayer main - 2788
    magic main - 2787
     */
    static int[][] scriptIds = new int[][]{
            {982, 981},
            {984, 983},
            {978, 977},
            {1010, 1009},
            {1004, 1003},
            {996, 995},
            {994, 993},
            {1008, 1007},
            {1002, 1001},
            {992, 991},
            {1000, 999},
            {980, 979},
            {1016, 1015},
            {1022, 1021},
            {998, 997},
            {1018, 1017},
            {986, 985},
            {990, 989},
            {1012, 1011},
            {988, 987},
            {1014, 1013},
            {3, 2},
            {1006, 1005},
            {1020, 1019},
            {3355, 3354}
    };
    private static final Pattern extraInfo = Pattern.compile("\\breturn\\s(-?\\d+),\\s(.*\\(AttrMap\\)(\\d+),.*),\\s(\".*\"),\\s(\".*\");");
    private static final Pattern skillInfo = Pattern.compile("^\t\tcase\\s(\\d+):|\\breturn\\s(-?\\d+),\\s(\\d+),\\s(\".*\"),\\s(\".*\");");
    private static final Pattern tabInfo = Pattern.compile("\\breturn\\s\"(.*)\",");

    private static boolean first = true;

    public static void main(String[] args) throws IOException {
        FileInputStream fstream = null;
        BufferedReader br = null;

        //for(Skill skill : Skill.values()) {
        Skill skill = Skill.AGILITY;
        int[] scripts = scriptIds[skill.ordinal()];
        //System.out.print(skill.getName().toUpperCase() + "(");
        fstream = new FileInputStream("C:\\Users\\Greg\\Desktop\\cs2\\" + /*scripts[0]*/2787 + ".cs2");
        br = new BufferedReader(new InputStreamReader(fstream));

        String strLine;
        first = true;

        int current = 0;
        System.out.println("public class " + Misc.uppercaseFirst(skill.getName()) + "Menu {");
        System.out.println("public static Object[][][] " + skill.getName() + " = {");
        while ((strLine = br.readLine()) != null) {
            Matcher m = skillInfo.matcher(strLine);

            if (m.find()) {
//                    System.out.println(m.group(2));
//                    System.out.println(m.group(3));
//                    System.out.println(m.group(4));
                if (m.group(2) == null) {
                    if (first) {
                        System.out.println("{");
                        first = false;
                    } else {
                        System.out.println("},");
                        System.out.println("{");
                    }
                } else {
                    System.out.println("{" + m.group(2) + ", " + m.group(3) + ", " + m.group(4) + ", " + m.group(5) + "},");
                }
            }
        }
        System.out.println("}");
        System.out.println("};");
        System.out.println("}");

        br.close();
        // }
    }

    public static void dumpEnum() throws IOException {
        FileInputStream fstream = null;
        BufferedReader br = null;

        for (Skill skill : Skill.values()) {
            int[] scripts = scriptIds[getSkillInt(skill) - 1];
            System.out.println(skill.getName().toUpperCase() + "(Skill." + skill.getName().toUpperCase() + ",");
            fstream = new FileInputStream("C:\\Users\\Greg\\Desktop\\cs2\\" + scripts[1] + ".cs2");
            br = new BufferedReader(new InputStreamReader(fstream));

            System.out.println("new String[] {" + getTabInfo(br) + "},");

            br.close();

            System.out.println("new int[][] {");
            fstream = new FileInputStream("C:\\Users\\Greg\\Desktop\\cs2\\" + scripts[0] + ".cs2");
            br = new BufferedReader(new InputStreamReader(fstream));

            printSkillInfo(br);
            System.out.println("}),");

            br.close();
        }
    }

    public static String getTabInfo(BufferedReader br) throws IOException {
        String strLine;

        List<String> tabs = new ArrayList<>();
        int current = 0;
        while ((strLine = br.readLine()) != null) {
            Matcher m = tabInfo.matcher(strLine);

            if (m.find()) {
                tabs.add("\"" + m.group(1) + "\"");
                /*if(m.group(2) == null) {
                    current = Integer.valueOf(m.group(1));
                } else {
                }*/
            }
        }
        String text = Arrays.toString(tabs.toArray(new String[tabs.size()]));
        return text.substring(1, text.length() - 1);
    }

    public static void printSkillInfo(BufferedReader br) throws IOException {
        String strLine;

        HashMap<Integer, List<Integer>> items = new HashMap<>();
        int current = 0;
        while ((strLine = br.readLine()) != null) {
            Matcher m = skillInfo.matcher(strLine);

            if (m.find()) {
                if (m.group(2) == null) {
                    current = Integer.valueOf(m.group(1));
                } else {
                    if (items.get(current) == null)
                        items.put(current, new ArrayList<>());
                    items.get(current).add(Integer.parseInt(m.group(3)));
                }
            }
        }

        for (List<Integer> list : items.values()) {
            String string = Arrays.toString(list.toArray(new Integer[list.size()]));

            System.out.println("{" + string.substring(1, string.length() - 1) + "},");
        }
    }

    public static int getSkillInt(Skill skill) {
        switch (skill) {
            case ATTACK:
                return 1;
            case STRENGTH:
                return 2;
            case DEFENCE:
                return 3;
            case RANGED:
                return 4;
            case PRAYER:
                return 5;
            case MAGIC:
                return 6;
            case CONSTITUTION:
                return 7;
            case AGILITY:
                return 8;
            case HERBLORE:
                return 9;
            case THIEVING:
                return 10;
            case CRAFTING:
                return 11;
            case FLETCHING:
                return 12;
            case MINING:
                return 13;
            case SMITHING:
                return 14;
            case FISHING:
                return 15;
            case COOKING:
                return 16;
            case FIREMAKING:
                return 17;
            case WOODCUTTING:
                return 18;
            case RUNECRAFTING:
                return 19;
            case SLAYER:
                return 20;
            case FARMING:
                return 21;
            case CONSTRUCTION:
                return 22;
            case HUNTER:
                return 23;
            case SUMMONING:
                return 24;
            case DUNGEONEERING:
                return 25;
        }
        return -1;
    }
}
