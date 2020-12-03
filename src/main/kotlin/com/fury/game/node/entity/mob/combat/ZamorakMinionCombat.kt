package com.fury.game.node.entity.mob.combat

import com.fury.core.model.node.entity.actor.figure.Figure
import com.fury.core.model.node.entity.actor.figure.player.Player
import com.fury.game.content.controller.impl.GodWars
import com.fury.game.node.entity.actor.figure.mob.MobCombat
import com.fury.game.node.entity.actor.figure.mob.MobDeath
import com.fury.game.npc.bosses.godwars.zamorak.ZamorakMinion
import com.fury.game.world.GameWorld

class ZamorakMinionCombat(private val mob: ZamorakMinion) : MobCombat(mob) {
    override fun sendDeath(source: Figure?) {
        mob.movement.reset()
        mob.mobCombat!!.removeTarget()
        mob.animate(-1)
        GameWorld.schedule(MobDeath(mob, mob.combatDefinition.deathDelay, Runnable {
            if (source is Player) {
                val controller = source.controllerManager.controller
                if (controller != null && controller is GodWars)
                    controller.incrementKillCount(GodWars.ZAMORAK)
            }
        }))
    }
}