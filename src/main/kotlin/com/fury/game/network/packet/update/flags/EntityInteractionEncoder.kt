package com.fury.game.network.packet.update.flags

import com.fury.core.model.node.entity.actor.figure.Figure
import com.fury.core.model.node.entity.actor.figure.player.Player
import com.fury.game.network.packet.out.PlayerUpdate
import com.fury.game.network.packet.update.MaskEncoder
import com.fury.network.packet.ByteOrder
import com.fury.network.packet.PacketBuilder

object EntityInteractionEncoder : MaskEncoder {
    override fun encodable(player: Player, figure: Figure, state: PlayerUpdate.UpdateState?): Boolean {
        return true
    }

    override fun encode(player: Player, figure: Figure, builder: PacketBuilder) {
        val entity = figure.direction.interacting

        val value = when (entity) {
            null -> -1
            is Player -> entity.index + 32768
            else -> entity.index
        }

        if(figure is Player)
            builder.putShort(value, ByteOrder.LITTLE)
        else
            builder.putShort(value)
    }

}