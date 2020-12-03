package com.fury.game.network.packet.update.flags

import com.fury.core.model.node.entity.actor.figure.Figure
import com.fury.core.model.node.entity.actor.figure.player.Player
import com.fury.core.model.node.entity.actor.figure.mob.Mob
import com.fury.game.network.packet.out.PlayerUpdate
import com.fury.game.network.packet.update.MaskEncoder
import com.fury.network.packet.PacketBuilder

object CombatChangeEncoder : MaskEncoder {

    override fun encodable(player: Player, figure: Figure, state: PlayerUpdate.UpdateState?): Boolean {
        return figure is Mob && figure.customCombatLevel >= 0
    }

    override fun encode(player: Player, figure: Figure, builder: PacketBuilder) {
        if(figure is Mob)
            builder.putShort(figure.combatLevel)
    }

}