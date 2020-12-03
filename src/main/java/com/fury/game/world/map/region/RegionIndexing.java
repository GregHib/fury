package com.fury.game.world.map.region;

import com.fury.cache.ByteStream;
import com.fury.cache.Revision;
import com.fury.game.GameLoader;
import com.fury.game.GameSettings;

/**
 * Created by Greg on 20/12/2016.
 */
public class RegionIndexing {
    public static RegionIndex[][] regions = new RegionIndex[Revision.values().length][];

    public static void init() {
        long startup = System.currentTimeMillis();

        for(Revision revision : Revision.values())
            load(revision);

        int total = 0;

        for(Revision revision : Revision.values())
            total += regions[revision.ordinal()].length;

        if(GameSettings.DEBUG)
            System.out.println("Loaded " + total + " region indices " + (System.currentTimeMillis() - startup) + "ms");
    }

    public static void load(Revision revision) {
        String name;
        switch (revision) {
            case RS2:
            default:
                name = "659";
                break;
            case PRE_RS3:
                name = "742";
                break;
        }
        byte[] data = GameLoader.getCache().getArchive(5).get("map_index_" + name);
        final ByteStream stream = new ByteStream(data);
        int size = stream.getUShort();
        regions[revision.ordinal()] = new RegionIndex[size];
        for (int i = 0; i < size; i++)
            regions[revision.ordinal()][i] = new RegionIndex(stream.getUShort(), stream.getUShort(), stream.getUShort(), revision);
    }

    public static RegionIndex get(int regionId) {
        for (RegionIndex region : regions[Revision.getRevision(regionId).ordinal()])
            if (region.id == regionId)
                return region;
        return null;
    }

}
