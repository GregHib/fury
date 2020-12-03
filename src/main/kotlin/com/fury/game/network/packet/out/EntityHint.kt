package com.fury.game.network.packet.out

import com.fury.core.model.node.entity.actor.figure.Figure
import com.fury.core.model.node.entity.actor.figure.player.Player
import com.fury.game.network.packet.OutgoingPacket
import com.fury.network.packet.ByteOrder

class EntityHint(private val entity: Figure) : OutgoingPacket(254) {

    override fun encode(player: Player): Boolean {
        builder.put(if (entity is Player) 10 else 1)//Type
        builder.putShort(entity.index)
        builder.putInt(0, ByteOrder.TRIPLE_INT)
        return true
    }

}