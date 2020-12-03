package com.fury.game.npc.bosses.corp

import com.fury.core.model.node.entity.actor.figure.mob.Mob
import com.fury.core.model.node.entity.actor.figure.player.Player
import com.fury.game.container.impl.equip.Slot
import com.fury.game.entity.character.combat.CombatIcon
import com.fury.game.entity.character.combat.Hit
import com.fury.game.node.entity.mob.combat.CorporealBeastCombat
import com.fury.game.world.map.Position

class CorporealBeast(id: Int, tile: Position, spawned: Boolean) : Mob(id, tile, spawned) {

    var core: DarkEnergyCore? = null

    override val magePrayerMultiplier: Double
        get() = 0.66

    override val meleePrayerMultiplier: Double
        get() = 0.66

    override val isStunImmune: Boolean
        get() = true

    override val isBoundImmune: Boolean
        get() = true

    init {
        lureDelay = 3000
        blockDeflections = true
        forceTargetDistance = 80
        //        setIntelligentRouteFinder(true);
    }

    fun spawnDarkEnergyCore() {
        if (core != null)
            return
        core = DarkEnergyCore(this)
    }

    fun removeDarkEnergyCore() {
        if (core == null)
            return
        core!!.deregister()
        core = null
    }

    override fun processNpc() {
        super.processNpc()
        if (isDead)
            return
        checkReset()
    }

    fun checkReset() {
        val maxhp = maxConstitution
        if (maxhp > health.hitpoints && !combat.isInCombat() && possibleTargets.isEmpty())
            health.hitpoints = maxhp
    }

    fun reduceHit(hit: Hit) {
        if (hit.source != null && !hit.source.isPlayer() || hit.combatIcon != CombatIcon.MELEE && hit.combatIcon != CombatIcon.RANGED && hit.combatIcon != CombatIcon.MAGIC || hit.weapon != null && hit.weapon.id == 25202)
            return
        val from = hit.source as Player
        val weapon = from.equipment.get(Slot.WEAPON)
        val name = weapon?.name?.toLowerCase() ?: "null"
        if (hit.combatIcon != CombatIcon.MELEE || !name.contains("spear"))
            hit.damage = hit.damage / 2

    }
}
