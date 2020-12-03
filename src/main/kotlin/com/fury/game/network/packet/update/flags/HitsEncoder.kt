package com.fury.game.network.packet.update.flags

import com.fury.core.model.node.entity.actor.figure.Figure
import com.fury.core.model.node.entity.actor.figure.player.Player
import com.fury.game.network.packet.out.PlayerUpdate
import com.fury.game.network.packet.update.MaskEncoder
import com.fury.network.packet.PacketBuilder
import com.fury.network.packet.ValueType

object HitsEncoder : MaskEncoder {
    override fun encodable(player: Player, figure: Figure, state: PlayerUpdate.UpdateState?): Boolean {
        return true
    }

    override fun encode(player: Player, figure: Figure, builder: PacketBuilder) {
        val count = figure.combat.hits.nextHits.size
        builder.put(count)
        if (count > 0) {
            figure.combat.hits.nextHits.forEach { hit ->
                builder.putShort(hit.damage, ValueType.A)
                if (figure is Player)
                    builder.put(hit.hitMask.ordinal)
                else
                    builder.put(hit.hitMask.ordinal, ValueType.C)
                builder.put(hit.combatIcon.ordinal)
                if (figure is Player)
                    builder.putShort(hit.absorb, ValueType.A)
                builder.putShort(figure.health.hitpoints, ValueType.A)
                builder.putShort(figure.maxConstitution, ValueType.A)
            }
        }
    }

}