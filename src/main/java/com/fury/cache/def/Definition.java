package com.fury.cache.def;

import com.fury.cache.ByteBuffer;

public abstract class Definition implements DefinitionInterface {

    public void changeValues() {}

    public int[] getIndices(ByteBuffer index, int total) {
        return getIndices(index, total, 0);
    }

    public final int[] getIndices(ByteBuffer index, int total, int offset) {
        int[] indices = new int[total];

        for (int id = 0; id < total; id++) {
            int length = index.getUnsignedShort();
            indices[id] = offset;
            offset += length;
        }

        return indices;
    }

    public final void readValueLoop(ByteBuffer buffer) {
        while (true) {
            int opcode = buffer.readUnsignedByte();
            if (opcode == 0)
                return;

            readValues(buffer, opcode);
        }
    }
}
