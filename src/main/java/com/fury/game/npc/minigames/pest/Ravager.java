package com.fury.game.npc.minigames.pest;

import com.fury.game.content.global.minigames.impl.PestControl;
import com.fury.game.world.map.Position;

public class Ravager extends PestMonsters {

    boolean destroyingObject = false;

    public Ravager(int id, Position tile, boolean spawned, int index, PestControl manager) {
        super(id, tile, spawned, index, manager);
    }
}