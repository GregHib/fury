package com.fury.game.content.global.dnd.star;

import com.fury.cache.Revision;
import com.fury.core.model.node.entity.actor.figure.mob.Mob;
import com.fury.game.world.map.Position;
import com.fury.game.world.update.flag.block.Direction;

/**
 * Created by Greg on 10/12/2016.
 */
public class StarSprite extends Mob {

    public StarSprite(Position position) {
        super(ShootingStar.SHADOW, position, Revision.RS2, true);
        getDirection().setDirection(Direction.WEST);
    }

}
