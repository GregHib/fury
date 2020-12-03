package com.fury.game.node.entity.mob.combat

import com.fury.core.model.node.entity.actor.figure.Figure
import com.fury.core.task.Task
import com.fury.game.entity.character.combat.CombatIcon
import com.fury.game.entity.character.combat.Hit
import com.fury.game.npc.minigames.pest.Splatter
import com.fury.game.world.GameWorld
import com.fury.game.world.update.flag.block.HitMask
import com.fury.game.world.update.flag.block.graphic.Graphic
import com.fury.util.Misc

class SplatterCombat(private val mob: Splatter) : PestMonstersCombat(mob) {

    override fun sendDeath(source: Figure?) {
        val defs = mob.combatDefinition
        mob.movement.reset()
        mob.mobCombat!!.removeTarget()
        mob.animate(-1)
        GameWorld.schedule(object : Task() {
            private var loop: Int = 0
            override fun run() {
                when {
                    loop == 1 -> {
                        mob.animate(3889)
                        mob.perform(Graphic(649 + (mob.id - 3727)))
                    }
                    loop == 2 -> {
                        mob.deregister()
                        for (e in mob.possibleTargets)
                            if (e.isWithinDistance(mob, 2))
                                e.combat.applyHit(Hit(mob, Misc.getRandom(400), HitMask.RED, CombatIcon.NONE))
                    }
                    loop >= defs.deathDelay -> {
                        mob.reset()
                        stop()
                    }
                }
                loop++
            }
        })
    }
}