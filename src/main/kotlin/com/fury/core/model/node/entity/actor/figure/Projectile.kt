package com.fury.core.model.node.entity.actor.figure

import com.fury.cache.Revision
import com.fury.game.node.entity.actor.figure.ProjectileManager
import com.fury.game.world.map.Position

open class Projectile {
    var source: Position? = null
    var target: Position? = null
    val id: Int
    val revision: Revision
    val startHeight: Int
    val endHeight: Int
    val delay: Int
    var speed: Int
    val angle: Int
    val offset: Int

    @JvmOverloads
    constructor(id: Int, revision: Revision = Revision.RS2, startHeight: Int, endHeight: Int, delay: Int, speed: Int, angle: Int, offset: Int) {
        this.id = id
        this.revision = revision
        this.startHeight = startHeight
        this.endHeight = endHeight
        this.delay = delay
        this.speed = speed
        this.angle = angle
        this.offset = offset
    }

    constructor(source: Position, target: Position, id: Int, startHeight: Int, endHeight: Int, delay: Int, speed: Int, angle: Int, offset: Int) : this(id, Revision.RS2, startHeight, endHeight, delay, speed, angle, offset) {
        setPositions(source, target)
    }

    constructor(source: Position, target: Position, id: Int, revision: Revision, startHeight: Int, endHeight: Int, delay: Int, speed: Int, angle: Int, offset: Int) : this(id, revision, startHeight, endHeight, delay, speed, angle, offset) {
        setPositions(source, target)
    }

    fun clone(): Projectile {
        return Projectile(id, revision, startHeight, endHeight, delay, speed, angle, offset)
    }

    open fun setPositions(source: Position, target: Position): Projectile {
        this.source = source
        this.target = target
        source.getRegion().addProjectile(this)
        return this
    }

    fun getTickDelay(): Int {
        return if(source != null && target != null)
            ProjectileManager.getDelay(source!!, target!!, delay / 10, speed / 10).toInt() + ProjectileManager.getProjectileDelay(source!!, target!!)
        else
            0
    }

    override fun toString(): String {
        return "Projectile [$source -> $target, $id, $startHeight, $endHeight, $delay, $speed, $angle, $offset]"
    }
}