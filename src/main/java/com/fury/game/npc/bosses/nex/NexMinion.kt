package com.fury.game.npc.bosses.nex

import com.fury.core.model.node.entity.actor.figure.mob.Mob
import com.fury.game.world.map.Position

class NexMinion(id: Int, tile: Position) : Mob(id, tile, true) {

    var isBarrierBroken: Boolean = false

    init {
        isCantFollowUnderCombat = true
        forceTargetDistance = 50
    }

    fun breakBarrier() {
        capDamage = 600
        isBarrierBroken = true
    }

    override fun processNpc() {
        if (isDead || !isBarrierBroken)
            return
        if (!mobCombat!!.process())
            checkAggression()
    }
}
