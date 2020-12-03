package com.fury.game.node.entity.mob

import com.fury.cache.Revision
import com.fury.core.model.node.entity.actor.figure.mob.Mob
import com.fury.game.world.map.Position

class AbyssalCreature(id: Int, position: Position, revision: Revision, spawned: Boolean) : Mob(id, position, revision, spawned) {
    init {
        forceAggressive = true
    }
}