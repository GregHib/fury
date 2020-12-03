package com.fury.game.node.entity

import com.fury.core.model.node.entity.actor.figure.Figure
import com.fury.core.model.node.entity.actor.figure.player.Player
import com.fury.core.task.Task

abstract class EntityDeath<out T : Figure>(val entity: T, delay: Int) : Task(true, delay) {

    var counter: Int = 0
    val killer = entity.combat.hits.getMostDamageReceivedSourcePlayer()

    abstract fun preDeath(killer: Figure?)

    abstract fun death(killer: Figure?)

    abstract fun postDeath(killer: Figure?)

    override fun onSchedule() {
        entity.movement.reset()
        entity.movement.lock()
    }

    override fun run() {
        when (++counter) {
            1 -> if (killer != null && entity !is Player /*&& killer!!.getCombat().isAttacking(entity)*/) {
                if (!entity.isDead) {
                    stop()
                    return
                }
            }
            2 -> preDeath(killer)
            3 -> {
                death(killer)
                postDeath(killer)
                killer?.combat?.hits?.removeDamage(entity)
            }
            4 -> stop()
        }
    }

    override fun onCancel(logout: Boolean) {
        entity.movement.unlock()
    }
}