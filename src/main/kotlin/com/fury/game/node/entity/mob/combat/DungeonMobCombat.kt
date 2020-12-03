package com.fury.game.node.entity.mob.combat

import com.fury.core.model.node.entity.actor.figure.Figure
import com.fury.game.entity.character.npc.impl.dungeoneering.DungeonMob
import com.fury.game.node.entity.actor.figure.mob.MobCombat

open class DungeonMobCombat(private val mob: DungeonMob) : MobCombat(mob) {

    override fun sendDeath(source: Figure?) {
        super.sendDeath(source)
        if (mob.marked) {
            mob.manager.removeMark()
            mob.marked = false
        }
    }
}