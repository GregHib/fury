package com.fury.game.network.packet.out

import com.fury.core.model.node.entity.actor.figure.player.Player
import com.fury.game.network.packet.OutgoingPacket
import com.fury.network.packet.ByteOrder
import com.fury.network.packet.ValueType

class PlayerHeadOnInterface(private val id: Int) : OutgoingPacket(185) {

    override fun encode(player: Player): Boolean {
        builder.putShort(id, ValueType.A, ByteOrder.LITTLE)
        return true
    }

}