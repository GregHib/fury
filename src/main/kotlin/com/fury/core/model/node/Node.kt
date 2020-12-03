package com.fury.core.model.node

import com.fury.core.model.node.entity.actor.figure.mob.Mob
import com.fury.core.model.node.entity.actor.figure.player.Player
import com.fury.game.npc.familiar.Familiar
import com.fury.game.world.map.Position

abstract class Node(position: Position) : Position(position) {

    abstract fun register()

    abstract fun deregister()

    abstract var sizeX: Int

    abstract var sizeY: Int

    fun getSize(): Int {
        return Math.max(sizeX, sizeY)
    }

    var finished: Boolean = false

    val centredPosition: Position
        get() {
            return if (sizeX == 1 && sizeY == 1) this.copyPosition() else Position(getCoordFaceX(sizeX), getCoordFaceY(sizeY = sizeY), z)
        }

    //TODO replace all is and to's with kotlin
    fun isPlayer(): Boolean {
        return this is Player
    }

    val isFamiliar: Boolean = this is Familiar

    fun isNpc(): Boolean {
        return this is Mob
    }
}