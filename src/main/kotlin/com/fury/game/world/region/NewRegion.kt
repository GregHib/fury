package com.fury.game.world.region

import com.fury.core.model.node.entity.actor.figure.Figure
import com.fury.core.model.node.entity.actor.figure.mob.Mob
import com.fury.core.model.node.entity.actor.figure.player.Player
import com.fury.game.entity.`object`.GameObject
import com.fury.core.model.item.FloorItem
import com.fury.game.world.map.Position
import com.fury.game.world.map.region.Region
import java.util.*

class NewRegion(val id: Int) {

    companion object {
        private const val CHUNK_SIZE = 8
        const val SIZE = CHUNK_SIZE * 8
        const val VIEW_DISTANCE = SIZE / 4 - 1
        const val HEIGHT_LEVELS = 4
    }

    private val blocks = arrayOfNulls<RegionBlock>(HEIGHT_LEVELS)

    var surroundingRegions = Optional.empty<Array<NewRegion>>()

    fun loadRegionMap(region: Region) {
        RegionDecoder(id, region).run()//TODO , this)
    }

    fun addEntity(figure: Figure) {
        if (figure is Player)
            addPlayer(figure)
        else if (figure is Mob)
            addNpc(figure)
    }

    fun addPlayer(player: Player) {
        getBlock(player.z).addPlayer(player)
    }

    fun addNpc(mob: Mob) {
        getBlock(mob.z).addNpc(mob)
    }

    fun addObject(gameObject: GameObject) {
        getBlock(gameObject.z).addObject(gameObject)
    }

    fun addFloorItem(item: FloorItem) {
        getBlock(item.tile.z).addFloorItem(item)
    }

    fun addRemovedObject(gameObject: GameObject) {
        getBlock(gameObject.z).addRemovedObject(gameObject)
    }


    fun removeEntity(figure: Figure) {
        if (figure is Player)
            removePlayer(figure)
        else if (figure is Mob)
            removeNpc(figure)
    }

    fun removePlayer(player: Player) {
        getBlock(player.z).removePlayer(player)
    }

    fun removeNpc(mob: Mob) {
        getBlock(mob.z).removeNpc(mob)
    }

    fun removeObject(gameObject: GameObject) {
        getBlock(gameObject.z).removeObject(gameObject)
    }

    fun removeFloorItem(item: FloorItem) {
        getBlock(item.tile.z).removeFloorItem(item)
    }

    fun containsNpc(height: Int, mob: Mob): Boolean {
        return getBlock(height).containsNpc(mob)
    }

    fun containsPlayer(height: Int, player: Player): Boolean {
        return getBlock(height).containsPlayer(player)
    }

    fun containsObject(height: Int, gameObject: GameObject): Boolean {
        return getBlock(height).containsObject(gameObject)
    }

    fun containsObject(position: Position): Boolean {
        return getBlock(position.z).containsObject(position)
    }

    fun containsFloorItem(item: FloorItem): Boolean {
        return getBlock(item.tile.z).containsFloorItem(item)
    }

    fun getPlayers(height: Int): Collection<Player> {
        return getBlock(height).players
    }

    fun getNpcs(height: Int): Collection<Mob> {
        return getBlock(height).npcs
    }

    fun getFloorItem(id: Int, position: Position): FloorItem? {
        return getBlock(position.z).getFloorItem(id, position)
    }

    fun getFloorItems(height: Int): MutableMap<Position, MutableSet<FloorItem>> {
        return getBlock(height).getFloorItems()
    }

    fun getGameObject(id: Int, position: Position): GameObject? {
        return getBlock(position.z).getGameObject(id, position)
    }

    fun sendFloorItems(player: Player) {
        getBlock(player.z).sendFloorItems(player)
    }

    fun sendGameObjects(player: Player) {
        getBlock(player.z).sendGameObjects(player)
    }

    fun getBlock(height: Int): RegionBlock {
        if (height > HEIGHT_LEVELS || height < 0)
            throw IllegalArgumentException("Height is out of bounds. Received ($height), expected range [0, $HEIGHT_LEVELS].")

        if (blocks[height] == null)
            blocks[height] = RegionBlock()

        return blocks[height]!!
    }
}