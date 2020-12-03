package com.fury.game.network.packet.out

import com.fury.core.model.node.entity.actor.figure.player.Player
import com.fury.game.network.packet.OutgoingPacket
import com.fury.game.world.map.Position
import com.fury.network.packet.ValueType

class Location(val position: Position) : OutgoingPacket(85) {

    override fun encode(player: Player): Boolean {
        val other = player.lastKnownRegion ?: return false

        builder.put(position.y - 8 * (other.getChunkY() - 6), ValueType.C)
        builder.put(position.x - 8 * (other.getChunkX() - 6), ValueType.C)
        return true
    }

}