package com.fury.game.content.events.daily

import com.fury.game.content.skill.Skill
import com.fury.game.node.entity.actor.figure.player.Points

interface DailyEvent {

    fun getEventType(): DailyEventType

    fun onStart(hours: Int) {}

    fun onFinish() {}

    fun getPointsCheck(type: Points, amount: Double): Double {
        return amount
    }

    fun getPointsRemoved(type: Points, amount: Double): Double {
        return amount
    }

    fun getPointsAdded(type: Points, amount: Double): Double {
        return amount
    }

    fun getExperienceAdded(skill: Skill, amount: Double): Double {
        return amount
    }
}