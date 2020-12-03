package com.fury.cache.def.item;

import com.fileserver.cache.impl.CacheArchive;
import com.fury.cache.ByteBuffer;
import com.fury.cache.Revision;

import java.util.HashMap;

public class ItemDefinition659 extends ItemDefinition {
    public static ItemDefinition659[] cache;
    protected static ByteBuffer buffer;
    protected static int[] indices;
    public static int total;

    public ItemDefinition659(int id) {
        super(id);
        revision = Revision.RS2;
    }

    public void load(CacheArchive archive, String name) {
        buffer = new ByteBuffer(archive.get(name + ".dat"));
        ByteBuffer index = new ByteBuffer(archive.get(name + ".idx"));

        total = index.getUnsignedShort();
        indices = getIndices(index, total);

        cache = new ItemDefinition659[total];
    }

    public ItemDefinition659 forId(int id) {
        ItemDefinition659 definition = id < 0 || id >= cache.length ? null : cache[id];
        if(definition == null) {
            definition = new ItemDefinition659(id);

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
            modelId = buffer.readUnsignedShort();
        } else if (opcode == 2) {
            name = buffer.readString();
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
            value = buffer.getInt();
        } else if (opcode == 16) {
            boolean members = true;
        } else if (opcode == 18) {
            int anInt1735 = buffer.readUnsignedShort();
        } else if (opcode == 23) {
            int primaryMaleModel = buffer.readUnsignedShort();
        } else if (opcode == 24) {
            int secondaryMaleModel = buffer.readUnsignedShort();
        } else if (opcode == 25) {
            int primaryFemaleModel = buffer.readUnsignedShort();
        } else if (opcode == 26) {
            int secondaryFemaleModel = buffer.readUnsignedShort();
        } else if (opcode >= 30 && opcode < 35) {
            if(groundOptions == null)
                groundOptions = new String[5];
            groundOptions[opcode - 30] = buffer.readString();
        } else if (opcode >= 35 && opcode < 40) {
            if(options == null)
                options = new String[5];
            options[opcode - 35] = buffer.readString();
        } else if (opcode == 40) {
            int size = buffer.readUnsignedByte();
            int[] originalColours = new int[size];
            int[] replacementColours = new int[size];
            for (int index = 0; index < size; index++) {
                originalColours[index] = buffer.readUnsignedShort();
                replacementColours[index] = buffer.readUnsignedShort();
            }
        } else if (opcode == 41) {
            int size = buffer.readUnsignedByte();
            short[] originalTextureColours = new short[size];
            short[] replacementTextureColours = new short[size];
            for (int index = 0; index < size; index++) {
                originalTextureColours[index] = (short) buffer.readUnsignedShort();
                replacementTextureColours[index] = (short) buffer.readUnsignedShort();
            }
        } else if (opcode == 42) {
            int size = buffer.readUnsignedByte();
            byte[] aByteArray1715 = new byte[size];
            for (int index = 0; index < size; index++)
                aByteArray1715[index] = (byte) buffer.readByte();
        } else if (opcode == 65) {
            boolean unnoted = true;
        } else if (opcode == 78) {
            int tertiaryMaleEquipmentModel = buffer.readUnsignedShort();
        } else if (opcode == 79) {
            int tertiaryFemaleEquipmentModel = buffer.readUnsignedShort();
        } else if (opcode == 90) {
            int primaryMaleDialogueHead = buffer.readUnsignedShort();
        } else if (opcode == 91) {
            int primaryFemaleDialogueHead = buffer.readUnsignedShort();
        } else if (opcode == 92) {
            int secondaryMaleDialogueHead = buffer.readUnsignedShort();
        } else if (opcode == 93) {
            int secondaryFemaleDialogueHead = buffer.readUnsignedShort();
        } else if (opcode == 95) {
            int spriteCameraYaw = buffer.readUnsignedShort();
        } else if (opcode == 96) {
            int anInt1729 = buffer.readUnsignedByte();
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
            stackRevisions[opcode - 100] = Revision.RS2;
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
        } else if (opcode == 127) {
            int anInt1702 = buffer.readUnsignedByte();
            int anInt1723 = buffer.readUnsignedShort();
        } else if (opcode == 128) {
            int anInt1703 = buffer.readUnsignedByte();
            int anInt1697 = buffer.readUnsignedShort();
        } else if (opcode == 129) {
            int anInt1772 = buffer.readUnsignedByte();
            int anInt1759 = buffer.readUnsignedShort();
        } else if (opcode == 130) {
            int anInt1737 = buffer.readUnsignedByte();
            int anInt1708 = buffer.readUnsignedShort();
        } else if (opcode == 132) {
            int size = buffer.readUnsignedByte();
            int[] anIntArray1711 = new int[size];
            for (int i_26_ = 0; size > i_26_; i_26_++)
                anIntArray1711[i_26_] = buffer.readUnsignedShort();
        } else if (opcode == 134) {
            int anInt1769 = buffer.readUnsignedByte();
        } else if (opcode == 139) {
            bindId = buffer.readUnsignedShort();
        } else if (opcode == 140) {
            bindTemplateId = buffer.readUnsignedShort();
        } else if (opcode == 249) {
            int length = buffer.readUnsignedByte();
            if (parameters == null)
                parameters = new HashMap<>(length);
            for (int index = 0; index < length; index++) {
                boolean bool = buffer.readUnsignedByte() == 1;
                int value = buffer.read24BitInt();
                parameters.put(value, bool ? buffer.readString() : buffer.readInt());
            }
        }
    }
}
