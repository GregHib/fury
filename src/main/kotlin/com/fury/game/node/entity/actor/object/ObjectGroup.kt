package com.fury.game.node.entity.actor.`object`

import java.util.*

enum class ObjectGroup(val value: Int) {

    /** The wall object group, which may block a tile.  */
    WALL(0),

    /** The wall decoration object group, which never blocks a tile.  */
    WALL_DECORATION(1),

    /** The interactable object group, for objects that can be clicked and interacted with. */
    INTERACTABLE_OBJECT(2),

    /** The ground decoration object group, which may block a tile.  */
    GROUND_DECORATION(3),

    /** The roof parts that disappear on a camera angle.  */
    ROOFING(4);


    companion object {
        fun valueOf(value: Int): Optional<ObjectGroup> {
            return Arrays.stream(values()).filter { group -> group.value == value }.findAny()
        }
    }

}