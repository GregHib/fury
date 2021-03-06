package com.fury.game.npc.misc.battlefield;

import com.fury.core.model.node.entity.actor.figure.Figure;
import com.fury.core.model.node.entity.actor.figure.mob.Mob;
import com.fury.game.world.map.Position;

import java.util.ArrayList;

public class BlackGuard extends Mob {
    public BlackGuard(int id, Position position, boolean spawned) {
        super(id, position, spawned);
    }

    @Override
    public ArrayList<Figure> getPossibleTargets() {
        ArrayList<Figure> possibilities = getPossibleTargets(true, true);
        ArrayList<Figure> targets = new ArrayList<>();
        for (Figure t : possibilities) {
            if (t instanceof BlackGuard || t.isFamiliar())
                continue;
            targets.add(t);
        }
        return targets;
    }
}
