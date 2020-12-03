package com.fury.game.network.packet

import com.fury.core.model.node.entity.actor.figure.player.Player
import com.fury.network.packet.PacketBuilder

abstract class OutgoingPacket(private val opcode: Int) {
    protected val builder = PacketBuilder(opcode)

    protected abstract fun encode(player: Player): Boolean

    fun execute(player: Player) {
        if (!encode(player))
            return

        if(player.session.isPresent)
            player.session.get().write(builder)
    }
}

val Boolean.int get() = if (this) 1 else 0