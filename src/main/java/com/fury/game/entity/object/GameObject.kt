package com.fury.game.entity.`object`

import com.fury.cache.Revision
import com.fury.cache.def.Loader
import com.fury.cache.def.`object`.ObjectDefinition
import com.fury.core.model.node.entity.actor.Actor
import com.fury.game.network.packet.out.ObjectAnimation
import com.fury.game.network.packet.out.WorldGraphic
import com.fury.game.node.entity.actor.`object`.ObjectDirection
import com.fury.game.node.entity.actor.`object`.ObjectType
import com.fury.game.world.GameWorld
import com.fury.game.world.map.Position
import com.fury.game.world.update.flag.block.Animation
import com.fury.game.world.update.flag.block.graphic.Graphic
import java.io.Serializable

/**
 * This file manages a game object figure on the globe.
 *
 * @author Relex lawl / iRageQuit2012
 */

open class GameObject @JvmOverloads constructor(var id: Int = 0, position: Position, var objectType: ObjectType/* = ObjectType.GENERAL_PROP*/, var face: ObjectDirection/* = ObjectDirection.SOUTH*/, revision: Revision = Revision.getRevision(position.getRegionId())) : Actor(position, revision), Serializable {

    var direction: Int
        get() = face.id
        set(value) {face = ObjectDirection.valueOf(value).get()}

    var type: Int
        get() = objectType.id
        set(value) {objectType = ObjectType.values()[value]}

    val definition: ObjectDefinition
        get() = Loader.getObject(id, revision)

    override var sizeX = 1
        get() = definition.sizeX
    override var sizeY = 1
        get() = definition.sizeY

    @JvmOverloads
    constructor(id: Int = 0, position: Position, type: Int = 10, direction: Int = 0, revision: Revision = Revision.getRevision(position.getRegionId())) : this(id, position, ObjectType.values()[type], ObjectDirection.valueOf(direction).get(), revision)

    constructor(obj: GameObject) : this(obj.id, obj, obj.objectType, obj.face, Revision.getRevision(obj.getRegionId()))

    constructor(id: Int, position: Position, revision: Revision) : this(id, position) {
        this.revision = revision
    }

    constructor(id: Int, position: Position, face: Int, revision: Revision) : this(id, position, direction = face) {
        this.revision = revision
    }

    override fun perform(animation: Animation) {
        GameWorld.players
                .filter { it.isViewableFrom(this) }
                .forEach { it.send(ObjectAnimation(this, animation)) }
    }

    override fun perform(graphic: Graphic) {
        GameWorld.players
                .filter { it.isViewableFrom(this) }
                .forEach { it.send(WorldGraphic(graphic, this)) }
    }

    override fun deregister() {
    }

    override fun register() {
    }

    fun validate(id: Int): Boolean {
        if (definition.varbitIndex != -1) {
            definition.configObjectIds
                    .filter { it != -1 && it == id }
                    .forEach { return true }
        }
        return this.id == id
    }

    override fun toString(): String {
        return id.toString() + " " + revision + " " + objectType + " " + face + " " + super.toString()
    }
}
