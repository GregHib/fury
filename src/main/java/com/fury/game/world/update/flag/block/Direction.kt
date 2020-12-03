package com.fury.game.world.update.flag.block

import com.fury.game.world.map.Position
import com.google.common.collect.ImmutableList

/**
 * Represents a single movement direction.
 *
 * @author Graham
 */
enum class Direction private constructor(val deltaX: Int, val deltaY: Int, private val intValue: Int) {
    NORTH(0, 1, 1),
    NORTH_EAST(1, 1, 2),
    EAST(1, 0, 4),
    SOUTH_EAST(1, -1, 7),
    SOUTH(0, -1, 6),
    SOUTH_WEST(-1, -1, 5),
    WEST(-1, 0, 3),
    NORTH_WEST(-1, 1, 0),
    NONE(0, 0, -1);

    private val faceLocation: Position = Position(deltaX, deltaY)

    fun toInteger(): Int {
        return intValue
    }

    companion object {
        fun getOppositeDirection(direction: Direction): Direction {
            return when (direction) {
                EAST -> WEST
                NORTH -> SOUTH
                NORTH_EAST -> SOUTH_WEST
                NORTH_WEST -> SOUTH_EAST
                SOUTH -> NORTH
                SOUTH_EAST -> NORTH_WEST
                SOUTH_WEST -> NORTH_EAST
                WEST -> EAST
                else -> NONE
            }
        }
        fun valid(): Array<Direction> {
            return VALID
        }
        val DIRECTIONS: ImmutableList<Direction> = ImmutableList.of(NORTH_WEST, NORTH, NORTH_EAST, WEST, EAST, SOUTH_WEST, SOUTH, SOUTH_EAST)
        private val VALID = arrayOf(NORTH, NORTH_EAST, EAST, SOUTH_EAST, SOUTH, SOUTH_WEST, WEST, NORTH_WEST)

        val DELTA_X = byteArrayOf(-1, 0, 1, -1, 1, -1, 0, 1)
        val DELTA_Y = byteArrayOf(1, 1, 1, 0, 0, -1, -1, -1)

        @JvmStatic
        fun fromDeltas(deltaX: Int, deltaY: Int): Direction {
            return when {
                deltaY == 1 -> when (deltaX) {
                    1 -> Direction.NORTH_EAST
                    0 -> Direction.NORTH
                    else -> Direction.NORTH_WEST
                }
                deltaY == -1 -> when (deltaX) {
                    1 -> Direction.SOUTH_EAST
                    0 -> Direction.SOUTH
                    else -> Direction.SOUTH_WEST
                }
                deltaX == 1 -> Direction.EAST
                deltaX == -1 -> Direction.WEST
                else -> Direction.NONE
            }
        }
        fun getDirection(position: Position, other: Position): Direction {
            val deltaX = other.x - position.x
            val deltaY = other.y - position.y

            when {
                deltaY >= 1 -> when {
                    deltaX >= 1 -> return NORTH_EAST
                    deltaX == 0 -> return NORTH
                    deltaX <= -1 -> return NORTH_WEST
                    else -> {
                    }
                }
                deltaY == 0 -> when {
                    deltaX >= 1 -> return EAST
                    deltaX == 0 -> return NONE
                    deltaX <= -1 -> return WEST
                    else -> {
                    }
                }
                deltaY <= -1 -> when {
                    deltaX >= 1 -> return SOUTH_EAST
                    deltaX == 0 -> return SOUTH
                    deltaX <= -1 -> return SOUTH_WEST
                    else -> {
                    }
                }
            }

            return Direction.NONE

        }

        fun isConnectable(deltaX: Int, deltaY: Int): Boolean {
            return Math.abs(deltaX) == Math.abs(deltaY) || deltaX == 0 || deltaY == 0
        }

        @JvmStatic
        fun forID(id: Int): Direction {
            for (d in Direction.values())
                if (id == d.toInteger())
                    return d
            return NORTH
        }

        fun direction(diffX: Int, diffY: Int): Int {
            return when {
                diffX < 0 -> when {
                    diffY < 0 -> 5
                    diffY > 0 -> 0
                    else -> 3
                }
                diffX > 0 -> when {
                    diffY < 0 -> 7
                    diffY > 0 -> 2
                    else -> 4
                }
                else -> when {
                    diffY < 0 -> 6
                    diffY > 0 -> 1
                    else -> -1
                }
            }
        }
    }


}