package com.fury.cache.def.object;


import com.fileserver.cache.impl.CacheArchive;
import com.fury.cache.ByteBuffer;
import com.fury.cache.Revision;

import java.util.HashMap;

public class ObjectDefinition742 extends ObjectDefinition {
    public static ObjectDefinition742[] cache;
    protected static ByteBuffer buffer;
    protected static int[] indices;
    public static int total;

    public ObjectDefinition742(int id) {
        super(id);
        revision = Revision.PRE_RS3;
    }

    public void load(CacheArchive archive, String name) {
        buffer = new ByteBuffer(archive.get(name + ".dat"));
        ByteBuffer index = new ByteBuffer(archive.get(name + ".idx"));

        total = index.getInt();
        indices = getIndices(index, total);

        cache = new ObjectDefinition742[total];
    }

    public ObjectDefinition742 forId(int id) {
        ObjectDefinition742 definition = id < 0 || id >= cache.length ? null : cache[id];
        if(definition == null) {
            definition = new ObjectDefinition742(id);

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
        if (opcode == 1 || opcode == 5) {
            if (opcode == 5 && true)
                skip(buffer);
            int length = buffer.readUnsignedByte();
            modelIds = new int[length][];
            modelTypes = new int[length];
            for (int index = 0; index < length; index++) {
                modelTypes[index] = (byte) buffer.readByte();
                int length2 = buffer.readUnsignedByte();
                modelIds[index] = new int[length2];
                for (int index2 = 0; length2 > index2; index2++)
                    modelIds[index][index2] = buffer.readBigSmart();
            }
            if (opcode == 5 && !true)
                skip(buffer);
        } else if (opcode == 2) {
            name = buffer.readString();
        } else if (opcode == 14) {
            sizeX = buffer.readUnsignedByte();
        } else if (opcode == 15) {
            sizeY = buffer.readUnsignedByte();
        } else if (opcode == 17) {
            solid = 0;
            projectileClipped = false;
        } else if (opcode == 18) {
            impenetrable = false;
            projectileClipped = false;
        } else if (opcode == 19) {
            interactive = buffer.readUnsignedByte();
        } else if (opcode == 21) {
            contouredGround = true;
        } else if (opcode == 22) {
            delayShading = false;
        } else if (opcode == 23) {
            needsCulling = true;//Not sure which way these should be
        } else if (opcode == 24) {
            int n = buffer.readBigSmart();
            if (n != 65535) {
                animationArray = new int[] { n };
                animationId = n;
            }
        } else if (opcode == 27) {
            solid = 1;
        } else if (opcode == 28) {
            offsetMultiplier = buffer.readUnsignedByte();
        } else if (opcode == 29) {
            brightness = (byte) buffer.readByte();
        } else if (opcode == 39) {
            contrast = (byte) buffer.readByte();
        } else if (opcode >= 30 && opcode < 35) {
            if (options == null)
                options = new String[5];
            options[opcode - 30] = buffer.readString();
            if (options[opcode - 30].equalsIgnoreCase("hidden"))
                options[opcode - 30] = null;
        } else if (opcode == 40) {
            int i_53_ = (buffer.readUnsignedByte());
            int[] replacementColours = new int[i_53_];
            int[] originalColours = new int[i_53_];
            for (int i_54_ = 0; i_53_ > i_54_; i_54_++) {
                originalColours[i_54_] = (short) (buffer.readUnsignedShort());
                replacementColours[i_54_] = (short) (buffer.readUnsignedShort());
            }
        } else if (opcode == 41) {
            int i_71_ = (buffer.readUnsignedByte());
            short[] modifiedModelTextures = new short[i_71_];
            short[] originalModelTextures = new short[i_71_];
            for (int i_72_ = 0; i_71_ > i_72_; i_72_++) {
                modifiedModelTextures[i_72_] = (short) (buffer.readUnsignedShort());
                originalModelTextures[i_72_] = (short) (buffer.readUnsignedShort());
            }
        } else if (opcode == 42) {
            int i_69_ = (buffer.readUnsignedByte());
            byte[] aByteArray3858 = (new byte[i_69_]);
            for (int i_70_ = 0; i_70_ < i_69_; i_70_++)
                aByteArray3858[i_70_] = (byte) (buffer.readByte());
        } else if (opcode == 62) {
            rotated = true;
        } else if (opcode == 64) {
            castsShadow = false;
        } else if (opcode == 65) {
            modelSizeX = buffer.readUnsignedShort();
        } else if (opcode == 66) {
            modelSizeH = buffer.readUnsignedShort();
        } else if (opcode == 67) {
            modelSizeY = buffer.readUnsignedShort();
        } else if (opcode == 69) {
            plane = buffer.readUnsignedByte();
        } else if (opcode == 70) {
            offsetX = buffer.readShort();
        } else if (opcode == 71) {
            offsetH = buffer.readShort();
        } else if (opcode == 72) {
            offsetY = buffer.readShort();
        } else if (opcode == 73) {
            obstructsGround = true;
        } else if (opcode == 74) {
            ignoreClipOnAlternativeRoute = true;
        } else if (opcode == 75) {
            supportItems = buffer.readUnsignedByte();
        } else if (opcode == 77 || opcode == 92) {
            varbitIndex = buffer.readUnsignedShort();
            if (varbitIndex == 65535)
                varbitIndex = -1;
            configId = buffer.readUnsignedShort();
            if (configId == 65535)
                configId = -1;
            int i_66_ = -1;
            if (opcode == 92) {
                i_66_ = buffer.readBigSmart();
                if (i_66_ == 65535)
                    i_66_ = -1;
            }
            int i_67_ = buffer.readUnsignedByte();
            configObjectIds = new int[i_67_ + 2];
            for (int i_68_ = 0; i_67_ >= i_68_; i_68_++) {
                configObjectIds[i_68_] = buffer.readBigSmart();
                if (configObjectIds[i_68_] == 65535)
                    configObjectIds[i_68_] = -1;
            }
            configObjectIds[i_67_ + 1] = i_66_;
        } else if (opcode == 78) {
            int anInt3860 = buffer.readUnsignedShort();
            int anInt3904 = buffer.readUnsignedByte();
        } else if (opcode == 79) {
            int anInt3900 = buffer.readUnsignedShort();
            int anInt3905 = buffer.readUnsignedShort();
            int anInt3904 = buffer.readUnsignedByte();
            int i_64_ = buffer.readUnsignedByte();
            int[] anIntArray3859 = new int[i_64_];
            for (int i_65_ = 0; i_65_ < i_64_; i_65_++)
                anIntArray3859[i_65_] = buffer.readUnsignedShort();
        } else if (opcode == 81) {
            byte aByte3912 = (byte) 2;
            int anInt3882 = 256 * buffer.readUnsignedByte();
        } else if (opcode == 82) {
            boolean aBoolean3891 = true;
        } else if (opcode == 88) {
            boolean aBoolean3853 = false;
        } else if (opcode == 89) {
            animateImmediately = false;
        } else if (opcode == 90) {
            boolean aBoolean3870 = true;
        } else if (opcode == 91) {
            boolean aBoolean3873 = true;
        } else if (opcode == 93) {
            byte aByte3912 = (byte) 3;
            int anInt3882 = buffer.readUnsignedShort();
        } else if (opcode == 94) {
            byte aByte3912 = (byte) 4;
        } else if (opcode == 95) {
            byte aByte3912 = (byte) 5;
            int anInt3882 = buffer.readShort();
        } else if (opcode == 96) {
            boolean aBoolean3924 = true;
        } else if (opcode == 97) {
            boolean aBoolean3866 = true;
        } else if (opcode == 98){
            boolean aBoolean3923 = true;
        } else if (opcode == 99) {
            int anInt3857 = buffer.readUnsignedByte();
            int anInt3835 = buffer.readUnsignedShort();
        } else if (opcode == 100) {
            int anInt3844 = buffer.readUnsignedByte();
            int anInt3913 = buffer.readUnsignedShort();
        } else if (opcode == 101){
            int anInt3850 = buffer.readUnsignedByte();
        } else if (opcode == 102){
            int mapSceneId = buffer.readUnsignedShort();
        } else if (opcode == 103){
            int thirdInt = 0;
        } else if (opcode == 104){
            int anInt3865 = buffer.readUnsignedByte();
        } else if (opcode == 105){
            boolean aBoolean3906 = true;
        } else if (opcode == 106) {
            int length = buffer.readUnsignedByte();
            animationArray = new int[length];
            int anIntArray3833[] = new int[length];
            for (int index = 0; index < length; index++) {
                anIntArray3833[index] = buffer.readBigSmart();
                int value = buffer.readUnsignedByte();
                animationArray[index] = value;
                animationId += value;
            }
        } else if (opcode == 107){
            int mapDefinitionId = buffer.readUnsignedShort();
        } else if (opcode >= 150 && opcode < 155) {
            if (options == null)
                options = new String[5];
            options[opcode - 150] = buffer.readString();
            if (options[opcode - 150].equalsIgnoreCase("hidden"))
                options[opcode - 150] = null;
        } else if (opcode == 160) {
            int i_62_ = buffer.readUnsignedByte();
            int[] anIntArray3908 = new int[i_62_];
            for (int i_63_ = 0; i_62_ > i_63_; i_63_++)
                anIntArray3908[i_63_] = buffer.readUnsignedShort();
        } else if (opcode == 162) {
            byte aByte3912 = (byte) 3;
            int anInt3882 = buffer.readInt();
        } else if (opcode == 163) {
            byte aByte3847 = (byte) buffer.readByte();
            byte aByte3849 = (byte) buffer.readByte();
            byte aByte3837 = (byte) buffer.readByte();
            byte aByte3914 = (byte) buffer.readByte();
        } else if (opcode == 164) {
            int anInt3834 = buffer.readShort();
        } else if (opcode == 165) {
            int anInt3875 = buffer.readShort();
        } else if (opcode == 166) {
            int anInt3877 = buffer.readShort();
        } else if (opcode == 167) {
            int anInt3921 = buffer.readUnsignedShort();
        } else if (opcode == 168) {
            boolean aBoolean3894 = true;
        } else if (opcode == 169) {
            boolean aBoolean3845 = true;
        } else if (opcode == 170) {
            int anInt3383 = buffer.readUnsignedSmart();
        } else if (opcode == 171) {
            int anInt3362 = buffer.readUnsignedSmart();
        } else if (opcode == 173) {
            int anInt3302 = buffer.readUnsignedShort();
            int anInt3336 = buffer.readUnsignedShort();
        } else if (opcode == 177) {
            boolean ub = true;
        } else if (opcode == 178){
            int db = buffer.readUnsignedByte();
        } else if (opcode == 189) {
            boolean bloom = true;
        } else if (opcode == 249) {
            int length = buffer.readUnsignedByte();
            if (parameters == null)
                parameters = new HashMap<>(length);
            for (int index = 0; index < length; index++) {
                boolean bool = buffer.readUnsignedByte() == 1;
                int id = buffer.read24BitInt();
                parameters.put(id, bool ? buffer.readString() : buffer.readInt());
            }
        }
    }
}
