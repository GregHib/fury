package com.fury.game.network.packet.out

import com.fury.cache.Revision
import com.fury.core.model.node.entity.actor.figure.player.Player
import com.fury.game.network.packet.OutgoingPacket
import com.fury.network.packet.ByteOrder
import com.fury.network.packet.ValueType

class NpcHeadOnInterface(private val id: Int, private val revision: Revision, private val interfaceId: Int) : OutgoingPacket(75) {

    override fun encode(player: Player): Boolean {
        builder.putShort(id, ValueType.A, ByteOrder.LITTLE)
        builder.putShort(interfaceId, ValueType.A, ByteOrder.LITTLE)
        builder.put(revision.ordinal)
        return true
    }

}