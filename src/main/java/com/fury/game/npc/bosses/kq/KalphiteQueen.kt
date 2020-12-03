package com.fury.game.npc.bosses.kq

import com.fury.core.model.node.entity.actor.figure.Figure
import com.fury.core.model.node.entity.actor.figure.mob.Mob
import com.fury.game.node.entity.actor.figure.player.handles.Settings
import com.fury.game.world.GameWorld
import com.fury.game.world.World
import com.fury.game.world.map.Position
import com.fury.util.Utils
import java.util.*

class KalphiteQueen(id: Int, position: Position, spawned: Boolean) : Mob(id, position, spawned) {

    override val possibleTargets: ArrayList<Figure>
        get() {
            val possibleTarget = ArrayList<Figure>()
            for (player in GameWorld.regions.getLocalPlayers(this)) {
                if (player == null
                        || player.isDead
                        || player.finished
                        || !player.settings.getBool(Settings.RUNNING)
                        || !player.isWithinDistance(this, 64)
                        || (!inMulti() || !player.inMulti()) && player.combat.attackedBy !== this && player.combat.attackedByDelay > Utils.currentTimeMillis()
                        || !combat.clippedProjectile(player, false))
                    continue
                possibleTarget.add(player)
            }
            return possibleTarget
        }

    init {
        lureDelay = 0
        forceAggressive = true
        setTransformation(1158)
    }

    fun respawn() {
        finished = false
        GameWorld.mobs.add(this)
        lastRegionId = 0
        World.updateEntityRegion(this)
        loadMapRegions()
        checkMulti()
    }

    companion object {

        fun atKQ(tile: Position): Boolean {
            return if (tile.x >= 3462 && tile.x <= 3510 && tile.y >= 9462 && tile.y <= 9528) true else false
        }
    }
}
