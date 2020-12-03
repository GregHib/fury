package com.fury.game.content.global.thievingguild;

import com.fury.cache.Revision;
import com.fury.core.model.node.entity.actor.figure.mob.Mob;
import com.fury.game.content.global.dnd.star.ShootingStar;
import com.fury.game.world.map.Position;
import com.fury.game.world.update.flag.block.Direction;

/**
 * Created by Leviticus on 06/14/2018.
 */
public class ThievingMaster extends Mob {

    public ThievingMaster(Position position) {
        super(ThievingGuild.THIEVING_MASTER, position, Revision.RS2, true);
        getDirection().setDirection(Direction.SOUTH);
        setWalkType(Mob.Companion.getNORMAL_WALK());
    }

}
