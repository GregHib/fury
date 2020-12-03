package com.fury.game.network.packet.out

import com.fury.core.model.node.entity.actor.figure.Figure
import com.fury.core.model.node.entity.actor.figure.Projectile
import com.fury.core.model.node.entity.actor.figure.player.Player
import com.fury.game.network.packet.OutgoingPacket

class ProjectileSpawn(private val projectile: Projectile) : OutgoingPacket(117) {

    override fun encode(player: Player): Boolean {
        val start = projectile.source
        val end = projectile.target
        if(start != null && end != null) {
            player.send(Location(start))
            val offsetX = end.x - start.x
            val offsetY = end.y - start.y
            builder.put(0)
            builder.put(offsetX)
            builder.put(offsetY)
            builder.putShort(if (end is Figure) if (end is Player) -end.index - 1 else end.index + 1 else 0)
            builder.putShort(projectile.id)
            builder.put(projectile.revision.ordinal)
            builder.put(projectile.startHeight)
            builder.put(projectile.endHeight)
            builder.putShort(projectile.delay)
            builder.putShort(projectile.speed)
            builder.put(projectile.angle)
            builder.put((if (start is Figure) start.getSize() * 64 else 0) + projectile.offset * 64)
        }
        return true
    }

}