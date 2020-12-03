package com.fury.cache.def.npc;


import com.fileserver.cache.impl.CacheArchive;
import com.fury.cache.ByteBuffer;
import com.fury.cache.Revision;

import java.util.HashMap;

public class NpcDefinition742 extends NpcDefinition {
    public static NpcDefinition742[] cache;
    protected static ByteBuffer buffer;
    protected static int[] indices;
    public static int total;

    public NpcDefinition742(int id) {
        super(id);
        revision = Revision.PRE_RS3;
    }

    public void load(CacheArchive archive, String name) {
        buffer = new ByteBuffer(archive.get(name + ".dat"));
        ByteBuffer index = new ByteBuffer(archive.get(name + ".idx"));

        total = index.getUnsignedShort();
        indices = getIndices(index, total);

        cache = new NpcDefinition742[total];
    }

    public NpcDefinition742 forId(int id) {
        NpcDefinition742 definition = id < 0 || id >= cache.length ? null : cache[id];
        if(definition == null) {
            definition = new NpcDefinition742(id);

            if(id >= indices.length || id < 0)
                return definition;

            buffer.position = indices[id];
            definition.readValueLoop(buffer);

            definition.changeValues();

            cache[id] = definition;
        }
        return definition;
    }

    @Override
    public void readValues(ByteBuffer buffer, int opcode) {
        if (opcode == 1) {
            int size = buffer.readUnsignedByte();
            modelIds = new int[size];
            for (int index = 0; index < size; index++) {
                modelIds[index] = buffer.readBigSmart();
                if (modelIds[index] == 65535)
                    modelIds[index] = -1;
            }
        } else if (opcode == 2) {
            name = buffer.readString();
        } else if (opcode == 12) {
            size = (byte) buffer.readUnsignedByte();
        } else if (opcode == 13) {
            idleAnimation = buffer.readShort();
        } else if (opcode == 14) {
            walkAnimation = buffer.readShort();
        } else if (opcode == 15) {
            runAnimation = buffer.readShort();
        } else if (opcode == 17) {
            int temp = buffer.readShort();
            int walkAnimation = temp;
            int halfTurnAnimation = buffer.readShort();
            int rotateClockwiseAnimation = buffer.readShort();
            int rotateAntiClockwiseAnimation = buffer.readShort();
        } else if (opcode >= 30 && opcode < 35) {
            options[opcode - 30] = buffer.readString();
            if (options[opcode - 30].equalsIgnoreCase("Hidden"))
                options[opcode - 30] = null;
        } else if (opcode == 40) {
            int i = buffer.readUnsignedByte();
            int[] replacementColours = new int[i];//TODO - short
            int[] originalColours = new int[i];
            for (int index = 0; index < i ; index++) {
                originalColours[index] = (short) buffer.readUnsignedShort();
                replacementColours[index] = (short) buffer.readUnsignedShort();
            }
        } else if (opcode == 41) {
            int i = buffer.readUnsignedByte();
            short[] originalTextureColours = new short[i];
            short[] replacementTextureColours = new short[i];
            for (int index = 0; index < i; index++) {
                originalTextureColours[index] = (short) buffer.readUnsignedShort();
                replacementTextureColours[index] = (short) buffer.readUnsignedShort();
            }
        } else if (opcode == 44) {
            int count = (short) buffer.readUnsignedShort();
        } else if (opcode == 45) {
            int size = (short) buffer.readUnsignedShort();
        } else if (opcode == 42) {
            int i = buffer.readUnsignedByte();
            byte[] aByteArray861 = new byte[i];
            for (int index = 0; i > index; index++)
                aByteArray861[index] = (byte) buffer.readByte();
        } else if (opcode == 60) {
            int i = buffer.readUnsignedByte();
            int[] dialogueModels = new int[i];
            for (int index = 0; index < i; index++)
                dialogueModels[index] = buffer.readBigSmart();
        } else if (opcode == 93) {
            boolean drawMinimapDot = false;
        } else if (opcode == 95) {
            combat = buffer.readUnsignedShort();
        } else if (opcode == 97) {
            int scaleXY = buffer.readUnsignedShort();
        } else if (opcode == 98) {
            int scaleZ = buffer.readUnsignedShort();
        } else if (opcode == 99) {
            boolean priorityRender = true;
        } else if (opcode == 100) {
            int lightModifier = buffer.readByte();
        } else if (opcode == 101) {
            int shadowModifier = buffer.readByte() * 5;
        } else if (opcode == 102) {
            int headIcon = buffer.readUnsignedShort();
        } else if (opcode == 103) {
            rotation = buffer.readUnsignedShort();
        } else if (opcode == 106 || opcode == 118) {
            int varbit = buffer.readUnsignedShort();
            if (varbit == 65535)
                varbit = -1;
            int varp = buffer.readUnsignedShort();
            if (varp == 65535)
                varp = -1;
            int i = -1;
            if (opcode == 118) {
                i = buffer.readUnsignedShort();
                if (i == 65535)
                    i = -1;
            }
            int length = buffer.readUnsignedByte();
            int[] morphisms = new int[length + 2];
            for (int index = 0; length >= index; index++) {
                morphisms[index] = buffer.readUnsignedShort();
                if (morphisms[index] == 65535)
                    morphisms[index] = -1;
            }
            morphisms[length + 1] = i;
        } else if (opcode == 107) {
            boolean clickable = false;
        } else if (opcode == 109) {
            boolean aBoolean852 = false;
        } else if (opcode == 111) {
            boolean aBoolean857 = false;
        } else if (opcode == 113) {
            short aShort862 = (short) buffer.readUnsignedShort();
            short aShort894 = (short) buffer.readUnsignedShort();
        } else if (opcode == 114) {
            byte aByte851 = (byte) buffer.readByte();
            byte aByte854 = (byte) buffer.readByte();
        } else if (opcode == 115) {
            buffer.readUnsignedByte();
            buffer.readUnsignedByte();
        } else if (opcode == 119) {
            walkMask = (byte) buffer.readByte();
        } else if (opcode == 121) {
            int[][] anIntArrayArray840 = new int[modelIds.length][];
            int i = buffer.readUnsignedByte();
            for (int loop = 0; loop < i; loop++) {
                int index = buffer.readUnsignedByte();
                int[] is = anIntArrayArray840[index] = new int[3];
                is[0] = buffer.readByte();
                is[1] = buffer.readByte();
                is[2] = buffer.readByte();
            }
        } else if (opcode == 122) {
            int anInt836 = buffer.readBigSmart();
        } else if (opcode == 123) {
            int anInt846 = buffer.readUnsignedShort();
        } else if (opcode == 125) {
            respawnDirection = (byte) buffer.readByte();
        } else if (opcode == 127) {
            int renderEmote = buffer.readUnsignedShort();
        } else if (opcode == 128) {
            buffer.readUnsignedByte();
        } else if (opcode == 134) {
            int anInt876 = buffer.readUnsignedShort();
            if (anInt876 == 65535)
                anInt876 = -1;
            int anInt842 = buffer.readUnsignedShort();
            if (anInt842 == 65535)
                anInt842 = -1;
            int anInt884 = buffer.readUnsignedShort();
            if (anInt884 == 65535)
                anInt884 = -1;
            int anInt871 = buffer.readUnsignedShort();
            if (anInt871 == 65535)
                anInt871 = -1;
            int anInt875 = buffer.readUnsignedByte();
        } else if (opcode == 135) {
            int anInt833 = buffer.readUnsignedByte();
            int anInt874 = buffer.readUnsignedShort();
        } else if (opcode == 136) {
            int anInt837 = buffer.readUnsignedByte();
            int anInt889 = buffer.readUnsignedShort();
        } else if (opcode == 137) {
            int anInt872 = buffer.readUnsignedShort();
        } else if (opcode == 138) {
            int anInt901 = buffer.readBigSmart();
        } else if (opcode == 139) {
            int anInt879 = buffer.readBigSmart();
        } else if (opcode == 140) {
            int anInt850 = buffer.readUnsignedByte();
        } else if (opcode == 141) {
            boolean aBoolean849 = true;
        } else if (opcode == 142) {
            int anInt870 = buffer.readUnsignedShort();
        } else if (opcode == 143) {
            boolean aBoolean856 = true;
        } else if (opcode >= 150 && opcode < 155) {
            options[opcode - 150] = buffer.readString();
            if (options[opcode - 150].equalsIgnoreCase("Hidden"))
                options[opcode - 150] = null;
        } else if (opcode == 160) {
            int i = buffer.readUnsignedByte();
            int[] anIntArray885 = new int[i];
            for (int i_58_ = 0; i > i_58_; i_58_++)
                anIntArray885[i_58_] = buffer.readUnsignedShort();
        } else if (opcode == 155) {
            int aByte821 = buffer.readByte();
            int aByte824 = buffer.readByte();
            int aByte843 = buffer.readByte();
            int aByte855 = buffer.readByte();
        } else if (opcode == 158) {
            byte aByte833 = (byte) 1;
        } else if (opcode == 159) {
            byte aByte833 = (byte) 0;
        } else if (opcode == 162) {
            boolean aBoolean3190 = true;
        } else if (opcode == 163) {
            int anInt864 = buffer.readUnsignedByte();
        } else if (opcode == 164) {
            int anInt848 = buffer.readUnsignedShort();
            int anInt837 = buffer.readUnsignedShort();
        } else if (opcode == 165) {
            int anInt847 = buffer.readUnsignedByte();
        } else if (opcode == 168) {
            int anInt828 = buffer.readUnsignedByte();
        } else if (opcode >= 170 && opcode < 176) {//More options?
            int i_44_ = (short) buffer.readUnsignedShort();
        } else if (opcode == 249) {
            int i = buffer.readUnsignedByte();
            if (clientScriptData == null)
                clientScriptData = new HashMap<>(i);
            for (int i_60_ = 0; i > i_60_; i_60_++) {
                boolean stringInstance = buffer.readUnsignedByte() == 1;
                int key = buffer.read24BitInt();
                Object value;
                if (stringInstance)
                    value = buffer.readString();
                else
                    value = buffer.readInt();
                clientScriptData.put(key, value);
            }
        }
    }
}
