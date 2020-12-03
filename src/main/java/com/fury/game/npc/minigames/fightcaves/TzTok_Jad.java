package com.fury.game.npc.minigames.fightcaves;

import com.fury.game.content.controller.impl.FightCavesController;
import com.fury.game.world.map.Position;

public class TzTok_Jad extends FightCavesMob {

    private boolean spawnedMinions;
    private FightCavesController controller;

    public TzTok_Jad(int id, Position position, FightCavesController controller) {
        super(id, position);
        this.controller = controller;
        setBlockDeflections(true);
        setForceAggressive(true);
    }

    @Override
    public void processNpc() {
        super.processNpc();
        if(!spawnedMinions && getHealth().getHitpoints() < getMaxConstitution() / 2) {
            spawnedMinions = true;
            controller.spawnHealers(this);
        }
    }
}
