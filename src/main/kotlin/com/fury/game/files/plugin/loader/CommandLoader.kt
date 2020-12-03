package com.fury.game.files.plugin.loader

import com.fury.core.model.node.entity.actor.figure.player.Player
import com.fury.game.files.plugin.PluginFileLoader
import com.fury.game.system.communication.commands.Command
import com.fury.game.system.communication.commands.impl.owner.FindCommand
import com.fury.game.system.communication.commands.impl.regular.ForumCommand
import com.fury.game.system.communication.commands.impl.regular.HighscoresCommand
import com.fury.game.system.communication.commands.impl.regular.PromoCommand
import com.fury.util.Misc
import java.lang.reflect.InvocationTargetException
import java.util.*
import java.util.regex.Pattern

object CommandLoader : PluginFileLoader<Command>() {

    override fun init() {
        init("com.fury.game.system.communication.commands.impl", "commands")
    }

    @JvmStatic
    fun getCommands(player: Player): Array<String>? {
        try {
            val commandsList = ArrayList<String>()
            for (command in files) {
                val hasRights = getRights(player, command)
                if (hasRights && command !is PromoCommand) {
                    var className = command.javaClass.simpleName
                    val format = getFormat(command)
                    className = className.substring(0, className.length - 7)
                    commandsList.add(className + " - ::" + (format ?: className.toLowerCase()))
                }
            }
            commandsList.sort()
            return commandsList.toTypedArray()
        } catch (e: Exception) {
            return null
        }

    }

    @JvmStatic
    fun handle(player: Player, text: String) {
        try {
            for (command in files) {
                val prefix = getPrefix(command)
                if (prefix == null || (prefix.endsWith(" ") || command is FindCommand || command is ForumCommand || command is HighscoresCommand) && text.startsWith(prefix) || text == prefix) {
                    //if has rights
                    val hasRights = getRights(player, command)
                    if (hasRights) {
                        //get pattern
                        val pattern = getPattern(command)
                        if (pattern == null && prefix != null) {
                            //process if has no required values
                            process(player, arrayOf(), command)
                            break
                        } else if (pattern != null) {
                            //check matches format
                            val matcher = pattern.matcher(text)
                            if (matcher.matches()) {
                                //process using values
                                val values = Misc.getValues(matcher)
                                process(player, values, command)
                                break
                            } else if (prefix != null) {
                                //tell user correct format
                                val format = getFormat(command)
                                if (format != null)
                                    player.message("Please use the format: " + format)
                                break
                            }
                        }
                    }
                }
            }

        } catch (e: Exception) {
            e.printStackTrace()
            player.message("Error executing that command")
        }

    }

    @Throws(NoSuchMethodException::class, InvocationTargetException::class, IllegalAccessException::class)
    private fun process(player: Player, values: Array<String>, command: Command) {
        val method = command.javaClass.getDeclaredMethod("process", Player::class.java, Array<String>::class.java)
        method.invoke(command, player, values)
    }

    @Throws(NoSuchMethodException::class, InvocationTargetException::class, IllegalAccessException::class)
    private fun getRights(player: Player, command: Command): Boolean {
        val method = command.javaClass.getDeclaredMethod("rights", Player::class.java)
        return method.invoke(command, player) as Boolean
    }

    @Throws(NoSuchMethodException::class, InvocationTargetException::class, IllegalAccessException::class)
    private fun getPrefix(command: Command): String? {
        val method = command.javaClass.getDeclaredMethod("prefix")
        return method.invoke(command) as String?
    }

    @Throws(NoSuchMethodException::class, InvocationTargetException::class, IllegalAccessException::class)
    private fun getFormat(command: Command): String? {
        val method = command.javaClass.getDeclaredMethod("format")
        return method.invoke(command) as String?
    }

    @Throws(NoSuchMethodException::class, InvocationTargetException::class, IllegalAccessException::class)
    private fun getPattern(command: Command): Pattern? {
        val method = command.javaClass.getDeclaredMethod("pattern")
        return method.invoke(command) as Pattern?
    }

    @Throws(NoSuchMethodException::class, InvocationTargetException::class, IllegalAccessException::class)
    private fun matches(command: Command, input: String): Boolean {
        val method = command.javaClass.getDeclaredMethod("matches", String::class.java)
        return method.invoke(command, input) as Boolean
    }

}