package com.fury.core.engine.sync

import com.fury.core.model.node.entity.actor.figure.mob.Mob
import com.fury.core.model.node.entity.actor.figure.player.Player
import com.fury.game.node.entity.EntityList

interface ClientSynchronizer {
    fun synchronize(players: EntityList<Player>, mobs: EntityList<Mob>)
}