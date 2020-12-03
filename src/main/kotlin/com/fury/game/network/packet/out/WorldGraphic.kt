package com.fury.game.network.packet.out

import com.fury.core.model.node.entity.actor.figure.player.Player
import com.fury.game.network.packet.OutgoingPacket
import com.fury.game.world.map.Position
import com.fury.game.world.update.flag.block.graphic.Graphic

class WorldGraphic(val graphic: Graphic, val position: Position) : OutgoingPacket(4) {

    override fun encode(player: Player): Boolean {
        player.send(Location(position))
        builder.put(0)
        builder.putShort(graphic.id)
        builder.put(graphic.revision.ordinal)
        builder.put(graphic.height)
        builder.putShort(graphic.delay)
        return true
    }

}