package com.fury.game.network.packet.out

import com.fury.core.model.node.entity.actor.figure.player.Player
import com.fury.game.network.packet.OutgoingPacket

class MoneyPouchFlash(private val added: Boolean) : OutgoingPacket(24) {

    override fun encode(player: Player): Boolean {
        builder.put(if (added) 1 else 0)
        return true
    }

}