package com.fury.game.node.entity.mob.combat

import com.fury.core.model.node.entity.actor.figure.Figure
import com.fury.game.entity.character.combat.Hit
import com.fury.game.node.entity.actor.figure.mob.MobCombat
import com.fury.game.npc.bosses.glacors.Glacyte
import com.fury.util.Misc

class GlacyteCombat(private val mob: Glacyte): MobCombat(mob) {

    override fun handleHit(hit: Hit) {
        if (mob.targetIndex == -1)
            mob.targetIndex = hit.source.index
        var damage = hit.damage
        if (mob.effect.toInt() == 2) {
            val target = mob.mobCombat!!.target
            if (target != null && damage > 0)
                damage = (((6 - Misc.getDistance(target, mob.glacor)) / 10 + .4) * damage).toInt()
            hit.damage = damage
        }
        super.handleHit(hit)
    }

    override fun sendDeath(source: Figure?) {
        mob.glacor!!.removeGlacyte(mob)
        super.sendDeath(source)
    }
}