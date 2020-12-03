package com.fury.game.network.packet.out

import com.fury.core.model.item.FloorItem
import com.fury.core.model.node.entity.actor.figure.player.Player
import com.fury.game.network.packet.OutgoingPacket

class FloorItemSpawn(private val floorItem: FloorItem) : OutgoingPacket(44) {

    override fun encode(player: Player): Boolean {
        if (player.gameMode.isIronMan && !player.isInDungeoneering && player.username != floorItem.ownerName)
            return false

        player.send(Location(floorItem.tile))
        builder.putInt(floorItem.id)
        builder.putInt(floorItem.amount)
        builder.put(floorItem.tile.z)
        builder.put(floorItem.revision.ordinal)
        return true
    }

}
