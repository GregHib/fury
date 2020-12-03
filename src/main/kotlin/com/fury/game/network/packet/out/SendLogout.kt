package com.fury.game.network.packet.out

import com.fury.core.model.node.entity.actor.figure.player.Player
import com.fury.game.network.packet.OutgoingPacket

class SendLogout : OutgoingPacket(109) {

    override fun encode(player: Player): Boolean {
        return true
    }

}