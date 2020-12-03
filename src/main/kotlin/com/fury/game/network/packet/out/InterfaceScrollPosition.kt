package com.fury.game.network.packet.out

import com.fury.core.model.node.entity.actor.figure.player.Player
import com.fury.game.network.packet.OutgoingPacket

class InterfaceScrollPosition(private val id: Int, private val position: Int) : OutgoingPacket(78) {

    override fun encode(player: Player): Boolean {
        builder.putShort(id)
        builder.putShort(position)
        return true
    }

}