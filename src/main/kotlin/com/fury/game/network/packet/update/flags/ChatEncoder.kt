package com.fury.game.network.packet.update.flags

import com.fury.core.model.node.entity.actor.figure.Figure
import com.fury.core.model.node.entity.actor.figure.player.Player
import com.fury.game.network.packet.out.PlayerUpdate
import com.fury.game.network.packet.update.MaskEncoder
import com.fury.network.packet.ByteOrder
import com.fury.network.packet.PacketBuilder
import com.fury.network.packet.ValueType

object ChatEncoder : MaskEncoder {
    override fun encodable(player: Player, figure: Figure, state: PlayerUpdate.UpdateState?): Boolean {
        if(figure is Player) {
            return state != PlayerUpdate.UpdateState.UPDATE_SELF && !player.relations.ignoreList.contains(figure.longUsername)
        }
        return false
    }

    override fun encode(player: Player, figure: Figure, builder: PacketBuilder) {
        if(figure is Player) {
            val message = figure.chatMessages.get()
            val bytes = message.text
            builder.putShort(message.colour and 0xff shl 8 or (message.effects and 0xff), ByteOrder.LITTLE)
            builder.put(figure.rightsId)
            builder.put(if(figure.gameMode.isIronMan) 1 else 0)
            builder.put(bytes.size, ValueType.C)
            builder.putBytesReverse(bytes)
        }
    }

}