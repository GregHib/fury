package com.fury.game.network.packet.out

import com.fury.core.model.node.entity.actor.figure.player.Player
import com.fury.game.network.packet.OutgoingPacket

class EnterAmountInterface(private val title: String) : OutgoingPacket(27) {

    override fun encode(player: Player): Boolean {
        builder.putString(title)
        return true
    }

}