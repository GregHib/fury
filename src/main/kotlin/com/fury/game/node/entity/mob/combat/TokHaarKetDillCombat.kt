package com.fury.game.node.entity.mob.combat

import com.fury.cache.Revision
import com.fury.core.model.node.entity.actor.figure.player.Player
import com.fury.game.container.impl.equip.Slot
import com.fury.game.entity.character.combat.Hit
import com.fury.game.entity.character.npc.impl.fightkiln.TokHaarKetDill
import com.fury.game.node.entity.actor.figure.mob.MobCombat

class TokHaarKetDillCombat(private val mob: TokHaarKetDill) : MobCombat(mob) {
    override fun applyHit(hit: Hit) {
        if (mob.receivedHits != -1) {
            hit.damage = 0
            val playerSource = hit.source as Player
            val weaponId = playerSource.equipment.get(Slot.WEAPON).id
            println("pick?$weaponId")
            if (weaponId == 1275 || weaponId == 13661 || weaponId == 15259) {
                mob.receivedHits++
                if (weaponId == 1275 && mob.receivedHits >= 5 || (weaponId == 13661 || weaponId == 15259) && mob.receivedHits >= 3) {
                    mob.receivedHits = -1
                    mob.setTransformation(mob.id + 1, Revision.PRE_RS3)
                    playerSource.message("Your pickaxe breaks the TokHaar-Ket-Dill's thick armour!")
                } else
                    playerSource.message("Your pickaxe slowy cracks its way through the TokHaar-Ket-Dill's armour.")
            }
        } else {
            super.applyHit(hit)
        }

    }
}
