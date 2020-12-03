package com.fury.game.network.packet.out

import com.fury.game.entity.`object`.GameObject
import com.fury.core.model.node.entity.actor.figure.player.Player
import com.fury.game.network.packet.OutgoingPacket
import com.fury.network.packet.ValueType

class ObjectRemoval(private val gameObject: GameObject) : OutgoingPacket(101) {

    override fun encode(player: Player): Boolean {
        builder.put((gameObject.type shl 2) + (gameObject.direction and 3), ValueType.C)
//        builder.put(gameObject.z)
        builder.put(0)
        builder.put(gameObject.revision.ordinal)
        return true
    }

}
