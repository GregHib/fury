package com.fury.game.node.entity.mob.combat

import com.fury.core.model.node.entity.actor.figure.player.Player
import com.fury.game.entity.character.combat.CombatIcon
import com.fury.game.entity.character.combat.Hit
import com.fury.game.node.entity.actor.figure.mob.MobCombat
import com.fury.game.npc.bosses.rfd.GelatinnothMother

class GelatinnothMotherCombat(private val mob: GelatinnothMother): MobCombat(mob) {
    override fun handleHit(hit: Hit) {
        if (hit.weapon != null && hit.weapon.id == 25202) {
            super.handleHit(hit)
            return
        }
        var correctType = false
        when (mob.type) {
            0//WHITE
            -> if (hit.source.isPlayer()) {
                val player = hit.source as Player
                if (player.previousCastSpell != null)
                    correctType = hit.combatIcon == CombatIcon.MAGIC && player.previousCastSpell!!.name.toLowerCase().contains("air")
            }
            1//ORANGE
            -> correctType = hit.combatIcon == CombatIcon.MELEE
            2//BLUE
            -> if (hit.source.isPlayer()) {
                val player = hit.source as Player
                if (player.previousCastSpell != null)
                    correctType = hit.combatIcon == CombatIcon.MAGIC && player.previousCastSpell!!.name.toLowerCase().contains("water")
            }
            3//RED
            -> if (hit.source.isPlayer()) {
                val player = hit.source as Player
                if (player.previousCastSpell != null)
                    correctType = hit.combatIcon == CombatIcon.MAGIC && player.previousCastSpell!!.name.toLowerCase().contains("fire")
            }
            4//GREEN
            -> correctType = hit.combatIcon == CombatIcon.RANGED
            5//BROWN
            -> if (hit.source.isPlayer()) {
                val player = hit.source as Player
                if (player.previousCastSpell != null)
                    correctType = hit.combatIcon == CombatIcon.MAGIC && player.previousCastSpell!!.name.toLowerCase().contains("earth")
            }
        }
        if (!correctType)
            hit.damage = 0
        super.handleHit(hit)
    }
}