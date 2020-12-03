package com.fury.game.npc.slayer

import com.fury.core.model.node.entity.actor.figure.mob.Mob
import com.fury.game.world.GameWorld
import com.fury.game.world.map.Position
import com.fury.util.Misc

class Nechryael(id: Int, tile: Position, spawned: Boolean) : Mob(id, tile, spawned) {

    private var deathSpawns: Array<Mob?>? = null

    override fun processNpc() {
        if (hasActiveSpawns() && !combat.isInCombat())
            removeDeathSpawns()
        super.processNpc()
    }

    fun summonDeathSpawns() {
        deathSpawns = arrayOfNulls(2)
        val target = mobCombat!!.target
        for (idx in deathSpawns!!.indices) {
            val deathSpawn = GameWorld.mobs.spawn(id + 1, Misc.getFreeTile(this, 2), true)
            if (target != null)
                deathSpawn.setTarget(target)
            deathSpawns!![idx] = deathSpawn
        }
    }

    fun removeDeathSpawns() {
        for (mob in deathSpawns!!)
            mob?.deregister()
        deathSpawns = null
    }

    fun hasActiveSpawns(): Boolean {
        return deathSpawns != null
    }
}