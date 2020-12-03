package com.fury.core.engine.sync.task

import com.fury.core.engine.sync.SynchronizationTask
import com.fury.core.model.node.entity.actor.figure.player.Player
import com.fury.game.network.packet.out.PlayerUpdate
import com.fury.game.world.GameWorld
import com.fury.util.Logger

class PlayerUpdateTask(private val player: Player) : SynchronizationTask() {

    override fun run() {
        try {
            player.send(PlayerUpdate())
        } catch (ex: Exception) {
            Logger.handle(String.format("Error in %s %s", PlayerUpdateTask::class.java.simpleName, player), ex)
            GameWorld.players.remove(player)
        }
    }

}