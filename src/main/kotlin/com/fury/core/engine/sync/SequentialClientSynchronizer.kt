package com.fury.core.engine.sync

import com.fury.core.engine.sync.task.*
import com.fury.core.model.node.entity.actor.figure.mob.Mob
import com.fury.core.model.node.entity.actor.figure.player.Player
import com.fury.game.node.entity.EntityList

class SequentialClientSynchronizer : ClientSynchronizer {

    override fun synchronize(players: EntityList<Player>, mobs: EntityList<Mob>) {
        players.forEach { player -> PlayerPreUpdateTask(player).run() }
        mobs.forEach { npc -> MobPreUpdateTask(npc).run() }

        players.forEach { player -> PlayerUpdateTask(player).run() }
        players.forEach { player -> MobUpdateTask(player).run() }

        players.forEach { player -> PlayerPostUpdateTask(player).run() }
        mobs.forEach { npc -> MobPostUpdateTask(npc).run() }
    }

}