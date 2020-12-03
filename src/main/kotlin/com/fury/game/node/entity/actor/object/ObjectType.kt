package com.fury.game.node.entity.actor.`object`

import java.util.*

enum class ObjectType
/**
 * Creates a new [ObjectType].
 *
 * @param id    the identification of this type
 * @param group the group of this type.
 */
private constructor(
        /** The identification of this type.  */
        /**
         * Gets the identification of this type.
         *
         * @return the identification of this type.
         */
        val id: Int,
        /** The [ObjectGroup] this type is associated to.  */
        /**
         * Gets the group of this type.
         *
         * @return the group of this type.
         */
        val group: ObjectGroup) {

    /** Represents straight walls, fences etc.  */
    STRAIGHT_WALL(0, ObjectGroup.WALL),

    /** Represents diagonal walls corner, fences etc connectors.  */
    DIAGONAL_CORNER_WALL(1, ObjectGroup.WALL),

    /** Represents entire walls, fences etc corners.  */
    ENTIRE_WALL(2, ObjectGroup.WALL),

    /** Represents straight wall corners, fences etc connectors.  */
    WALL_CORNER(3, ObjectGroup.WALL),

    /** Represents straight inside wall decorations.  */
    STRAIGHT_INSIDE_WALL_DECORATION(4, ObjectGroup.WALL_DECORATION),

    /** Represents straight outside wall decorations.  */
    STRAIGHT_OUTSIDE_WALL_DECORATION(5, ObjectGroup.WALL_DECORATION),

    /** Represents diagonal outside wall decorations.  */
    DIAGONAL_OUTSIDE_WALL_DECORATION(6, ObjectGroup.WALL_DECORATION),

    /** Represents diagonal inside wall decorations.  */
    DIAGONAL_INSIDE_WALL_DECORATION(7, ObjectGroup.WALL_DECORATION),

    /** Represents diagonal in wall decorations.  */
    DIAGONAL_INTERIOR_WALL_DECORATION(8, ObjectGroup.WALL_DECORATION),

    /** Represents diagonal walls, fences etc.  */
    DIAGONAL_WALL(9, ObjectGroup.WALL),

    /** Represents all kinds of objects, trees, statues, signs, fountains etc.  */
    GENERAL_PROP(10, ObjectGroup.INTERACTABLE_OBJECT),

    /** Represents ground objects like daisies etc.  */
    WALKABLE_PROP(11, ObjectGroup.INTERACTABLE_OBJECT),

    /** Represents straight sloped roofs.  */
    STRAIGHT_SLOPED_ROOF(12, ObjectGroup.ROOFING),

    /** Represents diagonal sloped roofs.  */
    DIAGONAL_SLOPED_ROOF(13, ObjectGroup.ROOFING),

    /** Represents diagonal slope connecting roofs.  */
    DIAGONAL_SLOPED_CONNECTING_ROOF(14, ObjectGroup.ROOFING),

    /** Represents straight sloped corner connecting roofs.  */
    STRAIGHT_SLOPED_CORNER_CONNECTING_ROOF(15, ObjectGroup.ROOFING),

    /** Represents straight sloped corner roofs.  */
    STRAIGHT_SLOPED_CORNER_ROOF(16, ObjectGroup.ROOFING),

    /** Represents straight flat top roofs.  */
    STRAIGHT_FLAT_TOP_ROOF(17, ObjectGroup.ROOFING),

    /** Represents straight bottom edge roofs.  */
    STRAIGHT_BOTTOM_EDGE_ROOF(18, ObjectGroup.ROOFING),

    /** Represents diagonal bottom edge connecting roofs.  */
    DIAGONAL_BOTTOM_EDGE_CONNECTING_ROOF(19, ObjectGroup.ROOFING),

    /** Represents straight bottom edge connecting roofs.  */
    STRAIGHT_BOTTOM_EDGE_CONNECTING_ROOF(20, ObjectGroup.ROOFING),

    /** Represents straight bottom edge connecting corner roofs.  */
    STRAIGHT_BOTTOM_EDGE_CONNECTING_CORNER_ROOF(21, ObjectGroup.ROOFING),

    /**
     * Represents ground decoration + map signs (quests, water fountains, shops,
     * etc)
     */
    GROUND_PROP(22, ObjectGroup.GROUND_DECORATION);


    companion object {

        /** A mutable [Map] of `int` keys to [ObjectType] values.  */
        private val values = HashMap<Int, ObjectType>()

        /* Populates the {@link #values} cache. */
        init {
            for (type in values()) {
                values.put(type.id, type)
            }
        }

        /**
         * Returns a [ObjectType] wrapped in an [Optional] for the
         * specified `id`.
         *
         * @param id The game object type id.
         * @return The optional game object type.
         */
        fun valueOf(id: Int): Optional<ObjectType> {
            return Optional.ofNullable(values[id])
        }
    }

}
