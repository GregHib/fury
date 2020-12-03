package com.fury.game.content.events.daily.event

import com.fury.game.content.events.daily.DailyEvent
import com.fury.game.content.events.daily.DailyEventType
import com.fury.game.node.entity.actor.figure.player.Points
import com.fury.game.world.GameWorld

class DoublePestControlPointEvent : DailyEvent {
    override fun getEventType(): DailyEventType {
        return DailyEventType.PEST_CONTROL_DOUBLE_POINTS
    }

    override fun onStart(hours: Int) {
        GameWorld.sendBroadcast("Pest Control Event has Started, last for $hours hours!")
    }

    override fun onFinish() {
        GameWorld.sendBroadcast("Pest Control Event has finished!")
    }

    override fun getPointsAdded(type: Points, amount: Double): Double {
        return when(type) {
            Points.COMMENDATIONS -> {
                amount * 2
            }
            else -> {
                super.getPointsAdded(type, amount)
            }
        }
    }

}