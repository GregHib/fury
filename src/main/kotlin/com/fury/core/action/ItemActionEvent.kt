package com.fury.core.action

import com.fury.cache.Revision
import com.fury.core.action.actions.ItemOnItemAction
import com.fury.core.action.actions.ItemOptionAction
import com.fury.core.event.Event
import com.fury.core.event.PlayerEventListener
import com.fury.core.model.node.entity.actor.figure.player.Player
import com.fury.core.model.item.Item
import com.fury.util.Logger

open class ItemActionEvent : PlayerEventListener {

    open fun firstOption(player: Player, item: Item, slot: Int): Boolean {
        return false
    }

    open fun secondOption(player: Player, item: Item, slot:Int): Boolean {
        return false
    }

    open fun thirdOption(player: Player, item: Item, slot:Int): Boolean {
        return false
    }

    open fun fourthOption(player: Player, item: Item, slot:Int): Boolean {
        return false
    }

    open fun fifthOption(player: Player, item: Item, slot:Int): Boolean {
        return false
    }

    open fun itemOnItem(player: Player, action: ItemOnItemAction): Boolean {
        return false
    }

    open fun canEquip(): Boolean {
        return true
    }

    protected fun check(action: ItemOnItemAction, first: Int, firstRev: Revision = Revision.RS2, second: Int, secondRev: Revision = Revision.RS2, needsOrder: Boolean = false): Boolean {
        return (action.itemUsed.isEqual(first, firstRev) && action.itemOn.isEqual(second, secondRev)) || (action.itemUsed.isEqual(second, secondRev) && action.itemOn.isEqual(first, firstRev) && !needsOrder)
    }

    override fun accept(player: Player, event: Event): Boolean {
        try {
            when (event) {
                is ItemOnItemAction -> return itemOnItem(player, event)
                is ItemOptionAction -> return when (event.option) {
                        5 -> fifthOption(player, event.item, event.slot)
                        4 -> fourthOption(player, event.item, event.slot)
                        3 -> thirdOption(player, event.item, event.slot)
                        2 -> secondOption(player, event.item, event.slot)
                        else -> {
                            firstOption(player, event.item, event.slot)
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