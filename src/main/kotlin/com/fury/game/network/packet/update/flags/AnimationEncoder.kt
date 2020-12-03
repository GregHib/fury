package com.fury.game.network.packet.update.flags

import com.fury.cache.Revision
import com.fury.core.model.node.entity.actor.figure.Figure
import com.fury.core.model.node.entity.actor.figure.player.Player
import com.fury.game.network.packet.out.PlayerUpdate
import com.fury.game.network.packet.update.MaskEncoder
import com.fury.network.packet.ByteOrder
import com.fury.network.packet.PacketBuilder
import com.fury.network.packet.ValueType

object AnimationEncoder : MaskEncoder {
    override fun encodable(player: Player, figure: Figure, state: PlayerUpdate.UpdateState?): Boolean {
        return figure.animation != null
    }

    override fun encode(player: Player, figure: Figure, builder: PacketBuilder) {
        val animation = figure.animation
        if (animation != null) {
            builder.putShort(animation.id, ByteOrder.LITTLE)

            if(figure is Player) {
                val high = figure.transformation == null && (figure.equipment.hasRevision(Revision.PRE_RS3) || figure.equipment.hasRevision(Revision.RS3))
                builder.put(if (high && animation.revision == Revision.RS2) Revision.PRE_RS3.ordinal else animation.revision.ordinal)
                builder.put(animation.delay, ValueType.C)
            } else {
                builder.put(animation.delay)
            }
        }
    }

}