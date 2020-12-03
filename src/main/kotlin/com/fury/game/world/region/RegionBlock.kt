package com.fury.game.world.region

import com.fury.core.model.item.FloorItem
import com.fury.core.model.node.entity.actor.figure.mob.Mob
import com.fury.core.model.node.entity.actor.figure.player.Player
import com.fury.game.entity.`object`.GameObject
import com.fury.game.network.packet.out.FloorItemRemoval
import com.fury.game.network.packet.out.FloorItemSpawn
import com.fury.game.network.packet.out.ObjectRemoval
import com.fury.game.network.packet.out.ObjectSpawn
import com.fury.game.world.map.Position
import io.netty.util.internal.ConcurrentSet
import java.util.*
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.ConcurrentLinkedDeque

class RegionBlock {

    private var floorItems: MutableMap<Position, MutableSet<FloorItem>>? = null

    private var objects: MutableMap<Position, MutableList<GameObject>>? = null

    val players = ConcurrentLinkedDeque<Player>()

    val npcs = ConcurrentLinkedDeque<Mob>()

    private var removed: Deque<GameObject>? = null

    internal fun addPlayer(player: Player) {
        players.add(player)
    }

    internal fun addNpc(mob: Mob) {
        npcs.add(mob)
    }


    internal fun addObject(gameObject: GameObject) {
        val objs = getObjects().getOrDefault(gameObject, LinkedList()).toMutableList()
        if (objs.add(gameObject))
            getObjects()[gameObject] = objs
    }

    internal fun addFloorItem(item: FloorItem) {
        val items = getFloorItems(item.tile)
        items.add(item)
        getFloorItems()[item.tile] = items
    }

    fun addRemovedObject(gameObject: GameObject) {
        getRemovedObjects().add(gameObject)
    }

    internal fun removeNpc(mob: Mob) {
        npcs.remove(mob)
    }

    internal fun removePlayer(player: Player) {
        players.remove(player)
    }

    internal fun removeObject(gameObject: GameObject) {
        getObjects()[gameObject]?.remove(gameObject)
    }

    internal fun removeFloorItem(item: FloorItem) {
        val items = getFloorItems(item.tile)
        items.remove(item)
        if (items.isEmpty()) {
            getFloorItems().remove(item.tile)
        } else {
            getFloorItems()[item.tile] = items
        }
    }

    internal fun containsNpc(mob: Mob): Boolean {
        return npcs.contains(mob)
    }

    internal fun containsPlayer(player: Player): Boolean {
        return players.contains(player)
    }

    internal fun containsFloorItem(floorItem: FloorItem): Boolean {
        return if (getFloorItems()[floorItem.tile] == null) false else getFloorItems()[floorItem.tile]!!.contains(floorItem)
    }

    internal fun containsObject(position: Position): Boolean {
        val objs = getObjects()[position]
        return objs != null && objs.contains(position)
    }

    internal fun getGameObjects(position: Position): List<GameObject> {
        return getObjects().getOrDefault(position, emptyList())
    }

    internal fun getGameObject(id: Int, position: Position): GameObject? {
        return getGameObjects(position).firstOrNull { it.id == id }//TODO revision
    }

    internal fun sendGameObjects(player: Player) {
        for (obj in getRemovedObjects())
            player.send(ObjectRemoval(obj))

        val entrySet = getObjects().entries
        for ((_, value) in entrySet)
            for (obj in value)
                player.send(ObjectSpawn(obj))

    }

    internal fun sendFloorItems(player: Player) {
        for ((_, value) in getFloorItems()) {
            value
                    .filter { it.visibleFor(player) }
                    .forEach { player.send(FloorItemRemoval(it)) }
            value
                    .filter { it.visibleFor(player) }
                    .forEach { player.send(FloorItemSpawn(it)) }
        }
    }

    fun getObjects(): MutableMap<Position, MutableList<GameObject>> {
        if (objects == null)
            objects = ConcurrentHashMap()
        return objects!!
    }

    fun getFloorItems(): MutableMap<Position, MutableSet<FloorItem>> {
        if (floorItems == null)
            floorItems = ConcurrentHashMap()
        return floorItems!!
    }

    private fun getRemovedObjects(): Deque<GameObject> {
        if (removed == null)
            removed = ConcurrentLinkedDeque<GameObject>()
        return removed!!
    }

    internal fun getFloorItem(id: Int, position: Position): FloorItem? {
        return getFloorItems(position).firstOrNull {
            it.id == id//TODO change to item & .isEqual(id)
        }
    }

    fun getFloorItems(position: Position): MutableSet<FloorItem> {
        return getFloorItems().getOrDefault(position, ConcurrentSet())
    }

    fun skip(gameObject: GameObject) {
        getRemovedObjects().add(gameObject)
    }
}