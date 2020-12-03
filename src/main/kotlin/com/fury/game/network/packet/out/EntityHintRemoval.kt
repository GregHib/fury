package com.fury.game.network.packet.out

import com.fury.core.model.node.entity.actor.figure.player.Player
import com.fury.game.network.packet.OutgoingPacket
import com.fury.network.packet.ByteOrder

class EntityHintRemoval(private val playerHintRemoval: Boolean = false) : OutgoingPacket(254) {

    override fun encode(player: Player): Boolean {
        val type = if (playerHintRemoval) 10 else 1
        builder.put(type).putShort(-1)
        builder.putInt(0, ByteOrder.TRIPLE_INT)
        return true
    }

}