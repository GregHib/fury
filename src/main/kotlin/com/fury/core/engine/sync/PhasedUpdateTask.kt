package com.fury.core.engine.sync

import com.fury.util.Logger
import java.util.concurrent.Phaser

class PhasedUpdateTask(private val phaser: Phaser, private val task: SynchronizationTask) : SynchronizationTask() {
    override fun run() {
        try {
            task.run()
        } catch (ex: Exception) {
            Logger.handle(String.format("Error in %s", task::class.simpleName), ex)
        }

        phaser.arriveAndDeregister()
    }

}