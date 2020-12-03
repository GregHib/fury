package com.fury.game.network.packet.out

import com.fury.core.model.node.entity.actor.figure.player.Player
import com.fury.game.network.packet.OutgoingPacket
import com.fury.game.network.packet.int

class InterfaceDisplayState(private val interfaceId: Int, private val hide: Boolean) : OutgoingPacket(171) {

    override fun encode(player: Player): Boolean {
        builder.put(hide.int)
        builder.putShort(interfaceId)
        return true
    }
}