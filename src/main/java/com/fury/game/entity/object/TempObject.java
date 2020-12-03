package com.fury.game.entity.object;

import com.fury.cache.Revision;
import com.fury.game.world.map.Position;

public class TempObject extends GameObject {

    private long time;

    public TempObject(int id, Position position, int type, int face, Revision revision, long time) {
        super(id, position, type, face, revision);
        this.time = time;
    }

    public TempObject(int id, Position position, int type, int face, long time) {
        super(id, position, type, face);
        this.time = time;
    }

    public long getTime() {
        return time;
    }

    public void addTime(long time) {
        this.time += time;
    }
}
