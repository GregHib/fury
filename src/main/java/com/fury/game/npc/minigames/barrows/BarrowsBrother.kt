package com.fury.game.npc.minigames.barrows

import com.fury.core.model.node.entity.actor.figure.mob.Mob
import com.fury.game.content.global.minigames.impl.Barrows
import com.fury.game.world.map.Position
import com.fury.util.Misc

class BarrowsBrother(id: Int, tile: Position, val barrows: Barrows) : Mob(id, tile, true) {
    override val meleePrayerMultiplier: Double
        get() = (if (id != 2030) 0 else if (Misc.random(3) == 0) 1 else 0).toDouble()
}
