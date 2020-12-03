package com.fury.core.model.node.entity.actor

import com.fury.cache.Revision
import com.fury.core.model.node.entity.Entity
import com.fury.game.world.map.Position
import com.fury.game.world.update.flag.block.Animation
import com.fury.game.world.update.flag.block.graphic.Graphic

abstract class Actor @JvmOverloads constructor(position: Position, var revision: Revision = Revision.RS2) : Entity(position) {

    fun animate(id: Int) {
        perform(Animation(id))
    }

    fun graphic(id: Int) {
        perform(Graphic(id))
    }

    open fun perform(animation: Animation) {}

    open fun perform(graphic: Graphic) {}
}