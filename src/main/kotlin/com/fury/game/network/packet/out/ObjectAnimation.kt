package com.fury.game.network.packet.out

import com.fury.game.entity.`object`.GameObject
import com.fury.core.model.node.entity.actor.figure.player.Player
import com.fury.game.network.packet.OutgoingPacket
import com.fury.game.world.update.flag.block.Animation
import com.fury.network.packet.ValueType

class ObjectAnimation(val obj: GameObject, val anim: Animation) : OutgoingPacket(160) {

    override fun encode(player: Player): Boolean {
        player.send(Location(obj))
        builder.put(0, ValueType.S)
        builder.put((obj.type shl 2) + (obj.direction and 3), ValueType.S)
        builder.putShort(anim.id, ValueType.A)
        return true
    }

}