package com.fury.game.node.entity.actor.figure.player

import com.fury.core.model.node.entity.actor.figure.Figure
import com.fury.core.model.node.entity.actor.figure.mob.Mob
import com.fury.core.model.node.entity.actor.figure.player.Player
import com.fury.game.content.skill.free.dungeoneering.DungeonController
import com.fury.game.world.GameWorld
import java.util.*

class PlayerViewport(private val player: Player) {
    fun add(figure: Figure): Boolean {
        //TODO simplify
        if (figure is Mob) {
            if (localMobs.contains(figure) || !figure.isVisible || figure.isNeedsPlacement)
                return false

            if (figure.isViewableFrom(player)) {
                localMobs.add(figure)
                return true
            }
        } else if (figure is Player) {
            if (figure === player || localPlayers.contains(figure) || !figure.isViewableFrom(player) && !(figure.controllerManager.controller is DungeonController && figure.dungManager.party.team.contains(player)))
                return false
            localPlayers.add(figure)
            return true
        }

        return false
    }

    fun shouldUpdate(figure: Figure): Boolean {
        if (figure is Mob) {
            return GameWorld.mobs.mobs[figure.index] != null && figure.isVisible && player.isViewableFrom(figure) && !figure.isNeedsPlacement
        } else if(figure is Player)
            return GameWorld.players[figure.index] != null && !figure.isNeedsPlacement && (figure.isViewableFrom(player) || figure.controllerManager.controller is DungeonController && figure.dungManager.party.team.contains(player))

        return false
    }

    val localPlayers: MutableList<Player> = LinkedList()
    val localMobs: MutableList<Mob> = LinkedList()
}