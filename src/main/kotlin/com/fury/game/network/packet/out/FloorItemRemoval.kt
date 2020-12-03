package com.fury.game.network.packet.out

import com.fury.core.model.item.FloorItem
import com.fury.core.model.node.entity.actor.figure.player.Player
import com.fury.game.network.packet.OutgoingPacket
import com.fury.network.packet.ValueType

class FloorItemRemoval(private val floorItem: FloorItem) : OutgoingPacket(156) {

    override fun encode(player: Player): Boolean {
        player.send(Location(floorItem.tile))
        builder.put(floorItem.tile.z)
        builder.put(0, ValueType.A)
        builder.putInt(floorItem.id)
        builder.put(floorItem.revision.ordinal)
        return true
    }

}