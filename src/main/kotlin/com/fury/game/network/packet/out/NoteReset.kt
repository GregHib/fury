package com.fury.game.network.packet.out

import com.fury.core.model.node.entity.actor.figure.player.Player
import com.fury.game.network.packet.OutgoingPacket

class NoteReset : OutgoingPacket(90) {

    override fun encode(player: Player): Boolean {
        builder.putShort(0)
        return true
    }

}