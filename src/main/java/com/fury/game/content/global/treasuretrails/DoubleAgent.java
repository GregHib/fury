package com.fury.game.content.global.treasuretrails;

import com.fury.game.world.map.Position;
import com.fury.core.model.node.entity.actor.figure.mob.Mob;
import com.fury.core.model.node.entity.actor.figure.player.Player;

/**
 * Created by Greg on 27/09/2016.
 */
public class DoubleAgent extends Mob {

    Player target;

    public DoubleAgent(int id, Position position) {
        super(id, position);
    }

    public Player getTarget() {
        return target;
    }

    public void setTarget(Player target) {
        this.target = target;
    }
}
