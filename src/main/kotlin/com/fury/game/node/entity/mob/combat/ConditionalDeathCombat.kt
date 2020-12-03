package com.fury.game.node.entity.mob.combat

import com.fury.core.model.node.entity.actor.figure.Figure
import com.fury.core.model.node.entity.actor.figure.player.Player
import com.fury.game.container.impl.equip.Slot
import com.fury.game.entity.character.combat.CombatIcon
import com.fury.game.entity.character.combat.Hit
import com.fury.game.node.entity.actor.figure.mob.MobCombat
import com.fury.game.npc.slayer.ConditionalDeath

class ConditionalDeathCombat(private val mob: ConditionalDeath) : MobCombat(mob) {
    override fun processHit(hit: Hit) {
        super.processHit(hit)
        if (mob.health.hitpoints < mob.maxConstitution * 0.1) {
            val source = hit.source
            if (source.isPlayer()) {
                val player = source as Player
                if (!mob.lastLegs) {
                    mob.lastLegs = true
                    player.message("The ${mob.name} is on its last legs! Finish it quickly!")
                }
                var canContinue = player.slayerManager.learnt[4] && mob.checkInventory
                if (!canContinue) {
                    for (requiredItem in mob.requiredItem) {
                        if (player.equipment.get(Slot.WEAPON).id == requiredItem || player.equipment.get(Slot.HANDS).id == requiredItem) {
                            canContinue = true
                            break
                        }
                    }
                    if (mob.name.equals("Turoth", ignoreCase = true)) {
                        val ammoId = player.equipment.get(Slot.ARROWS).id
                        if (hit.combatIcon == CombatIcon.RANGED && (ammoId == 13280 || ammoId == 4160))
                            canContinue = true
                    }
                }
                if (canContinue && mob.useHammer(player))
                    return
            }
        }
    }

    override fun sendDeath(source: Figure?) {
        mob.health.hitpoints = 1
    }

    fun finishHim(source: Figure?) {
        super.sendDeath(source)
    }
}