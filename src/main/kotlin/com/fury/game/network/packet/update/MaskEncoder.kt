package com.fury.game.network.packet.update

import com.fury.core.model.node.entity.actor.figure.Figure
import com.fury.core.model.node.entity.actor.figure.player.Player
import com.fury.game.network.packet.out.PlayerUpdate
import com.fury.network.packet.PacketBuilder

interface MaskEncoder {
    fun encodable(player: Player, figure: Figure, state: PlayerUpdate.UpdateState? = null): Boolean

    fun encode(player: Player, figure: Figure, builder: PacketBuilder)
}