package com.fury.game.network.packet.out

import com.fury.core.model.node.entity.actor.figure.mob.Mob
import com.fury.core.model.node.entity.actor.figure.player.Player
import com.fury.game.network.packet.OutgoingPacket
import com.fury.game.world.GameWorld
import com.fury.game.world.update.flag.Flag
import com.fury.network.packet.PacketBuilder

class MobUpdate : OutgoingPacket(65) {

    override fun encode(player: Player): Boolean {
        val update = PacketBuilder()
        builder.initializeAccess(PacketBuilder.AccessType.BIT)
        builder.putBits(8, player.viewport.localMobs.size)

        val npcIterator = player.viewport.localMobs.iterator()
        while (npcIterator.hasNext()) {
            val npc = npcIterator.next()

            if (npc.privateUser != null && npc.privateUser !== player)
                continue

            if (player.viewport.shouldUpdate(npc)) {
                updateMovement(npc)
                if (npc.isUpdateRequired)
                    appendUpdates(player, npc, update)
            } else {//Remove
                if (npc.atomicPlayerCount.decrementAndGet() < 0)
                    npc.atomicPlayerCount.set(0)
                npcIterator.remove()
                builder.putBit(true)
                builder.putBits(2, 3)
            }
        }

        for (npc in GameWorld.mobs.mobs) {//GameWorld.regions.getLocalNpcs(player)
            if (player.viewport.localMobs.size >= 255) {
                println("Mob update not sent: size >= 255" + ", Local size: " + player.viewport.localMobs.size + " location: " + player)
                break
            }

            if (npc.privateUser != null && npc.privateUser !== player)
                continue

            if (player.viewport.add(npc)) {
                add(player, npc, builder)
                if (npc.isUpdateRequired)
                    appendUpdates(player, npc, update)

                if (npc.atomicPlayerCount.incrementAndGet() < 0)
                    npc.atomicPlayerCount.set(0)
            }
        }

        if (update.buffer().writerIndex() > 0) {
            builder.putBits(14, 16383)
            builder.initializeAccess(PacketBuilder.AccessType.BYTE)
            builder.writeBuffer(update.buffer())
        } else {
            builder.initializeAccess(PacketBuilder.AccessType.BYTE)
        }
        return true
    }

    private fun add(player: Player, mob: Mob, builder: PacketBuilder) {
        builder.putBits(14, mob.index)
        builder.putBits(5, mob.y - player.y)
        builder.putBits(5, mob.x - player.x)
        builder.putBit(false)
        builder.putBits(16, mob.id)
        builder.putBits(4, mob.revision.ordinal)
        builder.putBit(mob.isUpdateRequired)
    }

    private fun updateMovement(mob: Mob) {
        if (mob.movement.walkingDirection.toInteger() == -1) {
            builder.putBit(mob.isUpdateRequired)
            if (mob.isUpdateRequired)
                builder.putBits(2, 0)
        } else {
            builder.putBit(true)
            builder.putBits(2, if (mob.movement.runningDirection.toInteger() == -1) 1 else 2)
            builder.putBits(3, mob.movement.walkingDirection.toInteger())
            if (mob.movement.runningDirection.toInteger() != -1)
                builder.putBits(3, mob.movement.runningDirection.toInteger())
            builder.putBit(mob.isUpdateRequired)
        }
    }

    private fun appendUpdates(player: Player, mob: Mob, block: PacketBuilder) {
        var mask = 0
        val flags = mob.updateFlags

        Flag.mobFlags
                .filter { it.mobMask != -1 && flags.contains(it) && it.encoder.encodable(player, mob) }
                .forEach {mask = mask or it.mobMask }

        block.putShort(mask)

        Flag.mobFlags
                .filter { it.mobMask != -1 && flags.contains(it) && it.encoder.encodable(player, mob) }
                .forEach { it.encoder.encode(player, mob, block) }
    }
}