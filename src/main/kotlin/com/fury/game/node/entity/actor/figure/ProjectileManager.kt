package com.fury.game.node.entity.actor.figure

import com.fury.core.model.node.Node
import com.fury.core.model.node.entity.actor.figure.Projectile
import com.fury.game.network.packet.out.ProjectileSpawn
import com.fury.game.world.GameWorld
import com.fury.game.world.map.Position

object ProjectileManager {

    @JvmStatic
    fun send(projectile: Projectile) {
        if(projectile.source != null) {
            GameWorld.regions.getLocalPlayers(projectile.source!!).forEach { player ->
                player.send(ProjectileSpawn(projectile))
            }
        }
    }

    @JvmStatic
    fun getProjectileDelay(source: Position, target: Position): Int {
        return 1 + Math.ceil(source.getDistance(target) * 0.3).toInt()
    }

    fun getSpeedModifier(source: Position, target: Position): Int {
        return 46 + ((source as? Node)?.centredPosition ?: source).getDistance(target) * 5
    }

    @JvmStatic
    fun getDelay(attacker: Position, victim: Position, delay: Int, speed: Int): Double {
        val projectileSpeed = (delay.toDouble() + speed.toDouble() + attacker.getDistance(victim)) * 5//Speed the projectile is traveling
        return projectileSpeed * .02857//Delay of the hit
    }
}