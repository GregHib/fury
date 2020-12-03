package com.fury.game.network.packet.update.flags

import com.fury.core.model.node.entity.actor.figure.Figure
import com.fury.core.model.node.entity.actor.figure.player.Player
import com.fury.game.network.packet.out.PlayerUpdate
import com.fury.game.network.packet.update.MaskEncoder
import com.fury.network.packet.ByteOrder
import com.fury.network.packet.PacketBuilder
import com.fury.network.packet.ValueType

object FacePositionEncoder : MaskEncoder {
    override fun encodable(player: Player, figure: Figure, state: PlayerUpdate.UpdateState?): Boolean {
        return true
    }

    override fun encode(player: Player, figure: Figure, builder: PacketBuilder) {
        if(figure.direction.getPositionToFace() == null) {
            val x = figure.x + figure.movement.lastDirection.deltaX
            val y = figure.y + figure.movement.lastDirection.deltaY
            if(figure is Player)
                builder.putShort(x * 2 + 1, ValueType.A, ByteOrder.LITTLE)
            else
                builder.putShort(x * 2 + 1, ByteOrder.LITTLE)
            builder.putShort(y * 2 + 1, ByteOrder.LITTLE)
        } else {
            val position = figure.direction.getPositionToFace()
            val x = position?.x ?: 0
            val y = position?.y ?: 0
            val sX = position?.sizeX ?: 1
            val sY = position?.sizeY ?: 1
            if(figure is Player)
                builder.putShort(x * 2 + sX, ValueType.A, ByteOrder.LITTLE)
            else
                builder.putShort(x * 2 + sX, ByteOrder.LITTLE)
            builder.putShort(y * 2 + sY, ByteOrder.LITTLE)
        }
    }

}