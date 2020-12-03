package com.fury.game.content.events.daily

import com.fury.game.content.events.daily.event.DoublePestControlPointEvent
import com.fury.game.content.events.daily.event.RandomDoubleExperienceEvent

class DailyEventFactory(val type: DailyEventType) {
    fun build(): DailyEvent {
        return when(type) {
            DailyEventType.PEST_CONTROL_DOUBLE_POINTS -> DoublePestControlPointEvent()
            DailyEventType.RANDOM_DOUBLE_EXPERIENCE -> RandomDoubleExperienceEvent()
        }
    }
}