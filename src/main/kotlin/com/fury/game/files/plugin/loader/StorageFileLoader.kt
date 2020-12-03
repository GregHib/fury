package com.fury.game.files.plugin.loader

import com.fury.Main
import com.fury.core.model.node.entity.actor.figure.player.Player
import com.fury.game.files.plugin.PluginFileLoader
import com.fury.game.system.files.save.StorageFile
import com.fury.util.Logger
import com.google.gson.GsonBuilder
import com.google.gson.JsonNull
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import java.io.*
import java.lang.reflect.InvocationTargetException
import java.util.logging.Level

object StorageFileLoader : PluginFileLoader<StorageFile>() {

    private const val characterFileIndex = 1

    override fun init() {
        init("com.fury.game.system.files.save.impl", "save files")
    }

    fun save(player: Player) {
        files.forEach { file ->
            try {
                save(player, file)
            } catch (e: Throwable) {
                println("Error saving player: " + player.username + " file: " + file::class.simpleName)
                Logger.handle(e)
            }
        }
    }

    private fun save(player: Player, file: StorageFile) {
        try {
            val directory = getDirectory(player, file)
            val obj = createSave(player, file)
            save(directory, obj)
        } catch (e: NoSuchMethodException) {
            e.printStackTrace()
        } catch (e: IllegalAccessException) {
            e.printStackTrace()
        } catch (e: InvocationTargetException) {
            e.printStackTrace()
        } catch (e: InstantiationException) {
            e.printStackTrace()
        }

    }

    private fun save(directory: String, obj: JsonObject) {
        try {
            FileWriter(directory).use { writer ->
                val builder = GsonBuilder().setPrettyPrinting().create()
                builder.toJson(obj, writer)
                writer.close()
            }
        } catch (e: Exception) {
            Main.getLogger().log(Level.WARNING, "An error has occurred while saving file: " + directory, e)
            e.printStackTrace()
        }

    }

    @JvmStatic
    fun load(player: Player): Boolean {
        var loaded = false

        //Figure file always needs to be processed before bank file
        //as bank size is determined by donor status
        try {
            loaded = loaded or load(player, files[characterFileIndex])
        } catch (e: Throwable) {
            Logger.handle(e)
            System.err.println("Error loading player file for: " + player.username)
        }

        files
                .asSequence()
                .filterIndexed { index, _ -> index != characterFileIndex }
                .forEach {
                    try {
                        loaded = loaded or load(player, it)
                    } catch (e: Throwable) {
                        Logger.handle(e)
                        System.err.println("Error loading " + it + " file for: " + player.username)
                    }
                }

        return loaded
    }

    private fun load(player: Player, file: StorageFile): Boolean {
        try {
            val directory = getDirectory(player, file)

            val dir = File(directory)
            if (!dir.exists()) {
                dir.createNewFile()
                setDefaults(player, file)
                return true
            } else {
                try {
                    FileReader(directory).use { fileReader ->
                        val element = JsonParser().parse(fileReader)
                        if (element is JsonNull)
                            return false
                        load(player, file, element as JsonObject)
                        return true
                    }
                } catch (e: FileNotFoundException) {
                    e.printStackTrace()
                } catch (e: IOException) {
                    e.printStackTrace()
                }

            }
        } catch (e: NoSuchMethodException) {
            e.printStackTrace()
        } catch (e: IllegalAccessException) {
            e.printStackTrace()
        } catch (e: InvocationTargetException) {
            e.printStackTrace()
        } catch (e: InstantiationException) {
            e.printStackTrace()
        } catch (e: IOException) {
            e.printStackTrace()
        }

        return false
    }

    @Throws(NoSuchMethodException::class, InvocationTargetException::class, IllegalAccessException::class)
    private fun getDirectory(player: Player, file: StorageFile): String {
        val method = file.javaClass.getDeclaredMethod("getDirectory", Player::class.java)
        return method.invoke(file, player) as String
    }

    @Throws(NoSuchMethodException::class, InvocationTargetException::class, IllegalAccessException::class)
    private fun createSave(player: Player, file: StorageFile): JsonObject {
        val method = file.javaClass.getDeclaredMethod("save", Player::class.java)
        return method.invoke(file, player) as JsonObject
    }

    @Throws(NoSuchMethodException::class, InvocationTargetException::class, IllegalAccessException::class)
    private fun setDefaults(player: Player, file: StorageFile) {
        val method = file.javaClass.getDeclaredMethod("setDefaults", Player::class.java)
        method.invoke(file, player)
    }

    @Throws(NoSuchMethodException::class, InvocationTargetException::class, IllegalAccessException::class)
    private fun load(player: Player, file: StorageFile, obj: JsonObject) {
        val method = file.javaClass.getDeclaredMethod("load", Player::class.java, JsonObject::class.java)
        method.invoke(file, player, obj)
    }
}