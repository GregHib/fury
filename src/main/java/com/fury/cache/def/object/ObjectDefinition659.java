package com.fury.cache.def.object;

import com.fileserver.cache.impl.CacheArchive;
import com.fury.cache.ByteBuffer;
import com.fury.cache.Revision;

import java.util.HashMap;

public class ObjectDefinition659 extends ObjectDefinition {
    public static ObjectDefinition659[] cache;
    protected static ByteBuffer buffer;
    protected static int[] indices;
    public static int total;


    public ObjectDefinition659(int id) {
        super(id);
        revision = Revision.RS2;
    }

    public void load(CacheArchive archive, String name) {
        buffer = new ByteBuffer(archive.get(name + ".dat"));
        ByteBuffer index = new ByteBuffer(archive.get(name + ".idx"));

        total = index.getUnsignedShort();
        indices = getIndices(index, total);

        cache = new ObjectDefinition659[total];
    }

    public ObjectDefinition659 forId(int id) {
        ObjectDefinition659 definition = id < 0 || id >= cache.length ? null : cache[id];
        if(definition == null) {
            definition = new ObjectDefinition659(id);

            if(id >= indices.length || id < 0)
                return definition;

            //Dungeoneering exit fixes
            switch (id) {
                case 49695:
                    id = 51608;
                    break;
                case 49697:
                case 49699:
                    id = 52153;
                    break;
                case 49696:
                    id = 51156;
                    break;
                case 49698:
                    id = 50604;
                    break;
                case 49700:
                    id = 51704;
                    break;
            }

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
                    modelIds[index][index2] = buffer.readUnsignedShort();
            }
            if (opcode == 5 && !true)
                skip(buffer);
        } else if(opcode == 2) {
            name = buffer.getNewString();
        } else if (opcode == 5) {
            int length = buffer.getUByte();
            modelTypes = new int[length];
            for (int index = 0; length > index; index++)
                modelTypes[index] = buffer.getUByte();
        } else if(opcode == 14) {
            sizeX = buffer.getUByte();
        } else if(opcode == 15) {
            sizeY = buffer.getUByte();
        } else if(opcode == 17) {
            solid = 0;
            projectileClipped = false;
        } else if(opcode == 18) {
            projectileClipped = false;
            impenetrable = false;
        } else if (opcode == 19) {
            interactive = buffer.getUByte();
        } else if (opcode == 21) {
            contouredGround = true;
        } else if(opcode == 22) {
            delayShading = false;
        } else if(opcode == 23) {
            needsCulling = false;//True but needs fixing?
        } else if(opcode == 24) {
            animationId = buffer.getUnsignedShort();
            if (animationId == 65535)
                animationId = -1;
        } else if(opcode == 27) {
            solid = 1;
        } else if(opcode == 28) {
            offsetMultiplier = buffer.getUByte();
        } else if(opcode == 29) {
            brightness = buffer.getByte();
        } else if(opcode >= 30 && opcode < 35) {
            if(options == null)
                options = new String[5];
            options[opcode - 30] = buffer.getNewString();
            if (options[opcode - 30].equalsIgnoreCase("hidden"))
                options[opcode - 30] = null;
        } else if(opcode == 39) {
            contrast = buffer.getByte();
        } else if(opcode == 40) {
            int size = buffer.getUnsignedByte();
            int[] originalColours = new int[size];
            int[] replacementColours = new int[size];
            for (int index = 0; size > index; index++) {
                originalColours[index] = (short) buffer.getUnsignedShort();
                replacementColours[index] = (short) buffer.getUnsignedShort();
            }
        } else if(opcode == 41) {
            int size = (buffer.getUByte());
            short[] originalModelTextures = new short[size];
            short[] modifiedModelTextures = new short[size];
            for (int index = 0; size > index; index++) {
                originalModelTextures[index] = (short) buffer.getUnsignedShort();
                modifiedModelTextures[index] = (short) buffer.getUnsignedShort();
            }
        } else if(opcode == 42) {
            int i_69_ = (buffer.getUByte());
            byte[] aByteArray3858 = (new byte[i_69_]);
            for (int i_70_ = 0; i_70_ < i_69_; i_70_++)
                aByteArray3858[i_70_] = (byte) (buffer.getByte());
        } else if (opcode == 60) {
            int minimapFunction = buffer.getUShort();
        } else if(opcode == 62) {
            rotated = true;
        } else if(opcode == 64) {
            castsShadow = false;
        } else if(opcode == 65) {
            modelSizeX = buffer.getUnsignedShort();
        } else if(opcode == 66) {
            modelSizeH = buffer.getUnsignedShort();
        } else if(opcode == 67) {
            modelSizeY = buffer.getUnsignedShort();
        } else if (opcode == 68) {
            int mapScene = buffer.getUnsignedShort();
        } else if(opcode == 69) {
            plane = buffer.getUByte();
        } else if(opcode == 70) {
            offsetX = buffer.getShort();
        } else if(opcode == 71) {
            offsetH = buffer.getShort();
        } else if(opcode == 72) {
            offsetY = buffer.getShort();
        } else if (opcode == 73) {
            obstructsGround = true;
        } else if (opcode == 74) {
            ignoreClipOnAlternativeRoute = true;
        } else if(opcode == 75) {
            supportItems = buffer.getUByte();
        } else if(opcode == 77 || opcode == 92) {
            varbitIndex = buffer.getUShort();
            if (varbitIndex == 65535)
                varbitIndex = -1;
            configId = buffer.getUShort();
            if (configId == 65535)
                configId = -1;
            int value = -1;
            if (opcode == 92) {
                value = buffer.getUShort();
                if (value == 0xffff)
                    value = -1;
            }
            int length = buffer.getUByte();
            configObjectIds = new int[length + 2];
            for (int index = 0; length >= index; index++) {
                configObjectIds[index] = buffer.getUShort();
                if (configObjectIds[index] == 65535)
                    configObjectIds[index] = -1;
            }
            configObjectIds[length + 1] = value;
        } else if(opcode == 78) {
            int anInt3860 = buffer.getUShort();
            int anInt3904 = buffer.getUByte();
        } else if(opcode == 79) {
            int anInt3900 = buffer.getUShort();
            int anInt3905 = buffer.getUShort();
            int anInt3904 = buffer.getUByte();
            int i_64_ = buffer.getUByte();
            int[] anIntArray3859 = new int[i_64_];
            for (int i_65_ = 0; i_65_ < i_64_; i_65_++)
                anIntArray3859[i_65_] = buffer.getUShort();
        } else if(opcode == 81) {
            //contouredGround = true;//(byte) 2;
            int anInt3882 = 256 * buffer.getUByte();
        } else if(opcode == 82) {
            boolean aBoolean3891 = true;
        } else if(opcode == 88) {
            boolean aBoolean3853 = false;
        } else if(opcode == 89) {
            animateImmediately = false;
        } else if (opcode == 90) {
            boolean aBoolean3870 = true;
        } else if(opcode == 91) {
            boolean aBoolean3873 = true;
        } else if(opcode == 93) {
            //contouredGround = true;//(byte) 3;
            int anInt3882 = buffer.getUShort();
        } else if (opcode == 94) {
            //contouredGround = true;//(byte) 4;
        } else if(opcode == 95) {
            //contouredGround = true;//(byte) 5;
            int anInt3882 = buffer.getShort();
        } else if(opcode == 96) {
            boolean aBoolean3924 = true;
        } else if (opcode == 97) {
            boolean aBoolean3866 = true;
        } else if (opcode == 98) {
            boolean aBoolean3923 = true;
        } else if (opcode == 99) {
            int anInt3857 = buffer.getUByte();
            int anInt3835 = buffer.getUShort();
        } else if (opcode == 100) {
            int anInt3844 = buffer.getUByte();
            int anInt3913 = buffer.getUShort();
        } else if(opcode == 101) {
            int anInt3850 = buffer.getUByte();
        } else if(opcode == 102) {
            int mapSceneId = buffer.getUShort();
        } else if (opcode == 103) {
            int thirdInt = 0;
        } else if(opcode == 104) {
            int anInt3865 = buffer.getUByte();
        } else if(opcode == 105) {
            boolean aBoolean3906 = true;
        } else if(opcode == 106) {
            int anInt3881 = 0;
            int i_55_ = buffer.getUByte();
            animationArray = new int[i_55_];
            int[] anIntArray3833 = new int[i_55_];
            for (int i_56_ = 0; i_56_ < i_55_; i_56_++) {
                anIntArray3833[i_56_] = buffer.readUnsignedShort();
                int i_57_ = buffer.getUnsignedByte();
                animationArray[i_56_] = i_57_;
                anInt3881 += i_57_;
            }
        } else if (opcode == 107) {
            int mapDefinitionId = buffer.getUnsignedShort();
        } else if (opcode >= 150 && opcode < 155) {
            if(options == null)
                options = new String[5];
            options[opcode - 150] = buffer.getNewString();
            if (options[opcode - 150].equalsIgnoreCase("hidden"))
                options[opcode - 150] = null;
        } else if(opcode == 160) {
            int i_62_ = buffer.getUnsignedByte();
            int[] anIntArray3908 = new int[i_62_];
            for (int i_63_ = 0; i_62_ > i_63_; i_63_++)
                anIntArray3908[i_63_] = buffer.getUnsignedShort();
        } else if(opcode == 162) {
            //contouredGround = true;//(byte) 3;
            int anInt3882 = buffer.getInt();
        } else if (opcode == 163) {
            byte aByte3847 = (byte) buffer.getByte();
            byte aByte3849 = (byte) buffer.getByte();
            byte aByte3837 = (byte) buffer.getByte();
            byte aByte3914 = (byte) buffer.getByte();
        } else if(opcode == 164) {
            int anInt3834 = buffer.getShort();
        } else if(opcode == 165) {
            int anInt3875 = buffer.getShort();
        } else if(opcode == 166) {
            int anInt3877 = buffer.getShort();
        } else if (opcode == 167) {
            int anInt3921 = buffer.getUnsignedShort();
        } else if (opcode == 168) {
            boolean aBoolean3894 = true;
        } else if (opcode == 169) {
            boolean aBoolean3845 = true;
        } else if (opcode == 170) {
            int anInt3383 = buffer.getUnsignedSmart();
        } else if (opcode == 171) {
            int anInt3362 = buffer.getUnsignedSmart();
        } else if (opcode == 173) {
            int anInt3302 = buffer.getUnsignedShort();
            int anInt3336 = buffer.getUnsignedShort();
        } else if (opcode == 177) {
            boolean ub = true;
        } else if (opcode == 178) {
            int db = buffer.getUnsignedByte();
        } else if (opcode == 189) {
            boolean bloom = true;
        } else if (opcode == 249) {
            int length = buffer.getUnsignedByte();
            if (parameters == null)
                parameters = new HashMap<>(length);
            for (int index = 0; index < length; index++) {
                boolean bool = buffer.getUnsignedByte() == 1;
                int key = buffer.getMedium();
                parameters.put(key, bool ? buffer.getString() : buffer.getInt());
            }
        }
    }
}
