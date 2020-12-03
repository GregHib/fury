package com.fury.core.engine.sync.task

import com.fury.core.engine.sync.SynchronizationTask
import com.fury.core.model.node.entity.actor.figure.mob.Mob
import com.fury.util.Logger

class MobPreUpdateTask(private val mob: Mob) : SynchronizationTask() {

    override fun run() {
        try {
            if (mob.atomicPlayerCount.get() == 0)
                return

//            if (npc.regionChange)
//                npc.lastPosition = npc.copyPosition()

            mob.movement.process()
        } catch (ex: Exception) {
            Logger.handle(String.format("Error in %s. %s", this::class.simpleName, mob), ex)
        }

    }

}