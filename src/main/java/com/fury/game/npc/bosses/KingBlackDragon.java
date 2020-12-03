package com.fury.game.npc.bosses;

import com.fury.core.model.node.entity.actor.figure.mob.Mob;
import com.fury.game.world.map.Position;

public class KingBlackDragon extends Mob {
    public KingBlackDragon(int id, Position position, boolean spawned) {
        super(id, position, spawned);
        setLureDelay(0);
    }
}
