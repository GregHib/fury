package com.fury.game.world.region

import com.fury.cache.ByteStream
import com.fury.cache.Revision
import com.fury.game.GameLoader
import com.fury.game.entity.`object`.GameObject
import com.fury.game.world.map.Position
import com.fury.game.world.map.region.Region
import com.fury.game.world.map.region.RegionIndexing
import kotlin.experimental.and

class RegionDecoder(private val regionId: Int, private val region: Region) : Runnable {

    override fun run() {
        try {
            val regionData = RegionIndexing.get(regionId) ?: return

            val mapObject = regionData.mapObject
            val mapSettingsData = getFile(mapObject, regionData.revision)
            val mapSettings = loadMapSettings(mapSettingsData) ?: return

            val landscapes = regionData.landscape
            val landscapeData = getFile(landscapes, regionData.revision)
            if (landscapeData != null)
                decodeLandscapes(regionId, landscapeData, mapSettings)

            //HARDCODE catherby bank tile
            if (regionId == 11061)
                region.forceGetRegionMap().setMask(57, 45, 0, 0)
        } catch (throwable: Throwable) {
            System.err.println("Failed to load region: " + regionId)
            throwable.printStackTrace()
        }
    }

    private fun getFile(fileId: Int, revision: Revision): ByteArray? {
        return try {
            GameLoader.getCache().getDecompressedFile(revision.mapIndex, fileId)
        } catch (throwable: Throwable) {
            println("Failed to load region file: $fileId $revision")
            throwable.printStackTrace()
            null
        }

    }

    private fun loadMapSettings(data: ByteArray?): Array<Array<ByteArray>>? {
        val heightMap: Array<Array<ByteArray>>?
        if (data != null) {
            heightMap = Array(4) { Array(64) { ByteArray(64) } }
            val groundStream = ByteStream(data)
            for (z in 0..3) {
                for (tileX in 0..63) {
                    for (tileY in 0..63) {
                        while (true) {
                            val tileType = groundStream.uByte
                            if (tileType == 0) {
                                break
                            } else if (tileType == 1) {
                                groundStream.uByte
                                break
                            } else if (tileType <= 49) {
                                groundStream.uByte
                            } else if (tileType <= 81) {
                                heightMap[z][tileX][tileY] = (tileType - 49).toByte()
                            }
                        }
                    }
                }
            }
            for (z in 0..3) {
                for (x in 0..63) {
                    for (y in 0..63) {
                        if (heightMap[z][x][y] and 0x1 == 1.toByte()) {
                            var height = z
                            if (heightMap[1][x][y] and 0x2 == 2.toByte())
                                height--

                            if (height in 0..3)
                                region.forceGetRegionMap().addUnwalkable(x, y, height)
                        }
                    }
                }
            }
        } else {
            heightMap = null
            for (z in 0..3) {
                for (x in 0..63) {
                    for (y in 0..63) {
                        region.forceGetRegionMap().addUnwalkable(x, y, z)
                    }
                }
            }
        }
        return heightMap
    }

    private fun decodeLandscapes(regionId: Int, data: ByteArray, heightMap: Array<Array<ByteArray>>?) {
        val objectStream = ByteStream(data)
        val absX = (regionId shr 8) * 64
        val absY = (regionId and 0xff) * 64
        var objectId = -1
        var incr = 0
        val revision = Revision.getRevision(regionId)
        while ({incr = objectStream.uSmart2; incr}() != 0) {
            objectId += incr
            var location = 0
            var locationOffset = 0
            while ({locationOffset = objectStream.uSmart; locationOffset}() != 0) {
                location += locationOffset - 1
                val localX = location shr 6 and 0x3f
                val localY = location and 0x3f
                var height = location shr 12
                val objectData = objectStream.uByte
                val type = objectData shr 2
                val direction = objectData and 0x3
                if (localX < 0 || localX >= 64 || localY < 0 || localY >= 64)
                    continue

                if (objectId == 3700)
                    continue

                if (heightMap != null && heightMap[1][localX][localY] and 0x2 == 2.toByte())
                    height--

                if (height in 0..3)
                    region.spawnObject(GameObject(objectId, Position(absX + localX, absY + localY, height), type, direction, revision), localX, localY, height, true) // Add object to clipping
            }
        }
    }

}