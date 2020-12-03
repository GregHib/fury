package com.fury.game.node.entity.mob.combat

import com.fury.cache.Revision
import com.fury.core.model.node.entity.actor.figure.Figure
import com.fury.game.entity.`object`.GameObject
import com.fury.game.entity.character.npc.impl.queenblackdragon.QueenBlackDragon
import com.fury.game.entity.character.npc.impl.queenblackdragon.QueenState
import com.fury.game.node.entity.actor.`object`.ObjectManager
import com.fury.game.node.entity.actor.figure.mob.MobCombat

class QueenBlackDragonCombat(private val mob: QueenBlackDragon) : MobCombat(mob) {

    override fun sendDeath(source: Figure?) {
        if (mob.phase != mob.deathPhaseCompleted) {
            when (mob.phase) {
                1 -> {
                    mob.attacker.packetSender.sendInterfaceSpriteUpdate(59578, 1153)
                    mob.activeArtifact = GameObject(70777, mob.base.transform(33, 31, 0), 10, 0, Revision.PRE_RS3)
                    mob.attacker.message("The Queen Black Dragon's concentration wavers; the first artifact is now unguarded.")
                }
                2 -> {
                    mob.attacker.packetSender.sendInterfaceSpriteUpdate(59579, 1153)
                    ObjectManager.spawnObject(GameObject(70844, mob.base.transform(24, 21, -1), 10, 0, Revision.PRE_RS3))
                    mob.activeArtifact = GameObject(70780, mob.base.transform(24, 21, 0), 10, 0, Revision.PRE_RS3)
                    mob.attacker.message("The Queen Black Dragon's concentration wavers; the second artifact is now")
                    mob.attacker.message("unguarded.")
                }
                3 -> {
                    mob.attacker.packetSender.sendInterfaceSpriteUpdate(59580, 1153)
                    ObjectManager.spawnObject(GameObject(70846, mob.base.transform(24, 21, -1), 10, 0, Revision.PRE_RS3))
                    mob.activeArtifact = GameObject(70783, mob.base.transform(42, 21, 0), 10, 0, Revision.PRE_RS3)
                    mob.attacker.message("The Queen Black Dragon's concentration wavers; the third artifact is now")
                    mob.attacker.message("unguarded.")
                }
                4 -> {
                    mob.attacker.packetSender.sendInterfaceSpriteUpdate(59581, 1153)
                    ObjectManager.spawnObject(GameObject(70848, mob.base.transform(24, 21, -1), 10, 0, Revision.PRE_RS3))
                    mob.activeArtifact = GameObject(70786, mob.base.transform(33, 21, 0), 10, 0, Revision.PRE_RS3)
                    mob.attacker.message("The Queen Black Dragon's concentration wavers; the last artifact is now unguarded.")
                }
            }
            mob.deathPhaseCompleted = mob.phase
            if(mob.activeArtifact != null)
                ObjectManager.spawnObject(mob.activeArtifact!!)
            mob.setCantInteract(true)
            if (mob.phase < 5) {
                mob.isSpawningWorms = true
                return
            }
            mob.switchState(QueenState.DEFAULT)
            mob.health.hitpoints = mob.maxConstitution
        }
    }
}