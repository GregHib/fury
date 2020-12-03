package com.fury.game.node.entity.mob.combat

import com.fury.core.model.node.entity.actor.figure.Figure
import com.fury.game.entity.character.combat.Hit
import com.fury.game.node.entity.actor.figure.mob.MobCombat
import com.fury.game.npc.bosses.bork.Bork

class BorkCombat(private val mob: Bork): MobCombat(mob) {
    override fun handleHit(hit: Hit) {
        if (hit.weapon != null && hit.weapon.id == 25202)
            mob.isSpawnedMinions = true

        super.handleHit(hit)
    }

    override fun sendDeath(source: Figure?) {
        if (!mob.isSpawnedMinions) {
            mob.health.hitpoints = 1
            return
        }
        mob.controller.killBork()
        if (mob.borkMinion != null) {
            for (n in mob.borkMinion!!) {
                if (n == null || n.isDead)
                    continue
                n.sendDeath(source)
            }
        }

        super.sendDeath(source)
    }
}