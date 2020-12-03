package com.fury.core.action

import com.fury.core.event.Event
import com.fury.core.event.PlayerEventListener
import com.fury.core.model.node.entity.actor.figure.player.Player
import java.util.*

object PlayerActionBus {
    private val chain = HashSet<PlayerEventListener>()

    fun subscribe(listener: PlayerEventListener) {
        chain.add(listener)
    }

    @JvmStatic
    fun publish(player: Player, event: Event): Boolean {
        return chain.any { it.accept(player, event) }
    }
}