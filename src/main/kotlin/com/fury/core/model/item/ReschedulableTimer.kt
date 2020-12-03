package com.fury.core.model.item

import com.fury.cache.Revision
import com.fury.engine.task.executor.GameExecutorManager
import java.util.*
import java.util.concurrent.ScheduledFuture
import java.util.concurrent.TimeUnit

class ReschedulableTimer(private val delay: Long = 1,
                         private val timeUnit: TimeUnit = TimeUnit.HOURS,
                         private val onFinish: ReschedulableTimer.() -> Unit) {

    private val scheduleEvent = { GameExecutorManager.slowExecutor.schedule({onFinish()}, delay, timeUnit) }
    private var scheduledEvent: ScheduledFuture<*> = scheduleEvent()

    fun reschedule(delay: Long = 1, timeUnit: TimeUnit = TimeUnit.HOURS) {
        stop();
        scheduleEvent()
    }

    fun stop() { scheduledEvent.cancel(true) }
    fun remainingTime(timeUnit: TimeUnit): Long { return scheduledEvent.getDelay(timeUnit) }
}