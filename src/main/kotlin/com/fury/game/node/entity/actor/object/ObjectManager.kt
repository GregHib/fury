package com.fury.game.node.entity.actor.`object`

import com.fury.game.entity.`object`.GameObject
import com.fury.game.world.map.Position

object ObjectManager {
    @JvmStatic
    fun getStandardObject(tile: Position): GameObject? {
        return tile.getRegion().getStandardObject(tile.getXInRegion(), tile.getYInRegion(), tile.z)
    }

    @JvmStatic
    fun getObjectWithType(tile: Position, type: Int): GameObject? {
        return tile.getRegion().getObjectWithType(tile.getXInRegion(), tile.getYInRegion(), tile.z, type)
    }

    @JvmStatic
    fun getObjectWithSlot(tile: Position, slot: Int): GameObject? {
        return tile.getRegion().getObjectWithSlot(tile.getXInRegion(), tile.getYInRegion(), tile.z, slot)
    }

    @JvmStatic
    fun getRealObject(tile: Position, slot: Int): GameObject? {
        return tile.getRegion().getRealObject(tile.getXInRegion(), tile.getYInRegion(), tile.z, slot)
    }

    @JvmStatic
    fun containsObjectWithId(tile: Position, id: Int): Boolean {
        return tile.getRegion().containsObjectWithId(id, tile.getXInRegion(), tile.getYInRegion(), tile.z)
    }

    @JvmStatic
    fun getObjectWithId(id: Int, tile: Position): GameObject? {
        return tile.getRegion().getObjectWithId(id, tile.getXInRegion(), tile.getYInRegion(), tile.z)
    }

    @JvmStatic
    fun isSpawnedObject(gameObject: GameObject): Boolean {
        return gameObject.getRegion().spawnedObjects.contains(gameObject)
    }

    @JvmStatic
    fun spawnObject(gameObject: GameObject) {
        gameObject.getRegion().spawnObject(gameObject, gameObject.getXInRegion(), gameObject.getYInRegion(), gameObject.z, false)
    }

    @JvmStatic
    fun spawnObject(gameObject: GameObject, original: Boolean) {
        gameObject.getRegion().spawnObject(gameObject, gameObject.getXInRegion(), gameObject.getYInRegion(), gameObject.z, original)
    }

    @JvmStatic
    fun removeObject(gameObject: GameObject) {
        gameObject.getRegion().removeObject(gameObject, gameObject.getXInRegion(), gameObject.getYInRegion(), gameObject.z)
    }
}