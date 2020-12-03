package com.fury.cache.def.item;

import com.fileserver.cache.impl.CacheArchive;
import com.fury.cache.ByteBuffer;
import com.fury.cache.Revision;

import java.util.Arrays;
import java.util.HashMap;

public class ItemDefinition830 extends ItemDefinition {
    public static ItemDefinition830[] cache;
    protected static ByteBuffer buffer;
    protected static int[] indices;
    public static int total;

    public ItemDefinition830(int id) {
        super(id);
        revision = Revision.RS3;
    }

    public void load(CacheArchive archive, String name) {
        buffer = new ByteBuffer(archive.get(name + ".dat"));
        ByteBuffer index = new ByteBuffer(archive.get(name + ".idx"));

        total = index.getUnsignedShort();
        indices = getIndices(index, total);

        cache = new ItemDefinition830[total];
    }

    public ItemDefinition830 forId(int id) {
        ItemDefinition830 definition = id < 0 || id >= cache.length ? null : cache[id];
        if(definition == null) {
            definition = new ItemDefinition830(id);

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
            modelId = buffer.readBigSmart();
        } else if (opcode == 2) {
            name = buffer.getNewString();
        } else if (opcode == 4) {
            int spriteScale = buffer.readUnsignedShort();
        } else if (opcode == 5) {
            int spritePitch = buffer.readUnsignedShort();
        } else if (opcode == 6) {
            int spriteCameraRoll = buffer.readUnsignedShort();
        } else if (opcode == 7) {
            int spriteTranslateX = buffer.readUnsignedShort();
            if (spriteTranslateX > 32767)
                spriteTranslateX -= 65536;
        } else if (opcode == 8) {
            int spriteTranslateY = buffer.readUnsignedShort();
            if (spriteTranslateY > 32767)
                spriteTranslateY -= 65536;
        } else if (opcode == 11) {
            stackable = true;
        } else if (opcode == 12) {
            value = buffer.readInt();
        } else if (opcode == 13) {
            equipSlot = buffer.readUnsignedByte();
        } else if (opcode == 14) {
            equipType = buffer.readUnsignedByte();
        } else if (opcode == 16) {
            members = true;
        } else if (opcode == 23) {
            primaryMaleModel = buffer.readBigSmart();
        } else if (opcode == 24) {
            secondaryMaleModel = buffer.readBigSmart();
        } else if (opcode == 25) {
            primaryFemaleModel = buffer.readBigSmart();
        } else if (opcode == 26) {
            secondaryFemaleModel = buffer.readBigSmart();
        } else if (opcode == 27) {
            buffer.readUnsignedByte();
        } else if (opcode >= 30 && opcode < 35) {
            if(groundOptions == null)
                groundOptions = new String[5];
            groundOptions[opcode - 30] = buffer.getNewString();
        } else if (opcode >= 35 && opcode < 40) {
            if(options == null)
                options = new String[5];
            options[opcode - 35] = buffer.getNewString();
        } else if (opcode == 40) {
            int length = buffer.readUnsignedByte();
            int[] originalColours = new int[length];
            int[] replacementColours = new int[length];
            for (int index = 0; index < length; ++index) {
                originalColours[index] = buffer.readUnsignedShort();
                replacementColours[index] = buffer.readUnsignedShort();
            }
        } else if (opcode == 41) {
            int length = buffer.readUnsignedByte();
            short[] originalTextureColours = new short[length];
            short[] replacementTextureColours = new short[length];
            for (int index = 0; index < length; ++index) {
                originalTextureColours[index] = (short) buffer.readUnsignedShort();
                replacementTextureColours[index] = (short) buffer.readUnsignedShort();
            }
        } else if (opcode == 42) {
            int length = buffer.readUnsignedByte();
            byte[] unknownArray1 = new byte[length];
            for (int index = 0; index < length; ++index)
                unknownArray1[index] = (byte) buffer.readByte();
        } else if(opcode == 43) {
            int anInt6713 = buffer.readUnsignedShort();
        } else if(opcode == 44) {
            int i_7_ = buffer.readUnsignedShort();
        } else if (opcode == 45) {
            int i_12_ = buffer.readUnsignedShort();
        } else if (opcode == 65) {
            boolean unnoted = true;
        } else if (opcode == 78) {
            tertiaryMaleEquipmentModel = buffer.readBigSmart();
        } else if (opcode == 79) {
            tertiaryFemaleEquipmentModel = buffer.readBigSmart();
        } else if (opcode == 90) {
            primaryMaleDialogueHead = buffer.readBigSmart();
        } else if (opcode == 91) {
            primaryFemaleDialogueHead = buffer.readBigSmart();
        } else if (opcode == 92) {
            secondaryMaleDialogueHead = buffer.readBigSmart();
        } else if (opcode == 93) {
            secondaryFemaleDialogueHead = buffer.readBigSmart();
        } else if (opcode == 94) {
            int anInt7887 = buffer.readUnsignedShort();
        } else if (opcode == 95) {
            int spriteCameraYaw = buffer.readUnsignedShort();
        } else if (opcode == 96) {
            int unknownInt6 = buffer.readUnsignedByte();
        } else if (opcode == 97) {
            noteId = buffer.readUnsignedShort();
        } else if (opcode == 98) {
            notedTemplateId = buffer.readUnsignedShort();
        } else if (opcode >= 100 && opcode < 110) {
            if (stackIds == null) {
                stackIds = new int[10];
                stackAmounts = new int[10];
                stackRevisions = new Revision[10];
            }
            stackIds[opcode - 100] = buffer.readUnsignedShort();
            stackAmounts[opcode - 100] = buffer.readUnsignedShort();
            stackRevisions[opcode - 100] = Revision.RS3;
        } else if (opcode == 110) {
            int groundScaleX = buffer.readUnsignedShort();
        } else if (opcode == 111) {
            int groundScaleY = buffer.readUnsignedShort();
        } else if (opcode == 112) {
            int groundScaleZ = buffer.readUnsignedShort();
        } else if (opcode == 113) {
            int ambience = buffer.readByte();
        } else if (opcode == 114) {
            int diffusion = buffer.readByte() * 5;
        } else if (opcode == 115) {
            team = buffer.readUnsignedByte();
        } else if (opcode == 121) {
            lendId = buffer.readUnsignedShort();
        } else if (opcode == 122) {
            lendTemplateId = buffer.readUnsignedShort();
        } else if (opcode == 125) {
            int maleWieldX = (byte) (buffer.readByte() << 2);
            int maleWieldY = (byte) (buffer.readByte() << 2);
            int maleWieldZ = (byte) (buffer.readByte() << 2);
        } else if (opcode == 126) {
            int femaleWieldX = (byte) (buffer.readByte() << 2);
            int femaleWieldY = (byte) (buffer.readByte() << 2);
            int femaleWieldZ = (byte) (buffer.readByte() << 2);
        } else if (opcode == 127 || opcode == 128 || opcode == 129 || opcode == 130) {
            buffer.readUnsignedByte();
            buffer.readUnsignedShort();
        } else if (opcode == 132) {
            int length = buffer.readUnsignedByte();
            int[] unknownArray2 = new int[length];

            for (int index = 0; index < length; ++index)
                unknownArray2[index] = buffer.readUnsignedShort();
        } else if (opcode == 134) {
            buffer.readUnsignedByte();
        } else if (opcode == 139) {
            bindId = buffer.readUnsignedShort();
        } else if (opcode == 140) {
            bindTemplateId = buffer.readUnsignedShort();
        } else if (opcode >= 142 && opcode < 147) {
            int[] unknownArray4 = null;
            if (unknownArray4 == null) {
                unknownArray4 = new int[6];
                Arrays.fill(unknownArray4, -1);
            }
            unknownArray4[opcode - 142] = buffer.readUnsignedShort();
        } else if (opcode >= 150 && opcode < 155) {
            int[] unknownArray5 = null;
            if (null == unknownArray5) {
                unknownArray5 = new int[5];
                Arrays.fill(unknownArray5, -1);
            }
            unknownArray5[opcode - 150] = buffer.readUnsignedShort();
        } else if (opcode == 156) { //new
        } else if (157 == opcode) {//new
            boolean aBool7955 = true;
        } else if (161 == opcode) {//new
            int anInt7904 = buffer.readUnsignedShort();
        } else if (162 == opcode) {//new
            int anInt7923 = buffer.readUnsignedShort();
        } else if (163 == opcode) {//new
            int anInt7939 = buffer.readUnsignedShort();
        } else if (164 == opcode) {//new coinshare shard
            String aString7902 = buffer.getNewString();
        } else if (opcode == 165) {//new
            //stackable = 2;
        } else if (opcode == 242) {
            int oldInvModel = buffer.readBigSmart();
//            int oldInvZoom = buffer.readBigSmart();
        } else if (opcode == 243) {
            int oldMaleEquipModelId3 = buffer.readBigSmart();
        } else if (opcode == 244) {
            int oldFemaleEquipModelId3 = buffer.readBigSmart();
        } else if (opcode == 245) {
            int oldMaleEquipModelId2 = buffer.readBigSmart();
        } else if (opcode == 246) {
            int oldFemaleEquipModelId2 = buffer.readBigSmart();
        } else if (opcode == 247) {
            int oldMaleEquipModelId1 = buffer.readBigSmart();
        } else if (opcode == 248) {
            int oldFemaleEquipModelId1 = buffer.readBigSmart();
        } else if (opcode == 250) {
            int oldEquipType = buffer.readUnsignedByte();
        } else if (opcode == 251) {
            int length = buffer.readUnsignedByte();
            int[] oldoriginalModelColors = new int[length];
            int[] oldmodifiedModelColors = new int[length];
            for (int index = 0; index < length; index++) {
                oldoriginalModelColors[index] = buffer.readUnsignedShort();
                oldmodifiedModelColors[index] = buffer.readUnsignedShort();
            }
        } else if (opcode == 252) {
            int length = buffer.readUnsignedByte();
            short[] oldoriginalTextureColors = new short[length];
            short[] oldmodifiedTextureColors = new short[length];
            for (int index = 0; index < length; index++) {
                oldoriginalTextureColors[index] = (short) buffer.readUnsignedShort();
                oldmodifiedTextureColors[index] = (short) buffer.readUnsignedShort();
            }
        } else if (opcode == 253) {
            int oldModelRotation1 = buffer.readUnsignedShort();
            int oldModelRotation2 = buffer.readUnsignedShort();
            int oldModelOffset1 = buffer.readUnsignedShort();
            int oldModelOffset2 = buffer.readUnsignedShort();
        } else if (opcode == 249) {
            int length = buffer.readUnsignedByte();
            if (parameters == null)
                parameters = new HashMap(length);

            for (int index = 0; index < length; ++index) {
                boolean stringInstance = buffer.readUnsignedByte() == 1;
                int key = buffer.readUnsignedTribyte();
                Object value = stringInstance ? buffer.getNewString() : buffer.readInt();
                parameters.put(key, value);
            }
        } else {
            throw new RuntimeException("MISSING OPCODE " + opcode + " FOR ITEM " + id);
        }
    }
}
