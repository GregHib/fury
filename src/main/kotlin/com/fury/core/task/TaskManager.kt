package com.fury.core.task

import com.fury.game.world.World
import com.fury.util.Logger
import com.google.common.base.Preconditions
import java.util.*
import java.util.concurrent.ConcurrentLinkedQueue

class TaskManager {

    /**
     * Tasks waiting to run
     * Thread safe
     */
    private val pending = ConcurrentLinkedQueue<Task>()

    /**
     * Tasks that are running
     */
    private val active = ArrayList<Task>()


    fun schedule(task: Task) {
        Preconditions.checkNotNull<Any>(task)

        try {
            if (!task.canSchedule())
                return

            task.running = true
            task.onSchedule()

            if (task.instant) {
                try {
                    synchronized(World::class.java) {
                        task.run()
                    }
                } catch (ex: Throwable) {
                    task.stop()
                    Logger.handle(String.format("error executing task: %s", task::class.simpleName), ex)
                    return
                }
            }
            pending.add(task)
        } catch (ex: Throwable) {
            Logger.handle(String.format("error scheduling task: %s", task::class.simpleName), ex)
        }
    }

    fun processTasks() {
        var t: Task?
        do {
            t = pending.poll()
            if (t == null)
                break

            active.add(t)
        } while (true)

        val iterator = active.listIterator()
        while (iterator.hasNext()) {
            val task = iterator.next()

            try {
                task.process()
            } catch (ex: Exception) {
                Logger.handle(String.format("Error executing task: %s [pendingQueue: %d executionQueue: %d], ", task::class.simpleName, pending.size, active.size), ex)
                task.stop()
            }

            if (!task.running) {
                iterator.remove()
            }
        }
    }

    fun cancel(attachment: Any, logout: Boolean) {
        pending
                .filter { attachment == it.key.orElse(null) }
                .forEach { it.stop(logout) }
    }
}