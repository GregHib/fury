package com.fury.game.network.packet.out

import com.fury.core.model.node.entity.actor.figure.player.Player
import com.fury.game.network.packet.OutgoingPacket
import com.fury.network.packet.ByteOrder

class SystemUpdate(private val time: Int) : OutgoingPacket(114) {

    override fun encode(player: Player): Boolean {
        builder.putShort(time, ByteOrder.LITTLE)
        return true
    }

}