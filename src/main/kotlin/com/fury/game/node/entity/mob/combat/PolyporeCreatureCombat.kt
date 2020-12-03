package com.fury.game.node.entity.mob.combat

import com.fury.game.entity.character.combat.CombatIcon
import com.fury.game.entity.character.combat.Hit
import com.fury.game.node.entity.actor.figure.mob.MobCombat
import com.fury.game.npc.slayer.polypore.PolyporeCreature

class PolyporeCreatureCombat(private val mob: PolyporeCreature): MobCombat(mob) {
    override fun handleHit(hit: Hit) {
        if (hit.weapon != null && hit.weapon.id == 25202) {
            mob.sendDeath(hit.source)
            return
        }

        if (hit.combatIcon == CombatIcon.MELEE || hit.combatIcon == CombatIcon.RANGED)
            hit.damage = hit.damage / 5
        super.handleHit(hit)
    }
}