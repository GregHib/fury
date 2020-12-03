package com.fury.game.node.entity.mob.combat

import com.fury.core.model.node.entity.actor.figure.Figure
import com.fury.game.node.entity.actor.figure.mob.MobCombat
import com.fury.game.npc.minigames.barrows.BarrowsBrother

class BarrowsBrotherCombat(private val mob: BarrowsBrother) : MobCombat(mob) {

    override fun sendDeath(source: Figure?) {
        super.sendDeath(source)
        mob.barrows.killedBrother()
    }
}