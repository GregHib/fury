package com.fury.game.world.region

import com.fury.core.model.node.entity.actor.figure.mob.Mob
import com.fury.core.model.node.entity.actor.figure.player.Player
import com.fury.game.world.GameWorld
import com.fury.game.world.map.Position
import com.fury.game.world.map.region.Region
import java.util.*
import java.util.concurrent.ConcurrentHashMap

class RegionManager {

    val activeRegions = ConcurrentHashMap<Int, Region>()

    fun getRegion(position: Position): Region {
        return getRegion(position.getRegionId())
    }

    fun getRegion(x: Int, y: Int): Region {
        return getRegion(Position(x, y))
    }

    fun getRegion(id: Int): Region {
        return activeRegions.computeIfAbsent(id, { Region(id) })
    }

    fun getSurroundingRegions(position: Position): Array<Region> {
        val target = getRegion(position)

        if (target.surroundingRegions.isPresent)
            return target.surroundingRegions.get()

        val surrounding = hashSetOf<Region>()
        val chunkX = position.getChunkX()
        val chunkY = position.getChunkY()

        for (y in -1..1) {
            for (x in -1..1) {
                val regionX = (chunkX + x * 8) shr 3
                val regionY = (chunkY + y * 8) shr 3

                val region = getRegion((regionX shl 8) + regionY)

                if (!surrounding.contains(region))
                    surrounding.add(region)
            }
        }

        val regions = surrounding.toTypedArray()
        target.surroundingRegions = Optional.of(regions)
        return target.surroundingRegions.get()
    }

    fun getLocalNpcs(position: Position): List<Mob> {
        return getSurroundingRegions(position)
                .flatMap { it.getNpcs(position.z) }
                .filterTo(LinkedList()) { position.isWithinDistance(it, Region.VIEW_DISTANCE) }
    }

    fun getLocalPlayers(position: Position): List<Player> {
        return getSurroundingRegions(position)
                .flatMap { it.getPlayers(position.z) }
                .filterTo(LinkedList<Player>()) { position.isWithinDistance(it, Region.VIEW_DISTANCE) }
    }

    fun load(id: Int) {
        get(id, true)
    }

    @JvmOverloads
    fun get(id: Int, load: Boolean = false): Region {
        val region = GameWorld.regions.getRegion(id)

        if (load)
            region.checkLoadMap()

        return region
    }
}