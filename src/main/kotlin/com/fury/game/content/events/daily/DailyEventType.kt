package com.fury.game.content.events.daily

enum class DailyEventType(val typeName: String, val description: String) {
    PEST_CONTROL_DOUBLE_POINTS("pest control", "double commendation"),
    RANDOM_DOUBLE_EXPERIENCE("double exp", "double exp in random skill")

    ;

    companion object {
        @JvmStatic
        fun getType(string: String) : DailyEventType? {
            return values().firstOrNull { string.equals(it.typeName, true) }
        }
    }
}