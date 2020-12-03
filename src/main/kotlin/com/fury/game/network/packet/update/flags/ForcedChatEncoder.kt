package com.fury.game.network.packet.update.flags

import com.fury.core.model.node.entity.actor.figure.Figure
import com.fury.core.model.node.entity.actor.figure.player.Player
import com.fury.game.network.packet.out.PlayerUpdate
import com.fury.game.network.packet.update.MaskEncoder
import com.fury.network.packet.PacketBuilder

object ForcedChatEncoder : MaskEncoder {
    override fun encodable(player: Player, figure: Figure, state: PlayerUpdate.UpdateState?): Boolean {
        return figure.forcedChat.isPresent
    }

    override fun encode(player: Player, figure: Figure, builder: PacketBuilder) {
        if(figure.forcedChat.isPresent)
            builder.putString(figure.forcedChat.get())
    }

}