package com.fury.game.world.map.instance.impl;

import com.fury.game.world.GameWorld;
import com.fury.game.world.map.Position;
import com.fury.game.world.map.instance.BossInstance;

public class DagannothKingsInstance extends BossInstance {
    @Override
    public Position getInside() {
        return new Position(2900, 4449);
    }

    @Override
    public Position getOutside() {
        return new Position(3182, 5704);
    }

    @Override
    public int[] getMap() {
        return new int[] {361, 553};
    }

    @Override
    public void load() {
        GameWorld.getMobs().spawn(2896, getTile(new Position(2905, 4435)), false);
        GameWorld.getMobs().spawn(2896, getTile(new Position(2926, 4458)), false);
        GameWorld.getMobs().spawn(2896, getTile(new Position(2898, 4446)), false);
        GameWorld.getMobs().spawn(2896, getTile(new Position(2928, 4439)), false);
        GameWorld.getMobs().spawn(2883, getTile(new Position(2920, 4444)), false);
        GameWorld.getMobs().spawn(2882, getTile(new Position(2906, 4444)), false);
        GameWorld.getMobs().spawn(2896, getTile(new Position(2929, 4444)), false);
        GameWorld.getMobs().spawn(2896, getTile(new Position(2912, 4433)), false);
        GameWorld.getMobs().spawn(2896, getTile(new Position(2899, 4456)), false);
        GameWorld.getMobs().spawn(2896, getTile(new Position(2928, 4453)), false);
        GameWorld.getMobs().spawn(2896, getTile(new Position(2911, 4464)), false);
    }
}
