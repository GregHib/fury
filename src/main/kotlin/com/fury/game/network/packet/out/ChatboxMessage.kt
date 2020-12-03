package com.fury.game.network.packet.out

import com.fury.core.model.node.entity.actor.figure.player.Player
import com.fury.game.network.packet.OutgoingPacket
import com.fury.util.Misc

class ChatboxMessage @JvmOverloads constructor(val message: String, val type: Int = 0, val target: Player? = null) : OutgoingPacket(253) {

    override fun encode(player: Player): Boolean {
        if(message.isEmpty())
            return false

        var maskData = 0
        if (target != null)
            maskData = maskData or 0x1

        builder.putSmart(type)
        builder.put(maskData)
        if (maskData and 0x1 != 0)
            builder.putString(Misc.formatPlayerNameForDisplay(player.username))
        builder.putString(message)
        return true
    }

}