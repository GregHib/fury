package com.fury.game.network.packet.out

import com.fury.core.model.node.entity.actor.figure.player.Player
import com.fury.game.network.packet.OutgoingPacket
import com.fury.game.world.map.Position
import com.fury.network.packet.ValueType

class ClearLocalChunk(private val chunk: Position) : OutgoingPacket(64) {

    override fun encode(player: Player): Boolean {
        builder.put(chunk.x, ValueType.C)
        builder.put(chunk.y, ValueType.S)
        return true
    }

}