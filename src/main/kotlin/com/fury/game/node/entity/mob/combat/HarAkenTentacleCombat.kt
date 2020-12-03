package com.fury.game.node.entity.mob.combat

import com.fury.core.model.node.entity.actor.figure.Figure
import com.fury.game.entity.character.npc.impl.fightkiln.HarAkenTentacle
import com.fury.game.node.entity.actor.figure.mob.MobCombat

class HarAkenTentacleCombat(private val mob: HarAkenTentacle) : MobCombat(mob) {

    override fun sendDeath(source: Figure?) {
        mob.aken.removeTentacle(mob)
        super.sendDeath(source)
    }
}