package com.fury.game.network.packet.out

import com.fury.cache.Revision
import com.fury.core.model.node.entity.actor.figure.player.Player
import com.fury.game.network.packet.OutgoingPacket
import com.fury.network.packet.ByteOrder

class InterfaceItemModel(private val interfaceId: Int, private val itemId: Int, private val revision: Revision, private val zoom: Int) : OutgoingPacket(246) {

    override fun encode(player: Player): Boolean {
        builder.putShort(interfaceId, ByteOrder.LITTLE)
        builder.putShort(zoom)
        builder.putInt(itemId)
        builder.put(revision.ordinal)
        return true
    }

}