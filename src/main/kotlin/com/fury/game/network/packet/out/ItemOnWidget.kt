package com.fury.game.network.packet.out

import com.fury.core.model.node.entity.actor.figure.player.Player
import com.fury.core.model.item.Item
import com.fury.game.network.packet.OutgoingPacket

class ItemOnWidget(private val widget: Int, private val item: Item?) : OutgoingPacket(53) {

    override fun encode(player: Player): Boolean {
        builder.put(1)
        builder.putShort(widget)
        builder.putShort(1)
        if (item == null) {
            builder.put(0)
        } else {
            builder.put(1)
            builder.putInt(item.amount)
            builder.putInt(item.id + 1)
            builder.put(if (item.revision == null) 0 else item.revision.ordinal)
        }
        return true
    }

}