package com.fury.game.content.tasks

import com.fury.Stopwatch
import com.fury.core.model.node.entity.actor.figure.player.Player
import com.fury.core.task.TickableTask
import com.fury.game.GameSettings
import com.fury.util.Utils
import java.util.concurrent.TimeUnit

class PlayerRemovalTask(private val player: Player) : TickableTask() {

    private val stopwatch = Stopwatch.start()
    private var flag = false

    override fun tick() {
        if(player.isDead)//TODO replace with boolean
            return

        if(player.combat.attackedByDelay + GameSettings.COMBAT_LOGOUT_TIME > Utils.currentTimeMillis() && stopwatch.elapsedTime(TimeUnit.SECONDS) < 60)
            return

        if(!flag) {
            player.deregister()
            flag = true
            return
        }

        if (player.finished)
            stop()
    }

    override fun onCancel(logout: Boolean) {
        player.save(true)
    }

}