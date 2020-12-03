package com.fury.core.action

import com.fury.core.event.PlayerEventListener
import com.fury.game.GameSettings
import org.reflections.Reflections


object ActionManager {

    private enum class PlayerActionTypes(val dir: String, val c: Class<out PlayerEventListener>) {
        ITEMS("com.fury.game.content.actions.item", ItemActionEvent::class.java),
        OBJECTS("com.fury.game.content.actions.obj", ObjectActionEvent::class.java)
    }

    @JvmStatic
    fun init() {
        val startup = System.currentTimeMillis()
        var total = 0
        for(type in PlayerActionTypes.values()) {
            val reflections = Reflections(type.dir)
            val actions = reflections.getSubTypesOf(type.c)
            actions.forEach { action ->
                val listener = action.getConstructor().newInstance()
                PlayerActionBus.subscribe(listener)
            }
            total += actions.size
        }

        if (GameSettings.DEBUG)
            println("Loaded $total player actions in ${System.currentTimeMillis() - startup}ms")
    }
}