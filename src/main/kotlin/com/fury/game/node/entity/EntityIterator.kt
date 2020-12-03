package com.fury.game.node.entity

import com.fury.core.model.node.entity.actor.figure.Figure
import java.util.*

/**
 * @author Tyluur <itstyluur></itstyluur>@gmail.com>
 * @since 8/15/2017
 */
class EntityIterator<out T : Figure> internal constructor(private val entityList: EntityList<T>) : MutableIterator<T> {

    /**
     * The previous index of this iterator.
     */
    private var previousIndex = -1

    /**
     * The current index of this iterator.
     */
    private var index = 0

    override fun hasNext(): Boolean {
        for (i in index until entityList.entities.size) {
            if (entityList.entities[i] != null) {
                index = i
                return true
            }
        }
        return false
    }

    override fun next(): T {
        var entity: T? = null
        for (i in index until entityList.entities.size) {
            if (entityList.entities[i] != null) {
                entity = entityList.entities[i]
                index = i
                break
            }
        }
        if (entity == null) {
            throw NoSuchElementException()
        }
        previousIndex = index
        index++
        return entity
    }

    override fun remove() {
        if (previousIndex == -1) {
            throw IllegalStateException()
        }
        entityList.remove(entityList.entities[previousIndex] as T)
        previousIndex = -1
    }

}
