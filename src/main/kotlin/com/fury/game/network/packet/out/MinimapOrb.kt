package com.fury.game.network.packet.out

import com.fury.core.model.node.entity.actor.figure.player.Player
import com.fury.game.network.packet.OutgoingPacket
import com.fury.game.network.packet.int

class MinimapOrb(private val orb: Int, private val active: Boolean) : OutgoingPacket(116) {

    override fun encode(player: Player): Boolean {
        builder.putShort(orb)
        builder.put(active.int)
        return true
    }

}