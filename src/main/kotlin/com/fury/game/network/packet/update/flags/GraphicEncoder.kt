package com.fury.game.network.packet.update.flags

import com.fury.core.model.node.entity.actor.figure.Figure
import com.fury.core.model.node.entity.actor.figure.player.Player
import com.fury.game.network.packet.out.PlayerUpdate
import com.fury.game.network.packet.update.MaskEncoder
import com.fury.network.packet.ByteOrder
import com.fury.network.packet.PacketBuilder

object GraphicEncoder : MaskEncoder {
    override fun encodable(player: Player, figure: Figure, state: PlayerUpdate.UpdateState?): Boolean {
        return figure.graphic != null
    }

    override fun encode(player: Player, figure: Figure, builder: PacketBuilder) {
        val graphic = figure.graphic
        if (graphic != null) {
            if (figure is Player)
                builder.putShort(graphic.id, ByteOrder.LITTLE)
            else
                builder.putShort(graphic.id)
            builder.put(graphic.revision.ordinal)
            builder.putInt((graphic.height shl 16) + (graphic.delay and 0xffff))
        }
    }

}