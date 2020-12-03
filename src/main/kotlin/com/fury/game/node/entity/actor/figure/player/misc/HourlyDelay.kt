package com.fury.game.node.entity.actor.figure.player.misc

import com.fury.core.model.node.entity.actor.figure.player.Player
import com.fury.game.node.entity.actor.figure.player.Variables
import com.fury.util.Misc

object HourlyDelay {

    fun fireOnce(player: Player, variable: Variables, hours: Int, runnable: () -> Boolean) : Boolean {
        if (player.vars.getLong(variable) == -1L || Misc.getHoursPassed(player.vars.getLong(variable)) >= hours) {
            if (runnable()) {
                player.vars.set(variable, System.currentTimeMillis())
                return true
            }
        } else {
            player.message("You must wait another ${(hours - Misc.getHoursPassed(player.vars.getLong(variable)))} hours before doing that again.")
        }
        return false
    }
}