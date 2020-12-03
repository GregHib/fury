package com.fury.game.network.packet.out

import com.fury.core.model.node.entity.actor.figure.player.Player
import com.fury.game.network.packet.OutgoingPacket
import com.fury.network.packet.ValueType

/**
 * Sends the map region a player is located in and also
 * sets the player's first step position of said region as their
 * {@code lastKnownRegion}.
 */
class MapRegion : OutgoingPacket(73) {

    override fun encode(player: Player): Boolean {
        player.isChangingRegion = true
        player.isAllowRegionChangePacket = true
        player.lastKnownRegion = player.copyPosition()
        builder.putShort(player.getChunkX(), ValueType.A)
        builder.putShort(player.getChunkY())
        return true
    }

}