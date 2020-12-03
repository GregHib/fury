package com.fury.game.network.packet.update.flags

import com.fury.core.model.node.entity.actor.figure.Figure
import com.fury.core.model.node.entity.actor.figure.player.Player
import com.fury.game.network.packet.out.PlayerUpdate
import com.fury.game.network.packet.update.MaskEncoder
import com.fury.network.packet.ByteOrder
import com.fury.network.packet.PacketBuilder
import com.fury.network.packet.ValueType

object TransformEncoder : MaskEncoder {
    override fun encodable(player: Player, figure: Figure, state: PlayerUpdate.UpdateState?): Boolean {
        return figure.transformation != null
    }

    override fun encode(player: Player, figure: Figure, builder: PacketBuilder) {
        val transformation = figure.transformation
        if (transformation != null) {
            builder.putShort(transformation.id, ValueType.A, ByteOrder.LITTLE)
            builder.put(transformation.revision.ordinal)
        }
    }

}