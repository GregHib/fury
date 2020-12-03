package com.fury.game.cache;

import com.fileserver.cache.impl.CacheArchive;
import com.fury.cache.ByteBuffer;
import com.fury.cache.Revision;
import com.fury.cache.def.Loader;
import com.fury.cache.def.item.ItemDefinition;
import com.fury.cache.def.item.ItemDefinition659;
import com.fury.game.GameLoader;

import java.io.DataOutputStream;
import java.io.FileOutputStream;
import java.util.HashMap;

public class ItemPriceChanger {

    private static void load2() {
//        DataInputStream idx = new DataInputStream(new FileInputStream(directory + "items.idx").);
//        DataInputStream dat = new DataInputStream(new FileInputStream(directory + "items.dat"));
    }

    public static void main(String[] args) throws Exception {
        GameLoader.getCache().init();

        Loader.init();

        load();

        Revision revision = Revision.RS2;
        String directory = System.getProperty("user.dir") + "\\src\\main\\test\\com\\fury\\game\\cache\\";

        DataOutputStream idx = new DataOutputStream(new FileOutputStream(directory + "items.idx"));
        DataOutputStream dat = new DataOutputStream(new FileOutputStream(directory + "items.dat"));

        idx.writeShort(total);

        System.out.println("Total: " + total);

        int index = 0;
        for (int id = 0; id < total; id++) {
//            if(id < 10) {
                ItemDefinition659 definition = new ItemDefinition659(id);

                if(id >= indices.length || id < 0)
                    continue;

                ByteBuffer temp = new ByteBuffer(new byte[10000]);

                buffer.position = indices[id];
                readValueLoop(definition, buffer, temp);

                idx.writeShort(temp.position);
                dat.write(temp.buffer, 0, temp.position);
//            }
        }

        idx.close();
        dat.close();
    }

    private static void readValueLoop(ItemDefinition definition, ByteBuffer buffer, ByteBuffer out) {
        while (true) {
            int opcode = buffer.readUnsignedByte();
            out.putByte(opcode);
            if (opcode == 0)
                return;

            readValues(definition, buffer, opcode, out);
        }
    }

    private static void readValues(ItemDefinition definition, ByteBuffer buffer, int opcode, ByteBuffer out) {
        if (opcode == 1) {
            definition.modelId = buffer.readUnsignedShort();
            out.putShort(definition.modelId);
        } else if (opcode == 2) {
            definition.name = buffer.readString();
            out.putString(definition.name);
        } else if (opcode == 4) {
            int spriteScale = buffer.readUnsignedShort();
            out.putShort(spriteScale);
        } else if (opcode == 5) {
            int spritePitch = buffer.readUnsignedShort();
            out.putShort(spritePitch);
        } else if (opcode == 6) {
            int spriteCameraRoll = buffer.readUnsignedShort();
            out.putShort(spriteCameraRoll);
        } else if (opcode == 7) {
            int spriteTranslateX = buffer.readUnsignedShort();
            out.putShort(spriteTranslateX);
            if (spriteTranslateX > 32767)
                spriteTranslateX -= 65536;
        } else if (opcode == 8) {
            int spriteTranslateY = buffer.readUnsignedShort();
            out.putShort(spriteTranslateY);
            if (spriteTranslateY > 32767)
                spriteTranslateY -= 65536;
        } else if (opcode == 11) {
            definition.stackable = true;
        } else if (opcode == 12) {
            definition.value = buffer.getInt();
            out.putInt(definition.value);
        } else if (opcode == 16) {
            boolean members = true;
        } else if (opcode == 18) {
            int anInt1735 = buffer.readUnsignedShort();
            out.putShort(anInt1735);
        } else if (opcode == 23) {
            int primaryMaleModel = buffer.readUnsignedShort();
            out.putShort(primaryMaleModel);
        } else if (opcode == 24) {
            int secondaryMaleModel = buffer.readUnsignedShort();
            out.putShort(secondaryMaleModel);
        } else if (opcode == 25) {
            int primaryFemaleModel = buffer.readUnsignedShort();
            out.putShort(primaryFemaleModel);
        } else if (opcode == 26) {
            int secondaryFemaleModel = buffer.readUnsignedShort();
            out.putShort(secondaryFemaleModel);
        } else if (opcode >= 30 && opcode < 35) {
            if(definition.groundOptions == null)
                definition.groundOptions = new String[5];
            definition.groundOptions[opcode - 30] = buffer.readString();
            out.putString(definition.groundOptions[opcode - 30]);
        } else if (opcode >= 35 && opcode < 40) {
            if(definition.options == null)
                definition.options = new String[5];
            definition.options[opcode - 35] = buffer.readString();
            out.putString(definition.options[opcode - 35]);
        } else if (opcode == 40) {
            int size = buffer.readUnsignedByte();
            out.putByte(size);
            int[] originalColours = new int[size];
            int[] replacementColours = new int[size];
            for (int index = 0; index < size; index++) {
                originalColours[index] = buffer.readUnsignedShort();
                out.putShort(originalColours[index]);
                replacementColours[index] = buffer.readUnsignedShort();
                out.putShort(replacementColours[index]);
            }
        } else if (opcode == 41) {
            int size = buffer.readUnsignedByte();
            out.putByte(size);
            short[] originalTextureColours = new short[size];
            short[] replacementTextureColours = new short[size];
            for (int index = 0; index < size; index++) {
                originalTextureColours[index] = (short) buffer.readUnsignedShort();
                out.putShort(originalTextureColours[index]);
                replacementTextureColours[index] = (short) buffer.readUnsignedShort();
                out.putShort(replacementTextureColours[index]);
            }
        } else if (opcode == 42) {
            int size = buffer.readUnsignedByte();
            out.putByte(size);
            byte[] aByteArray1715 = new byte[size];
            for (int index = 0; index < size; index++) {
                aByteArray1715[index] = (byte) buffer.readByte();
                out.putByte(aByteArray1715[index]);
            }
        } else if (opcode == 65) {
            boolean unnoted = true;
        } else if (opcode == 78) {
            int tertiaryMaleEquipmentModel = buffer.readUnsignedShort();
            out.putShort(tertiaryMaleEquipmentModel);
        } else if (opcode == 79) {
            int tertiaryFemaleEquipmentModel = buffer.readUnsignedShort();
            out.putShort(tertiaryFemaleEquipmentModel);
        } else if (opcode == 90) {
            int primaryMaleDialogueHead = buffer.readUnsignedShort();
            out.putShort(primaryMaleDialogueHead);
        } else if (opcode == 91) {
            int primaryFemaleDialogueHead = buffer.readUnsignedShort();
            out.putShort(primaryFemaleDialogueHead);
        } else if (opcode == 92) {
            int secondaryMaleDialogueHead = buffer.readUnsignedShort();
            out.putShort(secondaryMaleDialogueHead);
        } else if (opcode == 93) {
            int secondaryFemaleDialogueHead = buffer.readUnsignedShort();
            out.putShort(secondaryFemaleDialogueHead);
        } else if (opcode == 95) {
            int spriteCameraYaw = buffer.readUnsignedShort();
            out.putShort(spriteCameraYaw);
        } else if (opcode == 96) {
            int anInt1729 = buffer.readUnsignedByte();
            out.putByte(anInt1729);
        } else if (opcode == 97) {
            definition.noteId = buffer.readUnsignedShort();
            out.putShort(definition.noteId);
        } else if (opcode == 98) {
            definition.notedTemplateId = buffer.readUnsignedShort();
            out.putShort(definition.notedTemplateId);
        } else if (opcode >= 100 && opcode < 110) {
            if (definition.stackIds == null) {
                definition.stackIds = new int[10];
                definition.stackAmounts = new int[10];
                definition.stackRevisions = new Revision[10];
            }
            definition.stackIds[opcode - 100] = buffer.readUnsignedShort();
            out.putShort(definition.stackIds[opcode - 100]);
            definition.stackAmounts[opcode - 100] = buffer.readUnsignedShort();
            out.putShort(definition.stackAmounts[opcode - 100]);
            definition.stackRevisions[opcode - 100] = Revision.RS2;
        } else if (opcode == 110) {
            int groundScaleX = buffer.readUnsignedShort();
            out.putShort(groundScaleX);
        } else if (opcode == 111) {
            int groundScaleY = buffer.readUnsignedShort();
            out.putShort(groundScaleY);
        } else if (opcode == 112) {
            int groundScaleZ = buffer.readUnsignedShort();
            out.putShort(groundScaleZ);
        } else if (opcode == 113) {
            int ambience = buffer.readByte();
            out.putByte(ambience);
        } else if (opcode == 114) {
            int diffusion = buffer.readByte() * 5;
            out.putByte(diffusion/5);
        } else if (opcode == 115) {
            definition.team = buffer.readUnsignedByte();
            out.putByte(definition.team);
        } else if (opcode == 121) {
            definition.lendId = buffer.readUnsignedShort();
            out.putShort(definition.lendId);
        } else if (opcode == 122) {
            definition.lendTemplateId = buffer.readUnsignedShort();
            out.putShort(definition.lendTemplateId);
        } else if (opcode == 125) {
            int maleWieldX = (byte) (buffer.readByte() << 2);
            int maleWieldY = (byte) (buffer.readByte() << 2);
            int maleWieldZ = (byte) (buffer.readByte() << 2);
            out.putByte(maleWieldX >> 2);
            out.putByte(maleWieldY >> 2);
            out.putByte(maleWieldZ >> 2);
        } else if (opcode == 126) {
            int femaleWieldX = (byte) (buffer.readByte() << 2);
            int femaleWieldY = (byte) (buffer.readByte() << 2);
            int femaleWieldZ = (byte) (buffer.readByte() << 2);
            out.putByte(femaleWieldX >> 2);
            out.putByte(femaleWieldY >> 2);
            out.putByte(femaleWieldZ >> 2);
        } else if (opcode == 127) {
            int anInt1702 = buffer.readUnsignedByte();
            int anInt1723 = buffer.readUnsignedShort();
            out.putByte(anInt1702);
            out.putShort(anInt1723);
        } else if (opcode == 128) {
            int anInt1703 = buffer.readUnsignedByte();
            int anInt1697 = buffer.readUnsignedShort();
            out.putByte(anInt1703);
            out.putShort(anInt1697);
        } else if (opcode == 129) {
            int anInt1772 = buffer.readUnsignedByte();
            int anInt1759 = buffer.readUnsignedShort();
            out.putByte(anInt1772);
            out.putShort(anInt1759);
        } else if (opcode == 130) {
            int anInt1737 = buffer.readUnsignedByte();
            int anInt1708 = buffer.readUnsignedShort();
            out.putByte(anInt1737);
            out.putShort(anInt1708);
        } else if (opcode == 132) {
            int size = buffer.readUnsignedByte();
            out.putByte(size);
            int[] anIntArray1711 = new int[size];
            for (int i = 0; size > i; i++) {
                anIntArray1711[i] = buffer.readUnsignedShort();
                out.putShort(anIntArray1711[i]);
            }
        } else if (opcode == 134) {
            int anInt1769 = buffer.readUnsignedByte();
            out.putByte(anInt1769);
        } else if (opcode == 139) {
            definition.bindId = buffer.readUnsignedShort();
            out.putShort(definition.bindId);
        } else if (opcode == 140) {
            definition.bindTemplateId = buffer.readUnsignedShort();
            out.putShort(definition.bindTemplateId);
        } else if (opcode == 249) {
            int length = buffer.readUnsignedByte();
            out.putInt(length);
            if (definition.parameters == null)
                definition.parameters = new HashMap<>(length);
            for (int index = 0; index < length; index++) {
                boolean bool = buffer.readUnsignedByte() == 1;
                out.putByte(bool ? 1 : 0);
                int value = buffer.read24BitInt();
                out.put24BitInt(value);
                definition.parameters.put(value, bool ? buffer.readString() : buffer.readInt());
                if(bool) {
                    out.putString((String) definition.parameters.get(value));
                } else {
                    out.writeInt((int) definition.parameters.get(value));
                }
            }
        }
    }
    public static ItemDefinition659[] cache;
    protected static ByteBuffer buffer;
    protected static int[] indices;
    public static int total;

    private static void load() {
        String name = "item659";
        CacheArchive archive = GameLoader.getCache().getArchive(2);
        buffer = new ByteBuffer(archive.get(name + ".dat"));
        ByteBuffer index = new ByteBuffer(archive.get(name + ".idx"));

        total = index.getUnsignedShort();
        int offset = 0;
        indices = new int[total];
        for (int id = 0; id < total; id++) {
            int length = index.getUnsignedShort();
            indices[id] = offset;
            offset += length;
        }

        cache = new ItemDefinition659[total];

        System.out.println("Load complete");
    }

    private static void write() {

    }
}
