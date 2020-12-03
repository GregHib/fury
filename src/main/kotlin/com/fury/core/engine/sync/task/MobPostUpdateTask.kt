package com.fury.core.engine.sync.task

import com.fury.core.engine.sync.SynchronizationTask
import com.fury.core.model.node.entity.actor.figure.mob.Mob
import com.fury.game.world.update.flag.block.Direction
import com.fury.util.Logger
import java.util.*

class MobPostUpdateTask(private val mob: Mob) : SynchronizationTask() {

    override fun run() {
        try {
            mob.updateFlags.clear()
            mob.movement.teleporting = false
            mob.forcedChat = Optional.empty()
            mob.isNeedsPlacement = false
            mob.movement.walkingDirection = Direction.NONE
            mob.movement.runningDirection = Direction.NONE
            mob.combat.hits.nextHits.clear()
        } catch (ex: Exception) {
            Logger.handle(String.format("Error in %s", this::class.simpleName), ex)
        }
    }

}