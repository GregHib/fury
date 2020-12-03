package com.fury.game.npc.minigames.pest;

import com.fury.game.content.global.minigames.impl.PestControl;
import com.fury.core.model.node.entity.actor.figure.Figure;
import com.fury.game.world.GameWorld;
import com.fury.game.world.map.Position;
import com.fury.util.Misc;

public class Shifter extends PestMonsters {

    public Shifter(int id, Position tile, boolean spawned, int index, PestControl manager) {
        super(id, tile, spawned, index, manager);
    }

    @Override
    public void processNpc() {
        super.processNpc();
        Figure target = this.getPossibleTargets().get(0);
        if (this.getMobCombat().process() && !isWithinDistance(target, 10) || Misc.random(20) == 0)
            teleport(target);
    }

    private void teleport(Position tile) {
        moveTo(tile);
        animate(3904);
        GameWorld.schedule(1, () -> graphic(654));
    }
}
