package com.fury.game.files.plugin

import com.fury.game.files.plugin.loader.CommandLoader
import com.fury.game.files.plugin.loader.StorageFileLoader

object PluginLoader {
    /**
     * A list of plugin loaders
     */
    val loaders = arrayOf(CommandLoader, StorageFileLoader)

    /**
     * Init all plugin loaders
     */
    @JvmStatic
    fun init() {
        loaders.forEach {
            it.init()
        }
    }
}