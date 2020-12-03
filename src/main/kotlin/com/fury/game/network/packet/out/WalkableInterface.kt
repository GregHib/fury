package com.fury.game.network.packet.out

import com.fury.core.model.node.entity.actor.figure.player.Player
import com.fury.game.network.packet.OutgoingPacket

class WalkableInterface(private val id: Int) : OutgoingPacket(208) {

    override fun encode(player: Player): Boolean {
        player.walkableInterfaceId = id
        builder.putShort(id)
        return true
    }

}