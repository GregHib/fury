package com.fury.game.node.entity.mob.combat

import com.fury.core.model.node.entity.actor.figure.Figure
import com.fury.core.model.node.entity.actor.figure.Projectile
import com.fury.core.model.node.entity.actor.figure.player.Player
import com.fury.game.content.controller.impl.ZarosGodwars
import com.fury.game.content.global.Achievements
import com.fury.game.entity.character.combat.CombatIcon
import com.fury.game.entity.character.combat.Hit
import com.fury.game.node.entity.actor.figure.ProjectileManager
import com.fury.game.node.entity.actor.figure.mob.MobCombat
import com.fury.game.node.entity.actor.figure.mob.MobDeath
import com.fury.game.node.entity.actor.figure.player.misc.redo.KillsTracker
import com.fury.game.npc.bosses.nex.Nex
import com.fury.game.world.GameWorld
import com.fury.game.world.map.Position
import com.fury.game.world.update.flag.block.HitMask
import com.fury.util.Utils

class NexCombat(private val mob: Nex) : MobCombat(mob) {
    override fun handleHit(hit: Hit) {
        if (hit.weapon != null && hit.weapon.id == 25202)
            mob.stage = 3

        if (mob.isDoingSiphon)
            hit.hitMask = HitMask.PURPLE

        if (mob.id == 13449 && hit.combatIcon == CombatIcon.MELEE) {
            val source = hit.source
            if (source != null) {
                val deflectedDamage = (hit.damage * 0.1).toInt()
                hit.damage = (hit.damage * source.meleePrayerMultiplier).toInt()
                if (deflectedDamage > 0)
                    source.combat.applyHit(Hit(mob, deflectedDamage, HitMask.RED, CombatIcon.DEFLECT))
            }
        }
        super.handleHit(hit)
    }

    override fun sendDeath(source: Figure?) {
        mob.setTransformation(13450)
        GameWorld.schedule(MobDeath(mob, mob.combatDefinition.deathDelay, Runnable {
            if (source!!.isPlayer()) {
                val killer = source as Player?
                Achievements.finishAchievement(killer!!, Achievements.AchievementData.DEFEAT_NEX)
                killer.achievementAttributes.setGodKilled(4, true)
                KillsTracker.submit(killer, KillsTracker.KillsEntry(mob.name, 1, true))
                Achievements.doProgress(killer, Achievements.AchievementData.DEFEAT_500_BOSSES)
            }
            ZarosGodwars.endWar()
        }))
        mob.forceChat("Taste my wrath!")
        //        playSound(3323, 2);
        val target = mob
        GameWorld.schedule(1) {
            mob.graphic(2259)
            for (entity in mob.possibleTargets) {
                if (entity.isDead || entity.finished || !entity.isWithinDistance(target, 10))
                    continue
                ProjectileManager.send(Projectile(target, Position(mob.x + 2, mob.y + 2, mob.z), 2260, 24, 0, 41, 35, 30, 0))
                ProjectileManager.send(Projectile(target, Position(mob.x + 2, mob.y, mob.z), 2260, 41, 0, 41, 35, 30, 0))
                ProjectileManager.send(Projectile(target, Position(mob.x + 2, mob.y - 2, mob.z), 2260, 41, 0, 41, 35, 30, 0))
                ProjectileManager.send(Projectile(target, Position(mob.x - 2, mob.y + 2, mob.z), 2260, 41, 0, 41, 35, 30, 0))
                ProjectileManager.send(Projectile(target, Position(mob.x - 2, mob.y, mob.z), 2260, 41, 0, 41, 35, 30, 0))
                ProjectileManager.send(Projectile(target, Position(mob.x - 2, mob.y - 2, mob.z), 2260, 41, 0, 41, 35, 30, 0))
                ProjectileManager.send(Projectile(target, Position(mob.x, mob.y + 2, mob.z), 2260, 41, 0, 41, 35, 30, 0))
                ProjectileManager.send(Projectile(target, Position(mob.x, mob.y - 2, mob.z), 2260, 41, 0, 41, 35, 30, 0))
                entity.combat.applyHit(Hit(target, Utils.getRandom(600), HitMask.RED, CombatIcon.NONE))
            }
        }
    }
}