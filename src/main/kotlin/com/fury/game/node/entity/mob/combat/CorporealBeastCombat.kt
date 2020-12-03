package com.fury.game.node.entity.mob.combat

import com.fury.core.model.node.entity.actor.figure.Figure
import com.fury.game.entity.character.combat.Hit
import com.fury.game.node.entity.actor.figure.mob.MobCombat
import com.fury.game.npc.bosses.corp.CorporealBeast
import com.fury.util.Misc

class CorporealBeastCombat(private val mob: CorporealBeast): MobCombat(mob) {
    override fun handleHit(hit: Hit) {
        mob.reduceHit(hit)
        super.handleHit(hit)
        if (hit.source != null && hit.damage > 320)
            if (Misc.random(8) == 0)
                mob.spawnDarkEnergyCore()
    }

    override fun sendDeath(source: Figure?) {
        super.sendDeath(source)
        if (mob.core != null)
            mob.core!!.sendDeath(source)
    }
}