package com.fury.game.node.entity.mob.combat

import com.fury.core.model.node.entity.actor.figure.player.Player
import com.fury.game.container.impl.equip.Slot
import com.fury.game.entity.character.combat.CombatIcon
import com.fury.game.entity.character.combat.Hit
import com.fury.game.node.entity.actor.figure.mob.MobCombat
import com.fury.game.npc.slayer.Kurask

class KuraskCombat(mob: Kurask) : MobCombat(mob) {
    override fun handleHit(hit: Hit) {
        if (hit.source != null && hit.source.isPlayer()) {
            val player = hit.source as Player
            val weaponId = player.equipment.get(Slot.WEAPON).id
            val ammoId = player.equipment.get(Slot.ARROWS).id
            if (!(weaponId == 13290 || weaponId == 4158) && !(hit.combatIcon == CombatIcon.RANGED && (ammoId == 13280 || ammoId == 4160)))
                hit.damage = 0
        }
        super.handleHit(hit)
    }
}