package com.fury.game.world.map

import com.fury.game.world.GameWorld
import com.fury.game.world.map.region.Region
import com.fury.util.Misc

open class Position @JvmOverloads constructor(var x: Int, var y: Int, var z: Int = 0) {

    companion object {
        val VIEWPORT_SIZES = intArrayOf(104, 120, 136, 168)

        fun hash(x: Int, y: Int, z: Int): Int {
            return y shl 16 or (x shl 8) or z
        }
    }

    fun setPlane(z: Int): Position {
        this.z = z
        return this
    }

    constructor(position: Position) : this(position.x, position.y, position.z)

    constructor(position: Position, randomise: Int) : this(position.x + Misc.getRandom(randomise * 2) - randomise, position.y + Misc.getRandom(randomise * 2) - randomise, position.z)

    fun getChunkX() = x shr 3

    fun getChunkY() = y shr 3

    fun getRegionX() = x shr 6

    fun getRegionY() = y shr 6

    fun getXInRegion() = x and 0x3F

    fun getYInRegion() = y and 0x3F

    fun getXInChunk() = x and 0x7

    fun getYInChunk() = y and 0x7

    @JvmOverloads
    fun getLocalX(position: Position = this, mapSize: Int = 0): Int {
        return x - 8 * (position.getChunkX() - (VIEWPORT_SIZES[mapSize] shr 4))
    }

    @JvmOverloads
    fun getLocalY(position: Position = this, mapSize: Int = 0): Int {
        return y - 8 * (position.getChunkY() - (VIEWPORT_SIZES[mapSize] shr 4))
    }

    fun getRegion(): Region = GameWorld.regions.get(getRegionId())

    fun getRegionId() = (getRegionX() shl 8) + getRegionY()

    fun copyPosition() = Position(x, y, z)

    @JvmOverloads
    fun add(x: Int, y: Int, z: Int = 0): Position {
        this.x += x
        this.y += y
        this.z += z
        return this
    }

    fun setPosition(x: Int, y: Int, z: Int) {
        this.x = x
        this.y = y
        this.z = z
    }

    fun setPosition(position: Position) {
        this.x = position.x
        this.y = position.y
        this.z = position.z
    }

    @JvmOverloads
    fun transform(x: Int, y: Int, z: Int = 0) = copyPosition().add(x, y, z)


    fun getDistance(other: Position): Int {
        if (z != other.z)
            return 0
        return Math.sqrt(Math.pow((x - other.x).toDouble(), 2.0) + Math.pow((y - other.y).toDouble(), 2.0)).toInt()
    }

    fun getLongestDelta(other: Position): Int {
        if (z != other.z) return 0
        val deltaX = Math.abs(x - other.x)
        val deltaY = Math.abs(y - other.y)
        return Math.max(deltaX, deltaY)
    }

    fun isWithinDistance(other: Position, distance: Int): Boolean {
        if (z != other.z)
            return false
        val deltaX = x - other.x
        val deltaY = y - other.y
        return deltaX <= distance && deltaX >= -distance && deltaY <= distance && deltaY >= -distance
    }

    @JvmOverloads fun isViewableFrom(other: Position, min: Int = -15, max: Int = 14): Boolean {
        if (z != other.z)
            return false
        val deltaX = x - other.x
        val deltaY = y - other.y
        return deltaX in min..max && deltaY in min..max
    }

    @JvmOverloads fun getCoordFaceX(sizeX: Int, sizeY: Int = -1, rotation: Int = -1): Int {
        return x + ((if (rotation == 1 || rotation == 3) sizeY else sizeX) - 1) / 2
    }

    @JvmOverloads fun getCoordFaceY(sizeX: Int = -1, sizeY: Int, rotation: Int = -1): Int {
        return y + ((if (rotation == 1 || rotation == 3) sizeX else sizeY) - 1) / 2
    }

    fun compressed() = y + (x shl 14) + (z shl 28)

    override fun equals(other: Any?): Boolean {
        if(other === this)
            return true

        if(other is Position)
            return sameAs(other)

        return false
    }

    fun sameAs(other: Position) = other.x == x && other.y == y && other.z == z

    @JvmOverloads
    fun sameAs(x: Int, y: Int, z: Int = 0) = x == this.x && y == this.y && z == this.z

    override fun toString(): String {
        return "Position values: [x, y, z] - [$x, $y, $z]."
    }

    override fun hashCode(): Int {
        return hash(x, y, z)
    }
}

