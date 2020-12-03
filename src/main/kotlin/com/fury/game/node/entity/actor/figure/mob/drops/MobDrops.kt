package com.fury.game.node.entity.actor.figure.mob.drops

import com.fury.cache.Revision
import com.fury.game.entity.character.npc.drops.Drop
import com.fury.game.system.files.Resources
import java.io.RandomAccessFile
import java.nio.channels.FileChannel
import java.util.*

object MobDrops {

    private lateinit var drops: HashMap<Int, Array<Drop>>

    @JvmStatic
    fun init() {
        load()
    }

    @JvmStatic
    fun reload() {
        drops.clear()
        load()
    }

    @JvmOverloads
    @JvmStatic
    fun getDrops(npcId: Int, revision: Revision = Revision.RS2): Array<Drop>? {
        return drops[npcId]
    }

    private fun load() {
        try {
            val file = RandomAccessFile(Resources.getFile("packedDrops"), "r")
            val channel = file.channel
            val buffer = channel.map(FileChannel.MapMode.READ_ONLY, 0, channel.size())
            val dropCount = buffer.short.toInt() and 0xfff
            drops = HashMap(dropCount)
            for (i in 0 until dropCount) {
                val npcId = buffer.short.toInt() and 0xffff
                val length = buffer.short.toInt() and 0xffff
                val drops = Array(length) { Drop(0, 0.0, 0, 0, true) }
                for(d in drops.indices) {
                    if (buffer.get().toInt() == 0)
                        drops[d] = Drop(buffer.short.toInt() and 0xffff, buffer.double, buffer.int, buffer.int, false)
                }
                MobDrops.drops.put(npcId, drops)
            }
            channel.close()
            file.close()
        } catch (e: Throwable) {
            e.printStackTrace()
        }
    }
}