package com.fury.game.node.entity.mob.combat

import com.fury.core.model.node.entity.actor.figure.Figure
import com.fury.game.entity.character.combat.Hit
import com.fury.game.node.entity.actor.figure.mob.MobCombat
import com.fury.game.npc.bosses.glacors.Glacor
import java.util.*

class GlacorCombat(private val mob: Glacor): MobCombat(mob) {

    override fun handleHit(hit: Hit) {
        val glacites = mob.glacites
        if (glacites == null) {
            if (mob.health.hitpoints <= mob.maxConstitution / 2) {
                mob.glacites = ArrayList(3)
                mob.createGlacites()
            }
        } else if (glacites.size != 0)
            hit.damage = 0
        super.handleHit(hit)
    }

    override fun sendDeath(source: Figure?) {
        super.sendDeath(source)
        mob.resetMinions()
    }
}