package com.fury.game.node.entity

import com.fury.core.model.node.entity.actor.figure.Figure
import com.fury.core.model.node.entity.actor.figure.player.Player
import com.fury.core.model.node.entity.actor.figure.mob.Mob
import java.util.*
import java.util.function.Predicate
import java.util.stream.Stream
import java.util.stream.StreamSupport

class EntityList<T : Figure>(val capacity: Int, player: Boolean) : Iterable<T> {

    /** The array of entities */
    val entities: Array<T?> = (if (player) arrayOfNulls<Player?>(capacity) else arrayOfNulls<Mob?>(capacity)) as Array<T?>

    /** The lowest free index available */
    private var lowestFreeIndex: Int = 0

    /** The number of entities in list */
    var size: Int = 0

    override fun iterator(): Iterator<T> {
        return EntityIterator(this)
    }

    override fun toString(): String {
        return Arrays.toString(entities)
    }

    fun add(entity: T): Boolean {
        synchronized(this) {
            entity.index = lowestFreeIndex + 1
            entity.isRegistered = true
            entities[lowestFreeIndex] = entity
            size++
            for (i in lowestFreeIndex + 1 until entities.size) {
                if (entities[i] == null) {
                    lowestFreeIndex = i
                    break
                }
            }
            return true
        }
    }


    fun remove(entity: T): Boolean {
        synchronized(this) {
            val listIndex = entity.index - 1
            entity.isRegistered = false
            entities[listIndex] = null
            size--
            if (listIndex < lowestFreeIndex) {
                lowestFreeIndex = listIndex
            }
            return true
        }
    }

    operator fun get(index: Int): T? {
        return if (index >= entities.size || index == 0) {
            null
        } else entities[index - 1]
    }

    operator fun contains(entity: T): Boolean {
        return entity.index != 0 && entities[entity.index - 1] === entity
    }

    fun search(filter: Predicate<in T>): Optional<T> {
        for (e in entities) {
            if (e == null)
                continue
            if (filter.test(e))
                return Optional.of(e)
        }
        return Optional.empty()
    }

    fun size(): Int {
        return size
    }

    fun stream(): Stream<T> {
        return StreamSupport.stream(spliterator(), false)
    }

    fun capacity(): Int {
        return capacity
    }

    fun isFull(): Boolean {
        return size >= capacity
    }
}