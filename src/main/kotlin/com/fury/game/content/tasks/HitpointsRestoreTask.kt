package com.fury.game.content.tasks

import com.fury.core.task.Task
import com.fury.game.world.GameWorld

class HitpointsRestoreTask : Task(false, 10) {//6 seconds
    override fun run() {
        GameWorld.players.forEach { player ->
            if (!player.isDead)
                player.health.restoreHitPoints()
        }
        GameWorld.mobs.mobs.forEach {mob ->
            if(!mob.isDead && !mob.finished)
                mob.health.restoreHitPoints()
        }
    }

}
