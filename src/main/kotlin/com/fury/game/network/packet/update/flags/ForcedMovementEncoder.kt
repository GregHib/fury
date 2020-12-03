package com.fury.game.network.packet.update.flags

import com.fury.core.model.node.entity.actor.figure.Figure
import com.fury.core.model.node.entity.actor.figure.player.Player
import com.fury.game.network.packet.out.PlayerUpdate
import com.fury.game.network.packet.update.MaskEncoder
import com.fury.network.SessionState
import com.fury.network.packet.PacketBuilder

object ForcedMovementEncoder : MaskEncoder {
    override fun encodable(player: Player, figure: Figure, state: PlayerUpdate.UpdateState?): Boolean {
        return figure.forceMovement != null
    }

    override fun encode(player: Player, figure: Figure, builder: PacketBuilder) {
        if(player.session.get().state != SessionState.LOGGED_IN)
            return

        val region = player.lastKnownRegion
        val forceMovement = figure.forceMovement
        if (region != null && forceMovement != null) {
            builder.put(figure.getLocalX(region) + forceMovement.firstTile.x - figure.x)
            builder.put(figure.getLocalY(region) + forceMovement.firstTile.y - figure.y)
            builder.put(if (forceMovement.secondTile == null) 0 else figure.getLocalX(region) + forceMovement.secondTile.x - figure.x)
            builder.put(if (forceMovement.secondTile == null) 0 else figure.getLocalY(region) + forceMovement.secondTile.y - figure.y)
            builder.putShort(forceMovement.firstTileDelay * 30)
            builder.putShort(if (forceMovement.secondTile == null) 0 else forceMovement.secondTileDelay * 30)
            builder.put(forceMovement.direction)
        }
    }

}