package com.fury.game.node.entity.actor.figure.mob

import com.fury.Config
import com.fury.cache.Revision
import com.fury.core.model.node.entity.actor.figure.mob.Mob
import com.fury.game.node.entity.EntityList
import com.fury.game.world.map.Position

class MobManager {

    val mobs = EntityList<Mob>(Config.MAX_MOBS, false)

    fun add(mob: Mob): Boolean {
        return mobs.add(mob)
    }

    fun remove(mob: Mob): Boolean {
        return mobs.remove(mob)
    }

    fun spawn(id: Int, tile: Position, spawned: Boolean = false): Mob {
        return MobBuilder.spawnNpc(id, Revision.RS2, tile, spawned)
    }

    @JvmOverloads fun spawn(id: Int, revision: Revision = Revision.RS2, tile: Position, spawned: Boolean = false): Mob {
        return MobBuilder.spawnNpc(id, revision, tile, spawned)
    }

    fun get(id: Int, position: Position): Mob? {
        return mobs.firstOrNull { !it.isDead && !it.finished && it.id == id && it.sameAs(position) }
    }
}