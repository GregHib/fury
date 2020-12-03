package com.fury.game.node.entity.actor.`object`

import java.util.*

enum class ObjectDirection(val id: Int) {
    NORTH(1),
    SOUTH(3),
    EAST(2),
    WEST(0);

    companion object {

        private val values = HashMap<Int, ObjectDirection>()

        init {
            for (orientation in values()) {
                values.put(orientation.id, orientation)
            }
        }

        fun valueOf(id: Int): Optional<ObjectDirection> {
            return Optional.ofNullable(values[id])
        }
    }

}
