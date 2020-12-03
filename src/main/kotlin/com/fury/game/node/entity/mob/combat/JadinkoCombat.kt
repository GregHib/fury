package com.fury.game.node.entity.mob.combat

import com.fury.core.model.node.entity.actor.figure.Figure
import com.fury.core.model.node.entity.actor.figure.player.Player
import com.fury.game.content.controller.impl.JadinkoLair
import com.fury.game.node.entity.actor.figure.mob.MobCombat
import com.fury.game.npc.slayer.Jadinko

class JadinkoCombat(private val mob: Jadinko) : MobCombat(mob) {
    override fun sendDeath(source: Figure?) {
        super.sendDeath(source)
        if (source is Player)
            JadinkoLair.addPoints(source, if (mob.id == 13820) 3 else if (mob.id == 13821) 7 else 10)
    }
}