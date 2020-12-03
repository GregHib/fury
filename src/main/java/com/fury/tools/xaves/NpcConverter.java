package com.fury.tools.xaves;

import com.fury.cache.ByteBuffer;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;

public class NpcConverter {

    public NpcConverter() {

    }
    public String[] actions;
    public int scaleXY;
    public int scaleZ;
    public int[] replacementColours;
    public int[] morphisms;
    public int combat;
    public int varp;
    public int rotation;
    public byte[] description;
    public int[] dialogueModels;
    public boolean clickable;
    public boolean drawMinimapDot;
    public int headIcon;
    public int lightModifier;
    public int shadowModifier;
    public String name;
    public int[] modelIds;
    public byte size;
    public int[] originalColours;
    public int idleAnimation;
    public int id;
    public int varbit;
    public boolean priorityRender;
    public int walkAnimation;
    public int halfTurnAnimation;
    public int rotateClockwiseAnimation;
    public int rotateAntiClockwiseAnimation;
    public HashMap<Integer, Object> clientScriptData;
    static int[] clickables = new int[] {415,
            416,
            417,
            1024,
            1025,
            1026,
            1027,
            1689,
            1690,
            1957,
            3093,
            3803,
            3813,
            4365,
            4366,
            4367,
            4368,
            4369,
            4370,
            4420,
            4473,
            4609,
            4765,
            4876,
            4877,
            5562,
            6065,
            6066,
            6067,
            6068,
            6069,
            6284,
            6319,
            7516,
            7517,
            7566,
            7568,
            7637,
            7638,
            7639,
            7712,
            7865,
            7968,
            8089,
            8265,
            8272,
            8367,
            8368,
            8369,
            8370,
            8371,
            8372,
            8373,
            8374,
            8375,
            8376,
            8553,
            8554,
            8593,
            8594,
            8595,
            8639,
            8742,
            8743,
            8744,
            8746,
            8747,
            8748,
            8831,
            8843,
            8844,
            8908,
            8909,
            8911,
            8912,
            8930,
            9034,
            9035,
            9134,
            9164,
            9167,
            9169,
            9171,
            9350,
            9351,
            9352,
            9440,
            9447,
            9672,
            9673,
            9674,
            9675,
            9721,
            9722,
            9723,
            9781,
            11596,
            11600,
            11637,
            12177,
            12338,
            12339,
            12340,
            12341,
            12395,
            13487,
            13779,
            13781,
            14069,
            14203,
            14306,
            14307,
            14308,
            14309,
            14310,
            14311,
            14312,
            14313,
            14314,
            14315,
            14316,
            14317,
            14349};
    private static int[] offsets;

    static NpcConverter[] npcs;

    public static void main(String[] args) throws Exception {
        String dir = "C:\\Users\\Greg\\FCE\\npc742.dat";
        String indexDir = "C:\\Users\\Greg\\FCE\\npc742.idx";
        File file = new File("C:\\Users\\Greg\\FCE\\npcsd");

        int totalNpcs = 15940;

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            int currentId = -1;
            int lastId = -1;
            NpcConverter toWrite = null;
            while ((line = br.readLine()) != null) {
                // process the line.
                if (line.startsWith("case")) {
                    toWrite = npcs[currentId] = new NpcConverter();
                    currentId = Integer.parseInt(line.substring(5, line.length() - 1));
                    if(currentId != lastId + 1)
                        System.out.println("Problem!");
                    lastId = currentId;
                } else if (line.startsWith("name")) {
                    toWrite.name = trim(line);
                } else if (line.startsWith("componentModels")) {//modelIds
                    toWrite.modelIds = getArr(trim(line));
                } else if (line.startsWith("standAnimation")) {//idleAnim
                    toWrite.idleAnimation = Integer.parseInt(trim(line));
                } else if (line.startsWith("walkAnimation")) {
                    toWrite.walkAnimation = Integer.parseInt(trim(line));
                } else if (line.startsWith("actions")) {
                    toWrite.actions = getStrArr(trim(line));
                } else if (line.startsWith("colour1")) {//original
                    toWrite.originalColours = getArr(trim(line));
                } else if (line.startsWith("colour2")) {
                    toWrite.replacementColours = getArr(trim(line));
                } else if (line.startsWith("combatLevel")) {
                    toWrite.combat = Integer.parseInt(trim(line));
                } else if (line.startsWith("anIntArray73")) {//dialogueModels
                    toWrite.dialogueModels = getArr(trim(line));
                } else if (line.startsWith("aBoolean87")) {//drawMinimapDot
                    toWrite.drawMinimapDot = Boolean.parseBoolean(trim(line));
                } else if (line.startsWith("anInt85")) {//lightModifier
                    toWrite.lightModifier = Integer.parseInt(trim(line));
                } else if (line.startsWith("anInt92")) {//shadowModifier
                    toWrite.shadowModifier = Integer.parseInt(trim(line));
                } else if (line.startsWith("aByte68")) {//size
                    toWrite.size = (byte) Integer.parseInt(trim(line));
                } else if (line.startsWith("anInt91")) {//scaleXY
                    toWrite.scaleXY = (byte) Integer.parseInt(trim(line));
                } else if (line.startsWith("anInt86")) {//scaleZ
                    toWrite.scaleZ = (byte) Integer.parseInt(trim(line));
                } else if (line.startsWith("childrenIDs")) {//morphisms
                    toWrite.morphisms = getArr(trim(line));
                } else if (line.startsWith("anInt57")) {//varbit
                    toWrite.varbit = Integer.parseInt(trim(line));
                } else if (line.startsWith("aBoolean93")) {//priorityRender
                    toWrite.priorityRender = Boolean.parseBoolean(trim(line));
                } else if (line.startsWith("anInt59")) {//varp (varbit -1 first)
                    toWrite.varp = Integer.parseInt(trim(line));
                } else if (line.startsWith("anInt79")) {//rotation
                    toWrite.rotation = Integer.parseInt(trim(line));
                } else if (line.startsWith("anInt75")) {//headIcon
                    toWrite.headIcon = Integer.parseInt(trim(line));
                } else if (line.startsWith("break")) {
                    toWrite = null;
                    currentId = -1;
                } else {
                    System.out.println("Unrecognised: " + line);
                }
            }
        }

        ByteBuffer buffer = new ByteBuffer(new byte[10000000]);
        DataOutputStream idx = new DataOutputStream(new FileOutputStream(indexDir));
        DataOutputStream dat = new DataOutputStream(new FileOutputStream(dir));
        idx.writeShort(totalNpcs);

        for(int index = 0; index < npcs.length; index++) {
            NpcConverter def = npcs[index];

            if(def.modelIds != null && def.modelIds.length > 0) {
                buffer.putByte(1);
                buffer.putByte(def.modelIds.length);
                for(int model : def.modelIds)
                    buffer.putShort(model);
            }
            if(def.name != null && def.name.length() > 0) {
                buffer.putByte(2);
                buffer.putString(def.name);
            }

            if(def.size > 1) {
                buffer.putByte(3);
                buffer.putByte(def.size);
            }

            if(def.idleAnimation != -1) {
                if(def.idleAnimation == 32767)
                    System.out.println("Broken anim!");
                buffer.putByte(13);
                buffer.putInt(def.idleAnimation);
            }

            if(def.walkAnimation != -1) {
                if(def.walkAnimation == 32767)
                    System.out.println("Broken anim!");
                buffer.putByte(14);
                buffer.putInt(def.walkAnimation);
            }

            if(def.actions != null && def.actions.length > 0) {
                for(int i = 0; i < def.actions.length; i++) {
                    buffer.putByte(30 + i);
                    buffer.putString(def.actions[i]);
                }
            }

            if(def.originalColours != null && def.originalColours.length > 0) {
                if(def.originalColours.length != def.replacementColours.length)
                    System.out.println("Colour mismatch!");

                buffer.putByte(40);
                for(int i = 0; i < def.originalColours.length; i++) {
                    buffer.putShort(def.originalColours[i]);
                    buffer.putShort(def.replacementColours[i]);
                }
            }

            if(def.dialogueModels != null && def.dialogueModels.length > 0) {
                buffer.putByte(60);
                buffer.putByte(def.dialogueModels.length);
                for(int model : def.dialogueModels)
                    buffer.putShort(model);
            }

            if(def.drawMinimapDot == false) {
                buffer.putByte(93);
            }

            if(def.combat == -1) {
                buffer.putByte(95);
                buffer.putShort(def.combat);
            }

            if(def.scaleXY == 128) {
                buffer.putByte(97);
                buffer.putShort(def.scaleXY);
            }

            if(def.scaleZ == 128) {
                buffer.putByte(98);
                buffer.putShort(def.scaleZ);
            }

            if(def.priorityRender == true) {
                buffer.putByte(99);
            }

            if(def.lightModifier != 0) {
                buffer.putByte(100);
                buffer.putByte(def.lightModifier);
            }

            if(def.shadowModifier != 0) {
                buffer.putByte(101);
                buffer.putByte(def.shadowModifier);
            }

            if(def.headIcon != -1) {
                buffer.putByte(102);
                buffer.putShort(def.headIcon);
            }

            if(def.rotation != 32) {
                buffer.putByte(103);
                buffer.putShort(def.rotation);
            }

            if(def.varbit != -1 || def.varp != -1) {
                buffer.putByte(106);
                buffer.putShort(def.varbit);
                buffer.putShort(def.varp);

                if(def.morphisms != null) {
                    buffer.putShort(def.morphisms.length);
                    for(int morph : def.morphisms) {
                        buffer.putShort(morph);
                    }
                } else {
                    buffer.putShort(0);
                }
            }

            for(int id : clickables) {
                if(def.id == id) {
                    buffer.putByte(107);
                    break;
                }
            }

            buffer.putByte(0);
            byte[] out = new byte[buffer.position];
            System.arraycopy(buffer.buffer, 0, out, 0, buffer.position);
            dat.write(out);
            idx.writeShort(out.length);
        }

        dat.close();
        idx.close();


        System.out.println("READ");

        Path datPath = Paths.get(dir);
        Path idxPath = Paths.get(indexDir);
        byte[] datData = Files.readAllBytes(datPath);
        byte[] idxData = Files.readAllBytes(idxPath);
        ByteBuffer datBuffer = new ByteBuffer(datData);
        ByteBuffer indexBuffer = new ByteBuffer(idxData);
        totalNpcs = indexBuffer.getUnsignedShort();
        offsets = new int[totalNpcs];

        int pos = 0;
        for (int i = 0; i < totalNpcs; i++) {
            int size = indexBuffer.getUnsignedShort();
            if(i < 10)
                System.out.println(i + " " + size);
            offsets[i] = pos;
            pos += size;
        }


        /*int id = 1185;
        NpcDefinition out = new NpcDefinition();
        out.id = id;
        NpcDefinition742.readValueLoop(datBuffer, out);
        System.out.println(out.name);*/
    }

    private static String trim(String val) {
        return val.split(" = ")[1].replaceAll(";", "");
    }

    private static int[] getArr(String val) {
        String value = val.trim().replaceAll("new int\\[]\\{", "").replaceAll("}", "");
        String[] parts = value.split(", ");
        int[] arr = new int[parts.length];
        for (int i = 0; i < parts.length; i++)
            arr[i] = Integer.parseInt(parts[i]);
        return arr;
    }

    private static String[] getStrArr(String val) {
        String value = val.trim().replaceAll("new String\\[]\\{", "").replaceAll("}", "");
        String[] parts = value.split(", ");
        String[] arr = new String[parts.length];
        for (int i = 0; i < parts.length; i++)
            arr[i] = parts[i].equalsIgnoreCase("null") ? null : parts[i];
        return arr;
    }
}
