package com.fury.game.node.entity.actor.figure.player.misc.redo

import com.fury.cache.def.Loader
import com.fury.core.model.node.entity.actor.figure.player.Player
import com.fury.game.node.entity.actor.figure.mob.drops.DropLogEntry
import com.fury.util.Colours
import java.util.*
import java.util.concurrent.CopyOnWriteArrayList

class DropLog {

    val drops = CopyOnWriteArrayList<DropLogEntry>()

    companion object {

        @JvmStatic
        fun submit(player: Player, drops: Array<DropLogEntry?>) {
            for (drop in drops)
                if (drop != null)
                    submit(player, drop)
        }

        fun submit(player: Player, drop: DropLogEntry) {
            val index = getIndex(player, drop)
            if (index >= 0)
                player.dropLogs.drops[index].amount = player.dropLogs.drops[index].amount + drop.amount
            else if (addItem(player, drop))
                player.dropLogs.drops.add(drop)
        }

        @JvmStatic
        fun open(player: Player) {
            try {
                /* RESORT THE KILLS */
                player.dropLogs.drops.sortWith(Comparator { drop1, drop2 ->
                    val def1 = Loader.getItem(drop1.item)
                    val def2 = Loader.getItem(drop2.item)
                    val value1 = def1.getValue() * drop1.amount
                    val value2 = def2.getValue() * drop2.amount
                    when {
                        value1 > value2 -> -1
                        value2 > value1 -> 1
                        def2.getName() > def2.getName() -> 1
                        else -> -1
                    }
                })
                /* SHOW THE INTERFACE */
                resetInterface(player)
                player.packetSender.sendString(637, "Drop Log (Sorted by value)").sendInterface(8134)
                player.packetSender.sendString(8147, "Rare drops", Colours.DRE)
                var index = 1
                for (entry in player.dropLogs.drops) {
                    if (entry.rareDrop) {
                        val toSend = if (8147 + index > 8196) 12174 + index else 8147 + index
                        player.packetSender.sendString(toSend, Loader.getItem(entry.item).getName() + ": x" + entry.amount + "")
                        index++
                    }
                }
                index += 1
                player.packetSender.sendString(8147 + index, "Common drops", Colours.DRE)
                index++
                for (entry in player.dropLogs.drops) {
                    if (entry.rareDrop)
                        continue
                    val toSend = if (8147 + index > 8196) 12174 + index else 8147 + index
                    player.packetSender.sendString(toSend, Loader.getItem(entry.item).getName() + ": x" + entry.amount + "")
                    index++
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }

        }

        private fun resetInterface(player: Player) {
            for (i in 8145..8195)
                player.packetSender.sendString(i, "")
            for (i in 12174..12223)
                player.packetSender.sendString(i, "")
            player.packetSender.sendString(8136, "Close window")
        }

        private fun addItem(player: Player, drop: DropLogEntry): Boolean {
            if (player.dropLogs.drops.size >= 98) {
                val drop2 = player.dropLogs.drops[player.dropLogs.drops.size - 1]
                val value1 = Loader.getItem(drop.item).getValue() * drop.amount
                val value2 = Loader.getItem(drop2.item).getValue() * drop2.amount
                return if (value1 > value2) {
                    player.dropLogs.drops.remove(drop2)
                    true
                } else
                    false
            }
            return true
        }

        fun getIndex(player: Player, drop: DropLogEntry): Int {
            for (i in 0 until player.dropLogs.drops.size)
                if (player.dropLogs.drops[i].item == drop.item)
                    return i
            return -1
        }
    }

}
