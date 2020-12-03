package com.fury.game.network.packet.out

import com.fury.core.model.node.entity.actor.figure.player.Player
import com.fury.game.network.packet.OutgoingPacket
import com.fury.network.packet.ByteOrder

class ChatboxInterface(private val id: Int) : OutgoingPacket(164) {

    override fun encode(player: Player): Boolean {
        builder.putShort(id, ByteOrder.LITTLE)
        return true
    }

}