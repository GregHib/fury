package com.fury.game.network.packet.out

import com.fury.core.model.node.entity.actor.figure.player.Player
import com.fury.game.network.packet.OutgoingPacket
import com.fury.util.FontUtils

class InterfaceString(private val id: Int, private val string: String) : OutgoingPacket(126) {

    constructor(id: Int, string:String, colour: Int) : this(id, FontUtils.add(string, colour))

    override fun encode(player: Player): Boolean {
        builder.putString(string)
        builder.putShort(id)
        return true
    }

}