package com.fury.game.world.map.instance.impl;

import com.fury.game.world.GameWorld;
import com.fury.game.world.map.Position;
import com.fury.game.world.map.instance.BossInstance;

public class CorporealBeastInstance extends BossInstance {
    @Override
    public Position getInside() {
        return new Position(2885, 4374);
    }

    @Override
    public Position getOutside() {
        return new Position(3182, 5704);
    }

    @Override
    public int[] getMap() {
        return new int[] {360, 546};
    }

    @Override
    public void load() {
        GameWorld.getMobs().spawn(8133, getTile(new Position(2902, 4394)), false);
    }
}
