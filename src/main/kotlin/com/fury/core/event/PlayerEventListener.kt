package com.fury.core.event

import com.fury.core.model.node.entity.actor.figure.player.Player

interface PlayerEventListener : EventListener {
    fun accept(player: Player, event: Event): Boolean
}