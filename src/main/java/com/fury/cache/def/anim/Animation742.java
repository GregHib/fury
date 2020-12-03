package com.fury.cache.def.anim;

import com.fileserver.cache.impl.CacheArchive;
import com.fury.cache.ByteBuffer;
import com.fury.cache.Revision;

public class Animation742 extends GameAnimation {
    public static Animation742[] animations;
    public static int total;

    public Animation742(int id) {
        super(id);
        revision = Revision.PRE_RS3;
    }

    public void load(CacheArchive archive, String name) {
        ByteBuffer buffer = new ByteBuffer(archive.get(name + ".dat"));
        total = buffer.getUnsignedShort();
        animations = new Animation742[total];

        for(int id = 0; id < total; id++) {
            if (animations[id] == null)
                animations[id] = new Animation742(id);
            animations[id].readValueLoop(buffer);
            animations[id].setPrecedence();
        }
    }

    public Animation742 forId(int id) {
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
            for(int i_ = 0; i_ < frameCount; i_++){
                primaryFrames[i_] = buffer.getIntLittleEndian();
                secondaryFrames[i_] = -1;
            }
            for(int i_ = 0; i_ < frameCount; i_++)
                durations[i_] = buffer.getUnsignedByte();
        } else if(opcode == 2) {
            loopOffset = buffer.getUnsignedShort();
        } else if(opcode == 3) {
            int k = buffer.getUnsignedByte();
            interleaveOrder = new int[k + 1];
            for(int l = 0; l < k; l++)
                interleaveOrder[l] = buffer.getUnsignedByte();
            interleaveOrder[k] = 0x98967f;
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
            int length = buffer.getUnsignedByte();
            secondaryFrames = new int[length];
            for (int index = 0; index < length; index++)
                secondaryFrames[index] = buffer.getInt();
        } else
            System.out.println("Unrecognized config code: " + opcode);
    }
}
