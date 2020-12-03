package com.fury.cache.def.anim;


import com.fileserver.cache.impl.CacheArchive;
import com.fury.cache.ByteBuffer;
import com.fury.cache.Revision;

import java.util.HashMap;

public class Animation659 extends GameAnimation {
    public static Animation659[] animations;
    public static int total;
    int[][] handledSounds = null;

    public Animation659(int id) {
        super(id);
        revision = Revision.RS2;
    }

    public void load(CacheArchive archive, String name) {
        ByteBuffer buffer = new ByteBuffer(archive.get(name + ".dat"));
        total = buffer.getUnsignedShort();
        animations = new Animation659[total];

        for(int id = 0; id < total; id++) {
            if (animations[id] == null)
                animations[id] = new Animation659(id);
            animations[id].readValueLoop(buffer);
            animations[id].setPrecedence();
            handledSounds = null;
        }
    }

    public Animation659 forId(int id) {
        if(id >= animations.length)
            return animations[0];
        return animations[id];
    }

    @Override
    public void readValues(ByteBuffer buffer, int opcode) {
        if(opcode == 1) {
            frameCount = buffer.getUnsignedShort();
            primaryFrames = new int[frameCount];
            secondaryFrames = new int[frameCount];
            durations = new int[frameCount];
            for(int i = 0; i < frameCount; i++){
                primaryFrames[i] = buffer.getIntLittleEndian();
                secondaryFrames[i] = -1;
            }
            for(int i = 0; i < frameCount; i++)
                durations[i] = buffer.getUnsignedByte();
        } else if(opcode == 2) {
            loopOffset = buffer.getUnsignedShort();
        } else if(opcode == 3) {
            int count = buffer.getUnsignedByte();
            interleaveOrder = new int[count + 1];
            for(int l = 0; l < count; l++)
                interleaveOrder[l] = buffer.getUnsignedByte();
            interleaveOrder[count] = 0x98967f;
        } else if(opcode == 4) {
            stretches = true;
        } else if(opcode == 5) {
            priority = buffer.getUnsignedByte();
        } else if(opcode == 6) {
            playerOffhand = buffer.getUnsignedShort();
        } else if(opcode == 7) {
            playerMainhand = buffer.getUnsignedShort();
        } else if(opcode == 8) {
            maximumLoops = buffer.getUnsignedByte();
        } else if(opcode == 9) {
            animationPrecedence = buffer.getUnsignedByte();
        } else if(opcode == 10) {
            walkingPrecedence = buffer.getUnsignedByte();
        } else if(opcode == 11) {
            replayMode = buffer.getUnsignedByte();
        } else if (opcode == 12) {
            int[] anIntArray2151 = null;
            int i_7_ = buffer.getUnsignedByte();
            anIntArray2151 = new int[i_7_];
            for (int i_8_ = 0; i_8_ < i_7_; i_8_++)
                anIntArray2151[i_8_] = buffer.getUnsignedShort();
            for (int i_9_ = 0; i_9_ < i_7_; i_9_++)
                anIntArray2151[i_9_] = (buffer.getUnsignedShort() << 16) + anIntArray2151[i_9_];
        } else if (opcode == 13) {
            int i_10_ = buffer.getUnsignedShort();
            handledSounds = new int[i_10_][];
            for (int i_11_ = 0; i_11_ < i_10_; i_11_++) {
                int i_12_ = buffer.getUnsignedByte();
                if (i_12_ > 0) {
                    handledSounds[i_11_] = new int[i_12_];
                    handledSounds[i_11_][0] = buffer.getTribyte();
                    for (int i_13_ = 1; i_13_ < i_12_; i_13_++)
                        handledSounds[i_11_][i_13_] = buffer.getUnsignedShort();
                }
            }
        }
        else if (opcode == 14) {
            boolean aBoolean2158 = true;
        } else if (opcode == 15) {
            boolean aBoolean2159 = true;
        } else if (opcode == 19) {
            int[] anIntArray1362 = null;
            if (anIntArray1362 == null) {
                anIntArray1362 = new int[handledSounds.length];
                for (int i_14_ = 0; i_14_ < handledSounds.length; i_14_++)
                    anIntArray1362[i_14_] = 255;
            }
            anIntArray1362[buffer.getUnsignedByte()] = buffer.getUnsignedByte();
        } else if (opcode == 20) {
            int[] soundMaxDelay = null;
            int[] soundMinDelay = null;
            if (soundMaxDelay == null || null == soundMinDelay) {
                soundMaxDelay = new int[handledSounds.length];
                soundMinDelay = new int[handledSounds.length];
                for (int i_15_ = 0; i_15_ < handledSounds.length; i_15_++) {
                    soundMaxDelay[i_15_] = 256;
                    soundMinDelay[i_15_] = 256;
                }
            }
            int i_16_ = buffer.getUnsignedByte();
            soundMaxDelay[i_16_] = buffer.getUnsignedShort();
            soundMinDelay[i_16_] = buffer.getUnsignedShort();
        } else if (opcode == 22) {
            buffer.getUnsignedByte();
        } else if (opcode == 23) {
            buffer.getUnsignedShort();
        } else if (opcode == 24) {
            buffer.getUnsignedShort();
        } else if (opcode == 249) {
            int length = buffer.getUnsignedByte();
            HashMap<Integer, Object> clientScriptData = null;
            if (clientScriptData == null)
                clientScriptData = new HashMap<>(length);
            for (int index = 0; index < length; index++) {
                boolean stringInstance = buffer.getUnsignedByte() == 1;
                int key = buffer.getTribyte();
                Object value = stringInstance ? buffer.getString() : buffer.getInt();
                clientScriptData.put(key, value);
            }
        } else
            System.out.println("Unrecognized config code: " + opcode);
    }
}
