package com.fury.game.npc.bosses.bork

import com.fury.core.model.node.entity.actor.figure.mob.Mob
import com.fury.core.model.node.entity.actor.figure.player.Player
import com.fury.game.content.controller.impl.BorkController
import com.fury.game.entity.character.npc.drops.Drop
import com.fury.game.node.entity.mob.combat.BorkCombat
import com.fury.game.world.GameWorld
import com.fury.game.world.map.Position
import com.fury.game.world.map.instance.MapInstance
import com.fury.game.world.update.flag.block.Direction
import com.fury.util.Misc
import java.util.*

class Bork(position: Position, val controller: BorkController) : Mob(7134, position) {

    var isSpawnedMinions: Boolean = false
    var borkMinion: Array<Mob?>? = null

    init {
        setCantInteract(true)
        direction.direction = Direction.EAST
        isNoDistanceCheck = true
        forceAggressive = true
    }

    override fun addDrops(killer: Player): List<Drop>? {
        val drops = ArrayList<Drop>()
        drops.add(Drop(532, 100.0, 1, 1, false))
        drops.add(Drop(995, 100.0, 4000, 20000, false))
        drops.add(Drop(12163, 100.0, 8, 10, false))
        drops.add(Drop(12160, 100.0, 15, 17, false))
        drops.add(Drop(12159, 100.0, 4, 5, false))
        drops.add(Drop(1618, 100.0, 1, 2, false))
        drops.add(Drop(1620, 100.0, 2, 3, false))
        drops.add(Drop(1622, 100.0, 3, 4, false))
        drops.add(Drop(1624, 100.0, 1, 1, false))
        return drops
    }

    fun setMinions() {
        borkMinion = arrayOfNulls(3)
        val minions = borkMinion
        if(minions != null) {
            for (i in minions.indices) {
                val minion = GameWorld.mobs.spawn(7135, Position(this, 1), true)
                minion.forceChat("For bork!")
                minion.graphic(1314)
                minion.setTarget(controller.player)
                minions[i] = minion
            }
        }
        forceChat("Destroy the intruder, my Legions!")
        setCantInteract(false)
        setTarget(controller.player)
    }

    override fun processNpc() {
        if (borkMinion != null && Misc.random(20) == 0) {
            for (n in borkMinion!!) {
                if (n == null || n.isDead)
                    continue
                n.forceChat(MINION_MESSAGES[Misc.random(MINION_MESSAGES.size)])
            }
        }
        super.processNpc()
    }

    fun spawnMinions() {
        setCantInteract(true)
        forceChat("Come to my aid, brothers!")
        animate(8757)
        graphic(1315)
        isSpawnedMinions = true

        GameWorld.schedule(2) {
            if (controller.stage != MapInstance.Stages.RUNNING)
                return@schedule
            controller.spawnMinions()
        }
    }

    companion object {

        private val MINION_MESSAGES = arrayOf("Hup! 2.... 3.... 4!", "Resistance is futile!", "We are the collective!", "Form a triangle!")
    }


}
