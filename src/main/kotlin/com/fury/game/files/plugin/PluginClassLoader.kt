package com.fury.game.files.plugin

interface PluginClassLoader {
    fun init(directory: String, name: String)
}