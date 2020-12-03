package com.fury.game.node.entity.actor.figure.player.misc.redo

import com.fury.core.model.node.entity.actor.figure.player.Player
import com.fury.util.Colours
import java.util.concurrent.CopyOnWriteArrayList

class KillsTracker {
    val kills = CopyOnWriteArrayList<KillsEntry>()

    class KillsEntry(val npcName: String, var amount: Int, var boss: Boolean)

    companion object {

        @JvmStatic
        fun submit(player: Player, kills: Array<KillsEntry?>) {
            for (kill in kills)
                if (kill != null)
                    submit(player, kill)
        }

        @JvmStatic
        fun submit(player: Player, kill: KillsEntry) {
            val index = getIndex(player, kill)
            if (index >= 0)
                player.killsTracker.kills[index].amount += kill.amount
            else
                player.killsTracker.kills.add(kill)
        }

        @JvmStatic
        fun open(player: Player) {
            try {
                /* RESORT THE KILLS */
                player.killsTracker.kills.sortWith(Comparator { kill1, kill2 ->
                    kill2.amount - kill1.amount
                })
                /* SHOW THE INTERFACE */
                resetInterface(player)
                player.packetSender.sendString(637, "Kill Log (Sorted highest first)").sendInterface(8134)
                player.packetSender.sendString(8147, "Boss Kills", Colours.DRE)
                var index = 1
                for (entry in player.killsTracker.kills) {
                    if (entry.boss) {
                        val toSend = if (8147 + index > 8196) 12174 + index else 8147 + index
                        player.packetSender.sendString(toSend, entry.npcName + ": " + entry.amount)
                        index++
                    }
                }

                index += 1
                player.packetSender.sendString(8147 + index, "Other Kills", Colours.DRE)
                index++
                for (entry in player.killsTracker.kills) {
                    if (entry.boss)
                        continue
                    val toSend = if (8147 + index > 8196) 12174 + index else 8147 + index
                    player.packetSender.sendString(toSend, entry.npcName + ": " + entry.amount)
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

        fun getIndex(player: Player, kill: KillsEntry): Int {
            for (i in 0 until player.killsTracker.kills.size)
                if (player.killsTracker.kills[i].npcName == kill.npcName)
                    return i
            return -1
        }
    }

}
