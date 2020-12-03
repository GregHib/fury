package com.fury.game.node.entity.mob.combat

import com.fury.core.model.node.entity.actor.figure.Figure
import com.fury.game.node.entity.actor.figure.mob.MobCombat
import com.fury.game.npc.minigames.pest.PestMonsters

open class PestMonstersCombat(private val mob: PestMonsters) : MobCombat(mob) {

    override fun sendDeath(source: Figure?) {
        super.sendDeath(source)
        mob.manager.pestCounts[mob.portalIndex]--
    }
}