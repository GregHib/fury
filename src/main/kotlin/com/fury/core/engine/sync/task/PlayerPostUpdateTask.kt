package com.fury.core.engine.sync.task

import com.fury.core.engine.sync.SynchronizationTask
import com.fury.core.model.node.entity.actor.figure.player.Player
import com.fury.game.world.update.flag.block.Direction
import com.fury.util.Logger
import java.util.*

class PlayerPostUpdateTask(private val player: Player) : SynchronizationTask() {

    override fun run() {
        try {
            player.updateFlags.clear()
            player.isChangingRegion = false
            player.movement.teleporting = false
            player.forcedChat = Optional.empty()
            player.isResetMovementQueue = false
            player.isNeedsPlacement = false
            player.movement.walkingDirection = Direction.NONE
            player.movement.runningDirection = Direction.NONE
            player.combat.hits.nextHits.clear()
            player.session.get().flush()
        } catch (ex: Exception) {
            Logger.handle(String.format("Error in %s", PlayerPostUpdateTask::class.java.simpleName), ex)
        }

    }

}