package com.fury.game.node.entity.mob.combat

import com.fury.core.model.node.entity.actor.figure.Figure
import com.fury.game.node.entity.actor.figure.mob.MobCombat
import com.fury.game.npc.slayer.Nechryael

class NechryaelCombat(private val mob: Nechryael) : MobCombat(mob) {
    override fun sendDeath(source: Figure?) {
        super.sendDeath(source)
        if (mob.hasActiveSpawns())
            mob.removeDeathSpawns()
    }
}