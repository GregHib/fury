package com.fury.game.npc.slayer;

import com.fury.core.model.node.entity.actor.figure.Figure;
import com.fury.core.model.node.entity.actor.figure.mob.Mob;
import com.fury.game.world.map.Position;
import com.fury.util.Misc;

public class AbyssalDemon extends Mob {

    public AbyssalDemon(int id, Position tile, boolean spawned) {
        super(id, tile, spawned);
    }

    @Override
    public void processNpc() {
        super.processNpc();
        Figure target = getMobCombat().getTarget();
        if (target != null && Misc.isOnRange(target.getX(), target.getY(), target.getSizeX(), target.getSizeY(), getX(), getY(), getSizeX(), getSizeY(), 4) && Misc.random(50) == 0) {
            sendTeleport(target);
            sendTeleport(this);
        }
    }

    private void sendTeleport(Figure figure) {
        figure.graphic(409);
        figure.moveTo(Misc.getFreeTile(figure.copyPosition(), 1));
    }
}