package com.fury.game.npc.slayer

import com.fury.core.model.node.entity.actor.figure.Figure
import com.fury.core.model.node.entity.actor.figure.mob.Mob
import com.fury.core.model.node.entity.actor.figure.player.Player
import com.fury.engine.task.executor.GameExecutorManager
import com.fury.game.node.entity.mob.combat.LivingRockCombat
import com.fury.game.world.map.Position
import com.fury.util.Misc

import java.util.concurrent.TimeUnit

class LivingRock(id: Int, position: Position, spawned: Boolean) : Mob(id, position, spawned) {

    private var source: Figure? = null
    private var deathTime: Long = 0

    fun transformIntoRemains(source: Figure) {
        this.source = source
        deathTime = Misc.currentTimeMillis()
        val remainsId = id + 5
        setTransformation(remainsId)
        walkType = 0
        GameExecutorManager.slowExecutor.schedule({
            if (remainsId == id)
                takeRemains()
        }, 3, TimeUnit.MINUTES)
    }

    fun canMine(player: Player): Boolean {
        return Misc.currentTimeMillis() - deathTime > 60000 || player === source
    }

    fun takeRemains() {
        id = id - 5
        setTransformation(actualId)
        setBonuses()
        moveTo(respawnTile!!)
        walkType = Mob.Companion.NORMAL_WALK
        deregister()
        if (!isSpawned)
            setRespawnTask()
    }
}
