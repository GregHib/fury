package com.fury.game.npc.bosses.glacors

import com.fury.game.world.World
import com.fury.game.world.map.Position

class Glacor(id: Int, tile: Position) : Glacyte(null, id, tile) {

    var glacites: MutableList<Glacyte>? = null
    var isRangeAttack: Boolean = false

    override val magePrayerMultiplier: Double
        get() = 0.5

    override val rangePrayerMultiplier: Double
        get() = 0.5

    override val meleePrayerMultiplier: Double
        get() = 0.5

    init {
        effect = (-1).toByte()
        glacor = this
        isSpawned = false
    }

    override fun processNpc() {
        super.processNpc()

        if (mobCombat!!.target == null) {
            targetIndex = -1
            resetMinions()
            reset()
        } else {
            targetIndex = mobCombat!!.target.index
        }
    }

    fun removeGlacyte(glacyte: Glacyte) {
        if (glacites != null)
            glacites!!.remove(glacyte)
    }

    fun createGlacites() {
        for (index in 0..2) {
            tileLoop@ for (tileAttempt in 0..9) {
                val tile = Position(this, 2)
                if (World.isTileFree(tile.x, tile.y, 0, 1)) {
                    glacites!!.add(Glacyte(this, 14302 + index, tile))
                    break@tileLoop
                }
            }
        }
    }

    fun resetMinions() {
        if (glacites != null)
            for (g in glacites!!)
                g.deregister()
        glacites = null
        effect = (-1).toByte()
    }
}
