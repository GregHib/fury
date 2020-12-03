package com.fury.game.node.entity.actor.figure.player

import com.fury.core.model.node.entity.actor.figure.player.Player
import com.fury.game.files.plugin.loader.StorageFileLoader
import java.util.concurrent.atomic.AtomicBoolean

class PlayerSave(private val player: Player) : Runnable {
    private val saved = AtomicBoolean(false)
    @get:JvmName("isDisabled")
    var disabled: Boolean = false

    override fun run() {
        if (disabled)
            return

        Thread({ StorageFileLoader.save(player)}).start()
        saved.set(true)
    }
}