package com.fury.cache;

import com.fury.game.world.map.region.RegionIndexing;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

public enum Revision {
    RS2,
    PRE_RS3,
    RS3;

    public static short[] RS2_REGION_IDS = {13656, 13913, 13399, 13394, 13396, 18513, 18769, 19279, 19280, 19281};
    //                                         redsand       |poly |m army|trollheim    |dung|draynor
    public static short[] PRE_RS3_REGION_IDS = {10284, 9515, 18516, 9771, 11321, 13620, 313, 12338};
    public static short[] DISABLED_REGION_IDS = {13385, 295, 301, 307, 7511, 578, 580, 582, 584, 9050};

    public static Revision getRevision(int regionId) {
        for (int arrLen = 0; arrLen < RS2_REGION_IDS.length; arrLen++)
            if (regionId == RS2_REGION_IDS[arrLen])
                return RS2;

        for (int arrLen = 0; arrLen < PRE_RS3_REGION_IDS.length; arrLen++)
            if (regionId == PRE_RS3_REGION_IDS[arrLen])
                return PRE_RS3;

        for (Revision revision : Revision.values())
            for (int index = 0; index < RegionIndexing.regions[revision.ordinal()].length; index++)
                if (RegionIndexing.regions[revision.ordinal()][index].id == regionId)
                    return revision;
        return RS2;
    }

    public static ByteBuf regionBuffer() {
        ByteBuf buffer = Unpooled.buffer(4 + (RS2_REGION_IDS.length + PRE_RS3_REGION_IDS.length) * 2);
        buffer.writeShort(RS2_REGION_IDS.length);
        for(short region : RS2_REGION_IDS)
            buffer.writeShort(region);
        buffer.writeShort(PRE_RS3_REGION_IDS.length);
        for(short region : PRE_RS3_REGION_IDS)
            buffer.writeShort(region);
        buffer.writeShort(DISABLED_REGION_IDS.length);
        for(short region : DISABLED_REGION_IDS)
            buffer.writeShort(region);
        return buffer;
    }

    public int getMapIndex() {
        return Type.MAP.getIndex() + ordinal();
    }

    public enum Type {
        MODEL(1),
        ANIM(4),
        MAP(6),
        MUSIC(8);

        int index;
        Type(int index) {
            this.index = index;
        }

        public int getIndex() {
            return index;
        }
    }
}

