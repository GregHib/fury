package com.fury.game.content.events.daily

import com.fury.engine.task.executor.GameExecutorManager
import com.fury.game.content.skill.Skill
import com.fury.game.node.entity.actor.figure.player.Points
import com.fury.game.system.files.world.increment.timer.TimedCleanFile
import java.util.*
import java.util.concurrent.TimeUnit

object DailyEventManager {

    private val activeEvents = mutableListOf<DailyEvent>()

    init {
        val timer = Timer()
        val date = Calendar.getInstance()
        date.set(Calendar.HOUR, 0)
        date.set(Calendar.MINUTE, 0)
        date.set(Calendar.SECOND, 0)
        date.set(Calendar.MILLISECOND, 0)
        if (date < Calendar.getInstance())
            date.add(Calendar.DATE, 1)

        timer.schedule(
                object : TimerTask() {
                    var counter = 0
                    override fun run() {
                        val day = Calendar.getInstance().get(Calendar.DAY_OF_WEEK)
                        if(day in 2..5) {
                            val event = if (counter % 1 == 0)
                                DailyEventFactory(DailyEventType.RANDOM_DOUBLE_EXPERIENCE).build()
                            else
                                DailyEventFactory(DailyEventType.PEST_CONTROL_DOUBLE_POINTS).build()

                            timedEvent(event, 1)
                        }
                        counter++
                    }
                }, date.time, TimedCleanFile.TWELVE_HOURS/2
        )
    }

    @JvmStatic
    fun events() : List<DailyEvent> {
        return activeEvents
    }

    @JvmStatic
    fun timedEvent(event: DailyEvent, hours: Int) {
        addEvent(event, hours)
        GameExecutorManager.slowExecutor.schedule({
            removeEvent(event)
        }, hours.toLong(), TimeUnit.HOURS)
    }

    private fun addEvent(event: DailyEvent, hours: Int) {
        event.onStart(hours)
        activeEvents.add(event)
    }

    @JvmStatic
    fun removeEvent(event: DailyEvent) {
        event.onFinish()
        activeEvents.remove(event)
    }

    @JvmStatic
    fun clear() {
        activeEvents.forEach {
            it.onFinish()
        }
        activeEvents.clear()
    }

    fun getPointsCheck(type: Points, cost: Double): Double {
        var newAmount = cost
        activeEvents.forEach { event ->
            newAmount = event.getPointsCheck(type, newAmount)
        }
        return newAmount
    }

    fun getPointsRemoved(type: Points, amount: Double): Double {
        var newAmount = amount
        activeEvents.forEach { event ->
            newAmount = event.getPointsRemoved(type, newAmount)
        }
        return newAmount
    }

    fun getPointsAdded(type: Points, amount: Double): Double {
        var newAmount = amount
        activeEvents.forEach { event ->
            newAmount = event.getPointsAdded(type, newAmount)
        }
        return newAmount
    }

    @JvmStatic
    fun getExperienceAdded(skill: Skill, amount: Double): Double {
        var newAmount = amount
        activeEvents.forEach { event ->
            newAmount = event.getExperienceAdded(skill, newAmount)
        }
        return newAmount
    }

}