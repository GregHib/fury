package com.fury.game.network.packet.out

import com.fury.core.model.node.entity.actor.figure.player.Player
import com.fury.game.network.packet.OutgoingPacket
import com.fury.network.packet.ValueType

/**
 * Sends information about the player to the client.
 */
class PlayerDetails : OutgoingPacket(249) {

    override fun encode(player: Player): Boolean {
        builder.put(1, ValueType.A)
        builder.putShort(player.index)
        return true
    }

}