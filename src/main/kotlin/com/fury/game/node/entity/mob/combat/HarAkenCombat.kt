package com.fury.game.node.entity.mob.combat

import com.fury.core.model.node.entity.actor.figure.Figure
import com.fury.game.entity.character.npc.impl.fightkiln.HarAken
import com.fury.game.node.entity.actor.figure.mob.MobCombat

class HarAkenCombat(private val mob: HarAken) : MobCombat(mob) {

    override fun sendDeath(source: Figure?) {
        mob.graphic(2924 + mob.getSize())
        if (mob.time != 0L) {
            mob.removeTentacles()
            mob.controller.removeNPC()
            mob.time = 0
        }
        super.sendDeath(source)
    }
}