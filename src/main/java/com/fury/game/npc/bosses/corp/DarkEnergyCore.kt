package com.fury.game.npc.bosses.corp

import com.fury.core.model.node.entity.actor.figure.Figure
import com.fury.core.model.node.entity.actor.figure.Projectile
import com.fury.core.model.node.entity.actor.figure.mob.Mob
import com.fury.core.model.node.entity.actor.figure.player.Player
import com.fury.game.entity.character.combat.CombatIcon
import com.fury.game.entity.character.combat.Hit
import com.fury.game.entity.character.combat.effects.Effects
import com.fury.game.node.entity.actor.figure.ProjectileManager
import com.fury.game.node.entity.mob.combat.DarkEnergyCoreCombat
import com.fury.game.world.World
import com.fury.game.world.map.Position
import com.fury.game.world.update.flag.block.HitMask
import com.fury.util.Misc

class DarkEnergyCore(var beast: CorporealBeast) : Mob(8127, beast.centredPosition, true) {
    private var coreTarget: Figure? = null
    private var tile: Position? = null

    private var delay: Int = 0

    private val nextTarget: Figure?
        get() {
            val possibleTarget = beast.possibleTargets
            if (possibleTarget.isEmpty()) {
                deregister()
                beast.removeDarkEnergyCore()
                return null
            }
            return possibleTarget[Misc.random(possibleTarget.size)]
        }

    init {
        tile = beast.copyPosition()
        for (i in 0..9) {
            val t = Position(beast.copyPosition(), 10)
            if (World.isTileFree(t.x, t.y, t.z, 1)) {
                tile = t
                break
            }
        }
        setNPCHidden(true)
        val projectile = Projectile(this, tile!!, 1828, 40, 0, 1, 1, 20, 0)
        ProjectileManager.send(projectile)
        delay = projectile.getTickDelay()
        direction.face(tile)
    }

    override fun processNpc() {
        if (isDead || finished)
            return
        if (delay > 0) {
            delay--
            return
        }
        processTargetChange()
    }

    private fun processTargetChange() {
        if (effects.hasActiveEffect(Effects.STUNNED))
            return

        if (id == 8127) { //not hidden
            if (delay == -1) {
                setNPCHidden(true)
                val projectile = Projectile(this, tile!!, 1828, 30, 0, 1, 1, 20, 0)
                ProjectileManager.send(projectile)
                delay = projectile.getTickDelay()
                return
            }
            if (coreTarget == null || !isWithinDistance(coreTarget!!, 1)) {
                coreTarget = nextTarget
                if (coreTarget == null)
                    return
                animate(10393)
                tile = Position(coreTarget!!, 1)
                direction.face(tile)
                delay = -1
                return
            }
            applyCoreEffect()
        } else {
            setNPCHidden(false)
            moveTo(tile!!)
            delay = 3
        }
    }

    private fun setNPCHidden(hidden: Boolean) {
        if (id == (if (hidden) 1957 else 8127))
            return
        setTransformation(if (hidden) 1957 else 8127)
    }

    private fun applyCoreEffect() {
        val damage = Misc.random(50) + 150
        coreTarget!!.combat.applyHit(Hit(beast, damage, HitMask.RED, CombatIcon.MAGIC))
        beast.health.heal(damage)
        delay = if (effects.hasActiveEffect(Effects.POISON)) 10 else 3
        if (coreTarget!!.isPlayer()) {
            val player = coreTarget as Player?
            player!!.message("The dark core creature steals some life from you for its master.", true)
        }
    }
}
