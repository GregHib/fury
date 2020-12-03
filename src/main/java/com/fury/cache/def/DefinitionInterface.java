package com.fury.cache.def;

import com.fury.cache.ByteBuffer;

public interface DefinitionInterface {

    void readValues(ByteBuffer buffer, int opcode);
}
