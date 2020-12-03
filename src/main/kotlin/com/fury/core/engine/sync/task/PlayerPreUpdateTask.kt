package com.fury.core.engine.sync.task

import com.fury.core.engine.sync.SynchronizationTask
import com.fury.core.model.node.entity.actor.figure.player.Player
import com.fury.util.Logger

class PlayerPreUpdateTask(private val player: Player) : SynchronizationTask() {

    override fun run() {
        try {
            player.movement.process()
        } catch (ex: Exception) {
            Logger.handle(String.format("error player.movement.processNextMovement(): %s", player), ex)
        }
    }

}