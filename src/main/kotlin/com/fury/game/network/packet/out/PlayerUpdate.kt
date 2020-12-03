package com.fury.game.network.packet.out

import com.fury.core.model.node.entity.actor.figure.player.Player
import com.fury.game.network.packet.OutgoingPacket
import com.fury.game.world.GameWorld
import com.fury.game.world.update.flag.Flag
import com.fury.network.packet.PacketBuilder

class PlayerUpdate : OutgoingPacket(81) {

    companion object {
        private const val maxPlayersPerCycle = 25
    }

    override fun encode(player: Player): Boolean {
        val update = PacketBuilder()
        builder.initializeAccess(PacketBuilder.AccessType.BIT)

        updateMovement(player, builder)
        if (player.isUpdateRequired)
            appendUpdates(player, update, player, UpdateState.UPDATE_SELF)

        builder.putBits(8, player.viewport.localPlayers.size)

        val playerIterator = player.viewport.localPlayers.iterator()
        while (playerIterator.hasNext()) {
            val otherPlayer = playerIterator.next()

            if (player.viewport.shouldUpdate(otherPlayer)) {
                updateMovement(otherPlayer, builder, false)
                if (otherPlayer.isUpdateRequired)
                    appendUpdates(player, update, otherPlayer, UpdateState.UPDATE_LOCAL)
            } else {
                playerIterator.remove()
                builder.putBit(true)
                builder.putBits(2, 3)
            }
        }

        var playersAdded = 0
        for (otherPlayer in GameWorld.players) {//GameWorld.regions.getLocalPlayers(player)
            if (player.viewport.localPlayers.size >= 79 || playersAdded > maxPlayersPerCycle)
                break

            if (player.viewport.add(otherPlayer)) {
                addPlayer(player, otherPlayer, builder)
                appendUpdates(player, update, otherPlayer, UpdateState.ADD_LOCAL)
                playersAdded++
            }
        }

        if (update.buffer().writerIndex() > 0) {
            builder.putBits(11, 2047)
            builder.initializeAccess(PacketBuilder.AccessType.BYTE)
            builder.putBytes(update.buffer())
        } else {
            builder.initializeAccess(PacketBuilder.AccessType.BYTE)
        }
        return true
    }

    private fun addPlayer(player: Player, target: Player, builder: PacketBuilder) {
        builder.putBits(11, target.index)
        builder.putBit(true)
        builder.putBit(true)
        val yDiff = target.y - player.y
        val xDiff = target.x - player.x
        builder.putBits(5, yDiff)
        builder.putBits(5, xDiff)
    }

    private fun updateMovement(player: Player, builder: PacketBuilder, local: Boolean = true) {
        if (local && (player.isNeedsPlacement || player.isChangingRegion)) {//is teleporting?
            builder.putBit(true)//Update required
            builder.putBits(2, 3)//Teleported
            builder.putBits(2, player.z)
            builder.putBit(player.isResetMovementQueue)//Should client discard the walking queue?
            builder.putBit(player.isUpdateRequired)

            //Delta position
            builder.putBits(7, player.getLocalY(player.lastKnownRegion!!))
            builder.putBits(7, player.getLocalX(player.lastKnownRegion!!))
        } else if (player.movement.runningDirection.toInteger() != -1) {//Running
            builder.putBit(true)//Update required
            builder.putBits(2, 2)//Running
            builder.putBits(3, player.movement.walkingDirection.toInteger())//Walk direction
            builder.putBits(3, player.movement.runningDirection.toInteger())//Run direction
            builder.putBit(player.isUpdateRequired)
        } else if (player.movement.walkingDirection.toInteger() != -1) {//Walking
            builder.putBit(true)//Update required
            builder.putBits(2, 1)//Walk only
            builder.putBits(3, player.movement.walkingDirection.toInteger())//Walking direction
            builder.putBit(player.isUpdateRequired)
        } else {//No movement
            if (player.isUpdateRequired) {
                builder.putBit(true)//Update required
                builder.putBits(2, 0)//But no movement
            } else
                builder.putBit(false)//No change
        }
    }

    private fun appendUpdates(player: Player, builder: PacketBuilder, target: Player, state: UpdateState) {
        if (!target.isUpdateRequired && state != UpdateState.ADD_LOCAL)
            return

        val flags = target.updateFlags

        var mask = 0
        Flag.playerFlags
                .filter { it.playerMask != -1 && (if(it != Flag.APPEARANCE) flags.contains(it) else true) && it.encoder.encodable(player, target, state) }
                .forEach { mask = mask or it.playerMask }

        if (mask >= 0x100) {
            mask = mask or 0x40
            builder.put(mask and 0xFF)
            builder.put(mask shr 8)
        } else {
            builder.put(mask)
        }

        Flag.playerFlags
                .filter { it.playerMask != -1 && (if(it != Flag.APPEARANCE) flags.contains(it) else true) && it.encoder.encodable(player, target, state) }
                .forEach { it.encoder.encode(player, target, builder) }
    }

    enum class UpdateState {
        //updateAppearance, noChat
        UPDATE_SELF,//false true
        UPDATE_LOCAL,//false false
        ADD_LOCAL//true false
    }

}