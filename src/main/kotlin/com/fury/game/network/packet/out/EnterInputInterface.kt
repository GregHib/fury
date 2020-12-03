package com.fury.game.network.packet.out

import com.fury.core.model.node.entity.actor.figure.player.Player
import com.fury.game.network.packet.OutgoingPacket

class EnterInputInterface(private val title: String) : OutgoingPacket(187) {

    override fun encode(player: Player): Boolean {
        builder.putString(title)
        return true
    }

}