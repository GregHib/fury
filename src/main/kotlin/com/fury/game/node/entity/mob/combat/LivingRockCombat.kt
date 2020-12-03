package com.fury.game.node.entity.mob.combat

import com.fury.core.model.node.entity.actor.figure.Figure
import com.fury.game.node.entity.actor.figure.mob.MobCombat
import com.fury.game.node.entity.actor.figure.mob.MobDeath
import com.fury.game.npc.slayer.LivingRock
import com.fury.game.world.GameWorld

class LivingRockCombat(private val mob: LivingRock) : MobCombat(mob) {

    override fun sendDeath(source: Figure?) {
        mob.movement.reset()
        mob.mobCombat!!.removeTarget()
        mob.animate(-1)
        GameWorld.schedule(MobDeath(mob, mob.combatDefinition.deathDelay, Runnable {
            if(source != null)
                mob.transformIntoRemains(source)
        }))
    }
}