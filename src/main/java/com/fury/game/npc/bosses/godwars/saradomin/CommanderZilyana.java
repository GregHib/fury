package com.fury.game.npc.bosses.godwars.saradomin;

import com.fury.game.npc.bosses.godwars.GodWarMinion;
import com.fury.game.npc.bosses.godwars.GodWarsBoss;
import com.fury.game.world.map.Position;

public class CommanderZilyana extends GodWarsBoss {

    public static final GodWarMinion[] minions = new GodWarMinion[3];

    public CommanderZilyana(int id, Position position, boolean spawned) {
        super(id, position, spawned);
    }

    @Override
    public void respawnMinions() {
        for (GodWarMinion minion : minions)
            if (minion.getFinished() || minion.isDead())
                minion.respawn();
    }
}
