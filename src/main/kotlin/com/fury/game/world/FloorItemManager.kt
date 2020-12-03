package com.fury.game.world

import com.fury.cache.def.Loader
import com.fury.core.model.item.FloorItem
import com.fury.core.model.item.Item
import com.fury.core.model.node.entity.actor.figure.player.Player
import com.fury.engine.task.executor.GameExecutorManager
import com.fury.game.network.packet.out.FloorItemRemoval
import com.fury.game.network.packet.out.FloorItemSpawn
import com.fury.game.system.files.loaders.item.ItemConstants
import com.fury.game.world.map.Position
import java.util.concurrent.TimeUnit

object FloorItemManager {
    @JvmStatic
    fun addGroundItem(item: Item, tile: Position): FloorItem {
        return addGroundItem(item, tile, null, hiddenTime = -1, invisible = false, publicTime = -1)
    }

    @JvmStatic
    @JvmOverloads
    fun addGroundItem(item: Item, tile: Position, owner: Player?, publicTake: Boolean = true, hiddenTime: Int = 120, invisible: Boolean = owner != null, publicTime: Int = 180): FloorItem {
        val floorItem = FloorItem(item, tile, owner, publicTake, !invisible)
        val region = GameWorld.regions.getRegion(tile.getRegionId())
        region.addFloorItem(floorItem)

        if (invisible) {
            owner?.send(FloorItemSpawn(floorItem))
            if (hiddenTime != -1)
                GameExecutorManager.slowExecutor.schedule({ turnPublic(floorItem, publicTime) }, hiddenTime.toLong(), TimeUnit.SECONDS)
        } else {
            val regionId = tile.getRegionId()
            GameWorld.regions.getRegion(regionId).getPlayers(tile.z)
                    .filter { it.started && !it.finished }
                    .forEach {
                        it.send(FloorItemSpawn(floorItem))
                    }
            if (publicTime != -1)
                removeGroundItem(floorItem, publicTime.toLong())
        }
        return floorItem
    }

    @JvmOverloads
    fun turnPublic(floorItem: FloorItem, publicTime: Int, destroyUntradeables: Boolean = true) {
        if (floorItem.visible)
            return

        val regionId = floorItem.tile.getRegionId()
        val region = GameWorld.regions.getRegion(regionId)
        if (!region.region.containsFloorItem(floorItem))
            return

        val owner = floorItem.owner
        if (!ItemConstants.isTradeable(floorItem)) {
            if (destroyUntradeables) {
                region.removeFloorItem(floorItem)
                if (owner != null) {
                    for (r in GameWorld.regions.getSurroundingRegions(owner)) {
                        if (r.regionId == regionId || owner.z != floorItem.tile.z) {
                            owner.send(FloorItemRemoval(floorItem))
                            break
                        }
                    }
                }
            } else {
                if (publicTime != -1)
                    removeGroundItem(floorItem, publicTime.toLong())
            }
            return
        }

        floorItem.visible = true
        GameWorld.regions.getLocalPlayers(floorItem.tile)
                .filter { it.username != floorItem.ownerName && it.started && !it.finished && it.z == floorItem.tile.z }
                .forEach { it.send(FloorItemSpawn(floorItem)) }

        if (publicTime != -1)
            removeGroundItem(floorItem, publicTime.toLong())
    }

    fun removeGroundItem(floorItem: FloorItem): Boolean {
        val regionId = floorItem.tile.getRegionId()
        val region = GameWorld.regions.getRegion(regionId)
        if (!region.containsGroundItem(floorItem))
            return false
        region.removeFloorItem(floorItem)
        GameWorld.regions.getLocalPlayers(floorItem.tile)
                .filter { it.started && !it.finished }
                .forEach { it.send(FloorItemRemoval(floorItem)) }
        return true
    }

    fun removeGroundItem(floorItem: FloorItem, publicTime: Long) {
        GameExecutorManager.slowExecutor.schedule({
            val regionId = floorItem.tile.getRegionId()
            val region = GameWorld.regions.getRegion(regionId)
            if (region.containsGroundItem(floorItem)) {
                region.removeFloorItem(floorItem)
                GameWorld.regions.getLocalPlayers(floorItem.tile)
                        .filter { it.started && !it.finished }
                        .forEach { it.send(FloorItemRemoval(floorItem)) }
            }
        }, publicTime, TimeUnit.SECONDS)
    }

    @JvmStatic
    fun updateGroundItem(item: Item, tile: Position, owner: Player) {
        val groundItem = GameWorld.regions.get(tile.getRegionId()).getFloorItem(item.id, tile, owner)
        if (groundItem == null) {
            addGroundItem(item, tile, owner)
            return
        }
        groundItem.amount = groundItem.amount + item.amount
        owner.send(FloorItemRemoval(groundItem))
        owner.send(FloorItemSpawn(groundItem))
    }

    @JvmStatic
    @JvmOverloads
    fun removeGroundItem(player: Player, floorItem: FloorItem, add: Boolean = true): Boolean {
        val regionId = floorItem.tile.getRegionId()
        val region = GameWorld.regions.getRegion(regionId)
        if (!region.containsGroundItem(floorItem))
            return false

        if (!player.inventory.hasRoom() && !(Loader.getItem(floorItem.id, floorItem.revision).isStackable() && player.inventory.contains(Item(floorItem.id)))) {
            player.inventory.full()
            return false
        }

        region.removeFloorItem(floorItem)
        if (add) {
            player.inventory.add(floorItem.copy())
            player.logger.addPickup(floorItem.copy(), floorItem.owner)
            player.timers.itemPickup.reset()
        }
        return if (!floorItem.visible) {
            player.send(FloorItemRemoval(floorItem))
            true
        } else {
            GameWorld.regions.getLocalPlayers(floorItem.tile)
                    .filter { it.started && !it.finished }
                    .forEach { it.send(FloorItemRemoval(floorItem)) }
            true
        }
    }
}