package com.fury.game.node.entity.actor.figure.player

import com.fury.core.model.node.entity.actor.figure.player.Player
import com.fury.game.network.packet.out.ChatboxMessage
import com.fury.util.FontUtils

class Messenger(private val player: Player) {
    fun send(message: String, filter: Boolean = false) {
        if (player.session.isPresent)
            player.send(ChatboxMessage(message, if(filter) 109 else 0))
    }

    fun send(message: String, colour: Int, filter: Boolean = false) {
        send(FontUtils.add(message, colour), filter)
    }
}

/*
fun Player.message(message: String) {
    if (session.isPresent)
        send(ChatboxMessage(message))
}*/
