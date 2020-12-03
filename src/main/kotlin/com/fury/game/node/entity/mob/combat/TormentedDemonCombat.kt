package com.fury.game.node.entity.mob.combat

import com.fury.core.model.node.entity.actor.figure.Figure
import com.fury.core.model.node.entity.actor.figure.player.Player
import com.fury.game.container.impl.equip.Slot
import com.fury.game.entity.character.combat.CombatIcon
import com.fury.game.entity.character.combat.Hit
import com.fury.game.node.entity.actor.figure.mob.MobCombat
import com.fury.game.node.entity.actor.figure.mob.MobDeath
import com.fury.game.npc.bosses.TormentedDemon
import com.fury.game.world.GameWorld
import com.fury.util.Utils

class TormentedDemonCombat(private val mob: TormentedDemon): MobCombat(mob) {
    override fun handleHit(hit: Hit) {
        var type = 0
        if (hit.source.isPlayer()) {// darklight
            val player = hit.source as Player
            if ((player.equipment.get(Slot.WEAPON).id == 6746 || player.equipment.get(Slot.WEAPON).id == 2402) && hit.combatIcon == CombatIcon.MELEE && hit.damage > 0) {
                mob.shieldTimer = 60
                player.message("The demon is temporarily weakened by your weapon.")
            }
        }

        if (hit.weapon != null && hit.weapon.id == 25202) {
            mob.sendDeath(hit.source)
            return
        }

        if (mob.shieldTimer <= 0) {// 75% of damage is absorbed
            hit.damage = (hit.damage * 0.25).toInt()
            mob.graphic(1885)
        }
        if (hit.combatIcon == CombatIcon.MELEE) {
            if (mob.demonPrayer[0]) {
                hit.damage = 0
            } else {
                mob.cachedDamage[0] += hit.damage
            }
        } else if (hit.combatIcon == CombatIcon.MELEE) {
            type = 1
            if (mob.demonPrayer[1]) {
                hit.damage = 0
            } else {
                mob.cachedDamage[1] += hit.damage
            }
        } else if (hit.combatIcon == CombatIcon.RANGED) {
            type = 2
            if (mob.demonPrayer[2]) {
                hit.damage = 0
            } else {
                mob.cachedDamage[2] += hit.damage
            }
        } else if (hit.combatIcon == CombatIcon.BLOCK) {
            mob.cachedDamage[type] += 20
        } else {
            mob.cachedDamage[Utils.getRandom(2)] += 20// random
        }
        super.handleHit(hit)
    }

    override fun sendDeath(source: Figure?) {
        mob.movement.reset()
        mob.mobCombat!!.removeTarget()
        mob.animate(-1)
        mob.shieldTimer = 0

        GameWorld.schedule(MobDeath(mob, mob.combatDefinition.deathDelay))
    }
}