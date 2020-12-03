package com.fury.game.node.entity.actor.figure.mob.drops

import com.fury.core.model.node.entity.actor.figure.mob.Mob
import com.fury.core.model.node.entity.actor.figure.player.Player
import com.fury.game.world.map.Area
import com.fury.util.RandomUtils
import java.util.concurrent.CopyOnWriteArrayList

object Lootshare {
    fun isActive(player: Player): Boolean {
        val clan = player.currentClanChat
        return Area.isMulti(player) && clan != null && clan.lootShare
    }

    fun getRewardee(player: Player, mob: Mob): Player {
        val localPlayers = CopyOnWriteArrayList<Player>()
        player.currentClanChat?.members?.filterTo(localPlayers) { it != null && mob.combat.hits.receivedDamage[it] != null }

        if (localPlayers.size > 0) {
            var toGive: Player? = RandomUtils.random(localPlayers)
            if (toGive == null || toGive.currentClanChat == null || toGive.currentClanChat !== player.currentClanChat)
                toGive = player

            return toGive
        }
        return player
    }
}