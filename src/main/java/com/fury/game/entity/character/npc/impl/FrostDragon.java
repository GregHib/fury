package com.fury.game.entity.character.npc.impl;

import com.fury.game.world.map.Position;
import com.fury.core.model.node.entity.actor.figure.mob.Mob;
import com.fury.util.Misc;

/**
 * Created by Greg on 15/02/2017.
 */
public class FrostDragon extends Mob {

    private boolean magicOnly;

    public FrostDragon(int id, Position position) {
        super(id, position);
        setMagicOnly();
    }

    public boolean isMagicOnly() {
        return magicOnly;
    }

    private void setMagicOnly() {
        this.magicOnly = Misc.random(2) == 0;
    }
}
