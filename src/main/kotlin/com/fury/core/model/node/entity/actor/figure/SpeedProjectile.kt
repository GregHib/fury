package com.fury.core.model.node.entity.actor.figure

import com.fury.cache.Revision
import com.fury.game.node.entity.actor.figure.ProjectileManager
import com.fury.game.world.map.Position


class SpeedProjectile(id: Int, revision: Revision, startHeight: Int, endHeight: Int, delay: Int, angle: Int, offset: Int) : Projectile(id, revision, startHeight, endHeight, delay, 0, angle, offset) {

    constructor(id: Int, startHeight: Int, endHeight: Int, delay: Int, angle: Int, offset: Int) : this(id, Revision.RS2, startHeight, endHeight, delay, angle, offset)

    constructor(source: Position, target: Position, id: Int, startHeight: Int, endHeight: Int, delay: Int, angle: Int, offset: Int) : this(id, Revision.RS2, startHeight, endHeight, delay, angle, offset) {
        setPositions(source, target)
    }

    constructor(source: Position, target: Position, id: Int, revision: Revision, startHeight: Int, endHeight: Int, delay: Int, angle: Int, offset: Int) : this(id, revision, startHeight, endHeight, delay, angle, offset) {
        setPositions(source, target)
    }
    private fun calculateSpeed(): Projectile {
        if(source != null && target != null)
            speed = ProjectileManager.getSpeedModifier(source!!, target!!)
        return this
    }

    override fun setPositions(source: Position, target: Position): Projectile {
        super.setPositions(source, target)
        calculateSpeed()
        return this
    }
}