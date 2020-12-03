package com.fury.game.network.packet.out

import com.fury.game.entity.`object`.GameObject
import com.fury.core.model.node.entity.actor.figure.player.Player
import com.fury.game.network.packet.OutgoingPacket
import com.fury.network.packet.ValueType

class ObjectSpawn(private val gameObject: GameObject) : OutgoingPacket(151) {

    override fun encode(player: Player): Boolean {
        builder.put(gameObject.z, ValueType.A)
        builder.putInt(gameObject.id)
        builder.put(gameObject.revision.ordinal)
        builder.put(((gameObject.type shl 2) + (gameObject.direction and 3)).toByte().toInt(), ValueType.S)
        return true
    }

}
