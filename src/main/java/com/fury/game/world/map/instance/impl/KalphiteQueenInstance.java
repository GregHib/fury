package com.fury.game.world.map.instance.impl;

import com.fury.game.world.GameWorld;
import com.fury.game.world.map.Position;
import com.fury.game.world.map.instance.BossInstance;

public class KalphiteQueenInstance extends BossInstance {
    @Override
    public Position getInside() {
        return new Position(3507, 9494);
    }

    @Override
    public Position getOutside() {
        return new Position(3182, 5704);
    }

    @Override
    public int[] getMap() {
        return new int[] {433, 1185};
    }

    @Override
    public void load() {
        GameWorld.getMobs().spawn(1157, getTile(new Position(3480, 9508)), false);
        GameWorld.getMobs().spawn(1157, getTile(new Position(3495, 9505)), false);
        GameWorld.getMobs().spawn(1157, getTile(new Position(3492, 9490)), false);
        GameWorld.getMobs().spawn(1157, getTile(new Position(3472, 9497)), false);
        GameWorld.getMobs().spawn(1158, getTile(new Position(3484, 9491)), false);
        GameWorld.getMobs().spawn(1161, getTile(new Position(3473, 9487)), false);
        GameWorld.getMobs().spawn(1161, getTile(new Position(3476, 9496)), false);
        GameWorld.getMobs().spawn(1161, getTile(new Position(3473, 9501)), false);
        GameWorld.getMobs().spawn(1161, getTile(new Position(3481, 9504)), false);
        GameWorld.getMobs().spawn(1161, getTile(new Position(3488, 9514)), false);
        GameWorld.getMobs().spawn(1161, getTile(new Position(3497, 9501)), false);
        GameWorld.getMobs().spawn(1161, getTile(new Position(3486, 9490)), false);
        GameWorld.getMobs().spawn(1156, getTile(new Position(3479, 9484)), false);
        GameWorld.getMobs().spawn(1156, getTile(new Position(3483, 9491)), false);
        GameWorld.getMobs().spawn(1156, getTile(new Position(3496, 9493)), false);
        GameWorld.getMobs().spawn(1156, getTile(new Position(3488, 9502)), false);
        GameWorld.getMobs().spawn(1156, getTile(new Position(3473, 9502)), false);
        GameWorld.getMobs().spawn(1156, getTile(new Position(3480, 9526)), false);
        GameWorld.getMobs().spawn(1156, getTile(new Position(3500, 9520)), false);
        GameWorld.getMobs().spawn(1156, getTile(new Position(3504, 9525)), false);
        GameWorld.getMobs().spawn(1156, getTile(new Position(3506, 9518)), false);
        GameWorld.getMobs().spawn(1154, getTile(new Position(3467, 9481)), false);
        GameWorld.getMobs().spawn(1154, getTile(new Position(3467, 9489)), false);
    }
}
