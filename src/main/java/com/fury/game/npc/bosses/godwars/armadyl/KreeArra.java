package com.fury.game.npc.bosses.godwars.armadyl;

import com.fury.game.npc.bosses.godwars.GodWarMinion;
import com.fury.game.npc.bosses.godwars.GodWarsBoss;
import com.fury.game.world.map.Position;

public class KreeArra extends GodWarsBoss {
    public static final GodWarMinion[] minions = new GodWarMinion[3];

    public KreeArra(int id, Position position, boolean spawned) {
        super(id, position, spawned);
    }

    @Override
    public void respawnMinions() {
        for (GodWarMinion minion : minions)
            if (minion.getFinished() || minion.isDead())
                minion.respawn();
    }
}
