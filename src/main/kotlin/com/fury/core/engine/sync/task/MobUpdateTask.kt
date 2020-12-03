package com.fury.core.engine.sync.task

import com.fury.core.engine.sync.SynchronizationTask
import com.fury.core.model.node.entity.actor.figure.player.Player
import com.fury.game.network.packet.out.MobUpdate
import com.fury.util.Logger

class MobUpdateTask(private val player: Player) : SynchronizationTask() {

    override fun run() {
        try {
            player.send(MobUpdate())
        } catch (ex: Exception) {
            Logger.handle(String.format("Error in %s %s", this::class.simpleName, player), ex)
        }
    }

}