package com.fury.game.files.plugin

import com.fury.Config
import com.fury.game.GameSettings
import org.apache.commons.lang3.StringUtils
import java.io.File
import java.util.*

abstract class PluginFileLoader<T> : PluginClassLoader {

    lateinit var files: List<T>

    abstract fun init()

    override fun init(directory: String, name: String) {
        val startup = System.currentTimeMillis()
        files = getClassesInDirectory(directory)
        if (GameSettings.DEBUG)
            println("Loaded " + files.size + " $name in " + (System.currentTimeMillis() - startup) + "ms")
        else if(files.isEmpty())
            println("No files found for $name $directory.")
    }

    private fun getClassesInDirectory(`package`: String): List<T> {
        val classes = ArrayList<T>()
        if(GameSettings.HOSTED) {
            classes.addAll(loadFiles("${Config.BUILD_DIR}java/main/", `package`))
            classes.addAll(loadFiles("${Config.BUILD_DIR}kotlin/main/", `package`))
        } else {
            classes.addAll(loadFiles(Config.BUILD_DIR, `package`))
        }

        return classes
    }

    private fun loadFiles(directory: String, `package`: String): ArrayList<T> {
        val classes = ArrayList<T>()
        val files = File("$directory${StringUtils.replace(`package`, ".", "/")}").listFiles() ?: return classes
        files
                .asSequence()
                .filter {!it.name.contains("$") }
                .forEach {
                    if (it.isDirectory) {
                        var sub = it.path.substring(directory.length, it.path.length)
                        sub = StringUtils.replace(sub, "\\", ".")
                        sub = StringUtils.replace(sub, "/", ".")
                        classes.addAll(getClassesInDirectory(sub))
                    } else {
                        try {
                            val c = Class.forName("$`package`.${it.name.substring(0, it.name.length - 6)}")
                            val instance = c.getDeclaredConstructor().newInstance()

                            @Suppress("UNCHECKED_CAST")
                            val ca: T? = instance as? T
                            if(ca != null)
                                classes.add(ca)
                        } catch (e: InstantiationException) {
                            e.printStackTrace()
                        } catch (e: IllegalAccessException) {
                            e.printStackTrace()
                        } catch (e: ClassNotFoundException) {
                            e.printStackTrace()
                        }

                    }
                }
        return classes
    }
}