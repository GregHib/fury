package com.fury.game.content.actions.item

import com.fury.core.action.ItemActionEvent
import com.fury.core.action.actions.ItemOnItemAction
import com.fury.core.model.item.Item
import com.fury.core.model.node.entity.actor.figure.player.Player

class FrozenKeyPlugin : ItemActionEvent() {
    companion object {
        private val requiredRings = setOf(Item(15018), Item(15019), Item(15220), Item(15020), Item(15014))
        private val frozenKey = Item(20120)
    }

    override fun itemOnItem(player: Player, action: ItemOnItemAction): Boolean {
        if(!action.itemUsed.isEqual(frozenKey) || requiredRings.filter { action.itemOn.isEqual(it) }.none())
            return false;

        val requiredRings = requiredRings.filter { !player.hasItem(it) }.toCollection(mutableSetOf())
        if(requiredRings.size > 0) {
            val missingRings = requiredRings.map(Item::name).joinToString(", ", "(", ")")
            player.message("You are missing some rings... $missingRings")
        } else {
            FrozenKeyPlugin.requiredRings.forEach { player.inventory.delete(it) }
            player.inventory.delete(frozenKey)
            player.inventory.add(Item(15402))
            player.message("You combined the rings.")
        }

        return true;
    }
}