package com.fury.game.npc.bosses.godwars

import com.fury.core.model.node.entity.actor.figure.Figure
import com.fury.core.model.node.entity.actor.figure.mob.Mob
import com.fury.engine.task.executor.GameExecutorManager
import com.fury.game.node.entity.actor.figure.player.handles.Settings
import com.fury.game.world.GameWorld
import com.fury.game.world.World
import com.fury.game.world.map.Position
import java.util.*
import java.util.concurrent.TimeUnit

abstract class GodWarsBoss(id: Int, position: Position, spawned: Boolean) : Mob(id, position, spawned) {

    override val possibleTargets: ArrayList<Figure>
        get() {
            val possibleTarget = ArrayList<Figure>()
            for (player in GameWorld.regions.getLocalPlayers(this)) {
                if (player == null
                        || player.isDead
                        || player.finished
                        || !player.settings.getBool(Settings.RUNNING)
                        || !player.isWithinDistance(this, 64)
                        || (!inMulti() || !player.inMulti()) && player.combat.attackedBy !== this && player.combat.attackedByDelay > System.currentTimeMillis()
                        || !combat.clippedProjectile(player, false))
                    continue
                possibleTarget.add(player)
            }
            return possibleTarget
        }

    override fun setRespawnTask() {
        if (!finished) {
            reset()
            setPosition(respawnTile!!)
            deregister()
        }
        val mob = this
        GameExecutorManager.slowExecutor.schedule({
            finished = false
            GameWorld.mobs.add(mob)
            mob.lastRegionId = 0
            World.updateEntityRegion(mob)
            loadMapRegions()
            checkMulti()
            respawnMinions()
        }, (combatDefinition.respawnDelay * 600).toLong(),
                TimeUnit.MILLISECONDS)
    }

    abstract fun respawnMinions()
}
