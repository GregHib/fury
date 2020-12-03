package com.fury.core.action

import com.fury.core.action.actions.ItemOnObjectAction
import com.fury.core.action.actions.ObjectOptionAction
import com.fury.core.event.Event
import com.fury.core.event.PlayerEventListener
import com.fury.game.entity.`object`.GameObject
import com.fury.core.model.node.entity.actor.figure.player.Player
import com.fury.util.Logger

open class ObjectActionEvent : PlayerEventListener {

    open fun firstOption(player: Player, gameObject: GameObject): Boolean {
        return false
    }

    open fun secondOption(player: Player, gameObject: GameObject): Boolean {
        return false
    }

    open fun thirdOption(player: Player, gameObject: GameObject): Boolean {
        return false
    }

    open fun fourthOption(player: Player, gameObject: GameObject): Boolean {
        return false
    }

    open fun fifthOption(player: Player, gameObject: GameObject): Boolean {
        return false
    }

    open fun itemOnObject(player: Player, action: ItemOnObjectAction): Boolean {
        return false
    }

    override fun accept(player: Player, event: Event): Boolean {
        try {
            when (event) {
                is ItemOnObjectAction -> return itemOnObject(player, event)
                is ObjectOptionAction -> return when(event.option) {
                    5 -> fifthOption(player, event.gameObject)
                    4 -> fourthOption(player, event.gameObject)
                    3 -> thirdOption(player, event.gameObject)
                    2 -> secondOption(player, event.gameObject)
                    else -> {
                        firstOption(player, event.gameObject)
                    }
                }
                else -> {
                }
            }
        } catch (ex: Exception) {
            Logger.handle("player=${player.username} error while handling event ${event::class.simpleName}", ex)
        }
        return false
    }
}