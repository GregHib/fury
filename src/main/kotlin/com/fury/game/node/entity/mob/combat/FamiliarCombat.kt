package com.fury.game.node.entity.mob.combat

import com.fury.core.model.node.entity.actor.figure.Figure
import com.fury.game.node.entity.actor.figure.mob.MobCombat
import com.fury.game.node.entity.actor.figure.mob.MobDeath
import com.fury.game.npc.familiar.Familiar
import com.fury.game.world.GameWorld

class FamiliarCombat(private val mob: Familiar) : MobCombat(mob) {

    override fun sendDeath(source: Figure?) {
        if (mob.dead)
            return
        mob.dead = true
        mob.removeFamiliar()
        mob.movement.reset()
        mob.setCantInteract(true)
        mob.mobCombat!!.removeTarget()
        mob.animate(-1)
        mob.owner?.message("Your familiar slowly begins to fade away..")
        GameWorld.schedule(MobDeath(mob, mob.combatDefinition.deathDelay, Runnable { mob.dismissFamiliar(false) }))
    }
}