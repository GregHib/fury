package com.fury.game.node.entity.mob.combat

import com.fury.core.model.node.entity.actor.figure.Figure
import com.fury.core.model.node.entity.actor.figure.player.Player
import com.fury.game.entity.character.combat.CombatIcon
import com.fury.game.entity.character.combat.Hit
import com.fury.game.entity.character.player.actions.PlayerCombatAction
import com.fury.game.node.entity.actor.figure.mob.MobCombat
import com.fury.game.node.entity.actor.figure.mob.MobDeath
import com.fury.game.npc.bosses.kq.KalphiteQueen
import com.fury.game.world.GameWorld
import com.fury.game.world.update.flag.block.Direction
import com.fury.game.world.update.flag.block.graphic.Graphic

class KalphiteQueenCombat(private val mob: KalphiteQueen): MobCombat(mob) {
    override fun handleHit(hit: Hit) {
        val deathTouch = hit.weapon != null && hit.weapon.id == 25202
        if (deathTouch)
            mob.setTransformation(1160)

        if (mob.transformation!!.getId() == 1158) {
            val src = hit.source
            if (src.isFamiliar || hit.combatIcon == CombatIcon.RANGED || hit.combatIcon == CombatIcon.MAGIC)
                hit.damage = 0
        } else {
            val veracs = hit.source != null && hit.source.isPlayer() && PlayerCombatAction.hasVeracsSet(hit.source as Player)
            if (hit.combatIcon == CombatIcon.MELEE && !veracs)
                hit.damage = 0
        }

        super.handleHit(hit)
    }

    override fun sendDeath(source: Figure?) {
        mob.movement.reset()
        mob.mobCombat!!.removeTarget()
        mob.animate(-1)
        GameWorld.schedule(MobDeath(mob, mob.combatDefinition.deathDelay, Runnable {
            if (mob.transformation!!.getId() == 1158) {
                mob.mobCombat!!.reset()
                mob.setCantInteract(true)
                mob.direction.direction = Direction.SOUTH
                GameWorld.mobs.remove(mob)
                Graphic.sendGlobal(mob, Graphic(1055), mob.centredPosition)
                GameWorld.schedule(6) {
                    mob.reset()
                    mob.setCantInteract(false)
                    mob.setTransformation(1160)
                    GameWorld.mobs.add(mob)
                }
            } else {
                mob.drop()
                mob.reset()
                mob.moveTo(mob.respawnTile!!)
                mob.loadMapRegions()
                mob.deregister()
                if (!mob.isSpawned)
                    mob.setRespawnTask()
                mob.setTransformation(1158)
            }
        }))
    }
}