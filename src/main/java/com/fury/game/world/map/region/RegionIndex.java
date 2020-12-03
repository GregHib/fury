package com.fury.game.world.map.region;

import com.fury.cache.Revision;

/**
 * Created by Greg on 20/12/2016.
 */
public class RegionIndex {
    public int id;
    public int landscape;
    public int mapObject;
    public Revision revision;

    public RegionIndex(int regionId, int map, int landscape, Revision revision) {
        this.id = regionId;
        this.landscape = landscape;
        this.mapObject = map;
        this.revision = revision;
    }
}
