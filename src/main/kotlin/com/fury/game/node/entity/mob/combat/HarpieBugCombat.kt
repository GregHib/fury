package com.fury.game.node.entity.mob.combat

import com.fury.core.model.node.entity.actor.figure.player.Player
import com.fury.game.container.impl.equip.Slot
import com.fury.game.entity.character.combat.Hit
import com.fury.game.node.entity.actor.figure.mob.MobCombat
import com.fury.game.npc.slayer.HarpieBug

class HarpieBugCombat(mob: HarpieBug) : MobCombat(mob) {
    override fun handleHit(hit: Hit) {
        if (hit.source != null && hit.source.isPlayer()) {
            val player = hit.source as Player
            if (player.equipment.get(Slot.SHIELD).id != 7053) {
                player.message("You fail to damage the swarm without a lantern.")
                hit.damage = 0
            }
        }
        super.handleHit(hit)
    }
}