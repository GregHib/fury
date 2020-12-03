package com.fury.game.node.entity.actor.figure

import com.fury.core.model.node.entity.actor.figure.Figure
import com.fury.core.model.node.entity.actor.figure.mob.Mob
import com.fury.core.model.node.entity.actor.figure.player.Player
import com.fury.game.GameSettings
import com.fury.game.content.combat.CombatType
import com.fury.game.entity.character.combat.Hit
import com.fury.game.world.map.Position
import com.fury.game.world.map.path.RouteStrategy
import com.fury.util.Misc
import com.fury.util.Utils

open class FigureCombat(private val figure: Figure) {
    var findTargetDelay: Long = 0
    val hits = HitsManager(figure)
    var attackedBy: Figure? = null
    var attackedByDelay: Long = 0
        set(value) {//TODO check that this is working
            field = if(value > 0) {
                if (figure is Player && figure.interfaceId > 0)
                    figure.packetSender.sendInterfaceRemoval()
                Utils.currentTimeMillis() + value
            } else
                value
        }

    var freezeDelay: Long = 0
    private var frozenBlockedDelay: Long = 0
    var attackingDelay: Long = 0
        set(value) {
            field = if(value > 0) value + Utils.currentTimeMillis() else value
        }

    open val attackSpeed: Int = 4

    open val maxConstitution: Int = 100

    init {
        hits.init()
    }

    @JvmOverloads
    fun addFreezeDelay(time: Long, entangleMessage: Boolean = false, ignoreImmunity: Boolean = false) {
        if (!ignoreImmunity && figure.isStunImmune || frozenBlockedDelay >= Utils.currentTimeMillis())
            return
        val currentTime = Utils.currentTimeMillis()
        if (currentTime > freezeDelay) {
            figure.movement.reset()
            freezeDelay = time + currentTime
            if (figure is Player && entangleMessage)
                figure.message("You have been frozen!")
            frozenBlockedDelay = freezeDelay + 3000
        }
    }

    fun isUnderAttack(): Boolean {
        return attackedBy != null && attackedByDelay > System.currentTimeMillis()
    }

    fun isInCombat(): Boolean {
        return (figure as? Mob)?.mobCombat?.underCombat() ?: (attackedByDelay + GameSettings.COMBAT_DELAY >= Misc.currentTimeMillis() || attackingDelay + GameSettings.COMBAT_DELAY >= Misc.currentTimeMillis())
    }

    fun resetCombat() {
        attackedBy = null
        attackedByDelay = 0
        freezeDelay = 0
    }

    fun clippedProjectile(position: Position, checkClose: Boolean): Boolean {
        if (position is Figure) {
            var me: Position = figure
            var tile: Position = position
            if (position is Mob) {
                tile = position.centredPosition
            } else if (figure is Mob) {
                me = figure.centredPosition
            }
            return RouteStrategy.clippedProjectile(figure, tile, checkClose, 1) || RouteStrategy.clippedProjectile(position, me, checkClose, 1)
        }
        return RouteStrategy.clippedProjectile(position, figure, checkClose, 1)
    }

    open fun applyHit(hit: Hit) {
        if (figure.isDead)
            return
        hits.addHit(hit)
        handleHit(hit)
    }

    open fun processHit(hit: Hit) {
        if (figure.isDead)
            return
        figure.health.removeConstitution(hit)
        hits.showHit(hit)
    }

    open fun negateDamage(source: Figure, type: CombatType, damage: Int): Boolean {
        return false
    }

    open fun modifyDamage(source: Figure, type: CombatType, damage: Int): Int {
        return damage
    }

    open fun afterEffects(source: Figure, type: CombatType, damage: Int) {

    }

    open fun handleHit(hit: Hit) {

    }


    open fun sendDeath(source: Figure?) {

    }



























}