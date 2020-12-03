package com.fury.game.network.packet.out

import com.fury.cache.Revision
import com.fury.core.model.node.entity.actor.figure.player.Player
import com.fury.game.network.packet.OutgoingPacket

class InterfaceAnimation(private val interfaceId: Int, private val animationId: Int, private val revision: Revision) : OutgoingPacket(200) {

    override fun encode(player: Player): Boolean {
        builder.putShort(interfaceId)
        builder.putShort(animationId)
        builder.put(revision.ordinal)
        return true
    }

}