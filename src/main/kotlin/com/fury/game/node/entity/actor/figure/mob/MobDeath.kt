package com.fury.game.node.entity.actor.figure.mob

import com.fury.core.model.node.entity.actor.figure.Figure
import com.fury.core.model.node.entity.actor.figure.mob.Mob
import com.fury.core.model.node.entity.actor.figure.player.Player
import com.fury.game.container.impl.equip.Slot
import com.fury.game.content.global.Achievements
import com.fury.game.content.global.treasuretrails.TreasureTrailHandlers
import com.fury.game.content.skill.member.hunter.HunterData
import com.fury.game.entity.character.combat.prayer.Prayer
import com.fury.game.entity.character.player.actions.PlayerCombatAction
import com.fury.game.entity.character.player.content.Sounds
import com.fury.game.node.entity.EntityDeath
import com.fury.game.node.entity.actor.figure.mob.drops.MobDropHandler
import com.fury.game.node.entity.actor.figure.player.misc.redo.KillsTracker

class MobDeath @JvmOverloads constructor(mob: Mob, delay: Int = 2, val runnable: Runnable? = null) : EntityDeath<Mob>(mob, delay) {
    override fun preDeath(killer: Figure?) {
        entity.animate(entity.combatDefinition.deathAnim)
    }

    override fun death(killer: Figure?) {
        if(killer != null && killer is Player)
            MobDropHandler.drop(killer, entity)
        entity.reset()
        if(entity.respawnTile != null)
            entity.moveTo(entity.respawnTile!!)
        entity.deregister()
        if (!entity.isSpawned)
            entity.setRespawnTask()
    }

    override fun postDeath(killer: Figure?) {
        runnable?.run()

        if(killer == null)
            return

        if(killer is Player) {
            killer.controllerManager.processNpcDeath(entity)

            if (killer.combat.attackedBy === entity) {
                killer.combat.attackedByDelay = 0
                killer.combat.attackedBy = null
                killer.combat.findTargetDelay = 0
            }

            Sounds.sendGlobalSound(killer, Sounds.getNpcDeathSounds(entity))

            //Achievements
            val boss = entity.maxConstitution > 2000
            if (entity.id != 1158 && entity.id !in 3493..3497) {
                KillsTracker.submit(killer, KillsTracker.KillsEntry(entity.name, 1, boss))
                if (boss)
                    Achievements.doProgress(killer, Achievements.AchievementData.DEFEAT_500_BOSSES)
            }
            if(entity.name.startsWith("Revenant"))
                Achievements.doProgress(killer, Achievements.AchievementData.KILL_100_REVENANTS)

            if(killer.prayer.usingPrayer(1, Prayer.WRATH) && killer.isDead)
                Achievements.finishAchievement(killer, Achievements.AchievementData.KILL_WITH_WRATH)

            Achievements.doProgress(killer, Achievements.AchievementData.KILL_10000_MONSTERS)

            when (entity.id) {
                50 -> if(killer.equipment.get(Slot.WEAPON).id == 1205)
                        Achievements.finishAchievement(killer, Achievements.AchievementData.KILL_THE_KBD_USING_A_BRONZE_DAGGER)
                6260 -> killer.achievementAttributes.setGodKilled(0, true)
                6222 -> killer.achievementAttributes.setGodKilled(1, true)
                6247 -> killer.achievementAttributes.setGodKilled(2, true)
                6203 -> killer.achievementAttributes.setGodKilled(3, true)
                14301 -> Achievements.doProgress(killer, Achievements.AchievementData.KILL_100_GLACORS)
                8777, 8776 -> Achievements.doProgress(killer, Achievements.AchievementData.KILL_100_CHAOS_DWARF_CANNONNEERS)
                52 -> Achievements.finishAchievement(killer, Achievements.AchievementData.KILL_A_BABY_BLUE_DRAGON)
                51 -> Achievements.doProgress(killer, Achievements.AchievementData.KILL_100_FROST_DRAGONS)
                41 -> Achievements.finishAchievement(killer, Achievements.AchievementData.KILL_A_CHICKEN)
                2882, 2881, 2883 -> Achievements.finishAchievement(killer, Achievements.AchievementData.KILL_A_DAGANNOTH_KING)
                5529 -> Achievements.finishAchievement(killer, Achievements.AchievementData.KILL_A_YAK)
                96 -> Achievements.finishAchievement(killer, Achievements.AchievementData.KILL_A_WHITE_WOLF)
                10111 -> Achievements.finishAchievement(killer, Achievements.AchievementData.KILL_A_UNHOLY_CURSEBEARER)
                55 -> if (PlayerCombatAction.isRanging(killer) > 0)
                    Achievements.finishAchievement(killer, Achievements.AchievementData.KILL_A_BLUE_DRAGON_USING_RANGED)
            }

            //Hunter
            if (HunterData.isSalamander(killer.equipment.get(Slot.WEAPON).id))
                Achievements.finishAchievement(killer, Achievements.AchievementData.KILL_A_MONSTER_USING_SALAMANDER)

            //Slayer
            killer.slayerManager.killedNpc(entity)

            TreasureTrailHandlers.handleNpcDeaths(killer, entity)
        }
    }

}