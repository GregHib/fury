package com.fury.game.node.entity.mob.combat

import com.fury.core.model.node.entity.actor.figure.Figure
import com.fury.core.model.node.entity.actor.figure.player.Player
import com.fury.game.content.global.Achievements
import com.fury.game.node.entity.actor.figure.mob.MobCombat
import com.fury.game.node.entity.actor.figure.mob.MobDeath
import com.fury.game.npc.minigames.pest.PestPortal
import com.fury.game.world.GameWorld

class PestPortalCombat(private val mob: PestPortal) : MobCombat(mob) {

    override fun sendDeath(source: Figure?) {
        mob.movement.reset()
        mob.animate(-1)
        GameWorld.schedule(MobDeath(mob, mob.combatDefinition.deathDelay, Runnable {
            val knight = mob.control.knight
            if (knight != null)
                knight.health.heal(500)
            if (source is Player)
                Achievements.finishAchievement(source, Achievements.AchievementData.KILL_A_PORTAL)
        }))
    }
}