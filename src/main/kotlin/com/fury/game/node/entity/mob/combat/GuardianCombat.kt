package com.fury.game.node.entity.mob.combat

import com.fury.core.model.node.entity.actor.figure.Figure
import com.fury.game.entity.character.npc.impl.dungeoneering.Guardian

class GuardianCombat(private val mob: Guardian) : DungeonMobCombat(mob) {

    override fun sendDeath(source: Figure?) {
        super.sendDeath(source)
        mob.manager.updateGuardian(mob.reference)
    }
}