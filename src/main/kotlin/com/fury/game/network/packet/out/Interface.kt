package com.fury.game.network.packet.out

import com.fury.core.model.node.entity.actor.figure.player.Player
import com.fury.game.network.packet.OutgoingPacket

class Interface(private val id: Int) : OutgoingPacket(97) {

    override fun encode(player: Player): Boolean {
        player.interfaceId = id
        builder.putInt(id)
        return true
    }

}