package com.fury.game.network.packet.out

import com.fury.core.model.node.entity.actor.figure.player.Player
import com.fury.game.network.packet.OutgoingPacket

class Notes(private val notes: List<String>, private val colours: List<Int>) : OutgoingPacket(90) {

    override fun encode(player: Player): Boolean {
        builder.putShort(notes.size)
        for (i in notes.indices) {
            builder.putShort(colours[i])
            builder.putString(notes[i])
        }
        return true
    }

}