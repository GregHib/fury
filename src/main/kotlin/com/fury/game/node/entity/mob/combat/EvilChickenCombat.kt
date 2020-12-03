package com.fury.game.node.entity.mob.combat

import com.fury.core.model.node.entity.actor.figure.Figure
import com.fury.game.node.entity.actor.figure.mob.MobCombat
import com.fury.game.npc.misc.EvilChicken
import com.fury.util.Misc

class EvilChickenCombat(private val mob: EvilChicken) : MobCombat(mob) {

    override fun sendDeath(source: Figure?) {
        super.sendDeath(source)
        mob.respawnTile = EvilChicken.spots[Misc.random(EvilChicken.spots.size - 1)]
    }
}