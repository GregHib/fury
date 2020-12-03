package com.fury.game.node.entity.mob.combat

import com.fury.core.model.node.entity.actor.figure.Figure
import com.fury.game.entity.character.npc.impl.fightkiln.FightKilnMob
import com.fury.game.node.entity.actor.figure.mob.MobCombat
import com.fury.game.node.entity.actor.figure.mob.MobDeath
import com.fury.game.world.GameWorld

class FightKilnMobCombat(private val mob: FightKilnMob) : MobCombat(mob) {

    override fun sendDeath(source: Figure?) {
        mob.direction.interacting = null
        mob.movement.reset()
        mob.mobCombat!!.removeTarget()
        //setAnimation(null);
        mob.controller.checkCrystal()
        //setGraphic(new Graphic(getDeathGfx()));
        GameWorld.schedule(MobDeath(mob, mob.combatDefinition.deathDelay, Runnable { mob.controller.removeNPC() }))
    }
}