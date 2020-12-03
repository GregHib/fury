package com.fury.game.network.packet.out

import com.fury.core.model.node.entity.actor.figure.player.Player
import com.fury.game.network.packet.OutgoingPacket

class GravestoneTimer(private val timer: Int) : OutgoingPacket(120) {

    override fun encode(player: Player): Boolean {
        builder.putShort(timer)
        return true
    }

}