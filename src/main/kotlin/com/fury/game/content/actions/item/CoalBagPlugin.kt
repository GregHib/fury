package com.fury.game.content.actions.item

import com.fury.cache.Revision
import com.fury.core.action.ItemActionEvent
import com.fury.core.action.actions.ItemOnItemAction
import com.fury.core.model.node.entity.actor.figure.player.Player
import com.fury.core.model.item.Item
import com.fury.game.node.entity.actor.figure.player.Variables

class CoalBagPlugin : ItemActionEvent() {

    override fun firstOption(player: Player, item: Item, slot: Int): Boolean {
        if(item.isEqual(18339, Revision.RS2)) {
            player.message("There is ${player.vars.getInt(Variables.COAL_BAG_STORAGE)} coal in the bag.")
            return true
        }
        return false
    }

    override fun fourthOption(player: Player, item: Item, slot: Int): Boolean {
        if(item.isEqual(18339, Revision.RS2)) {
            if(player.inventory.hasRoom()) {
                if(player.vars.getInt(Variables.COAL_BAG_STORAGE) > 0) {
                    player.vars.remove(Variables.COAL_BAG_STORAGE, 1)
                    player.inventory.add(Item(453))
                } else
                    player.message("Your coal bag is empty.")
            } else
                player.inventory.full()
            return true
        }
        return false
    }

    override fun thirdOption(player: Player, item: Item, slot: Int): Boolean {
        if(item.isEqual(18339, Revision.RS2)) {
            if(player.inventory.hasRoom()) {
                val space = player.inventory.spaces
                val amount = player.vars.getInt(Variables.COAL_BAG_STORAGE)
                val remove = Math.min(space, amount)
                if(remove > 0) {
                    player.vars.remove(Variables.COAL_BAG_STORAGE, remove)
                    player.inventory.add(Item(453, remove))
                } else
                    player.message("Your coal bag is empty")
            } else
                player.inventory.full()
            return true
        }
        return false
    }

    override fun itemOnItem(player: Player, action: ItemOnItemAction): Boolean {
        if(check(action, first = 18339, second = 453)) {
            player.inventory.items.forEachIndexed { index, item ->
                if(item != null && item.id == 453) {
                    if(player.vars.getInt(Variables.COAL_BAG_STORAGE) < 81) {
                        player.inventory.delete(index)
                        player.vars.add(Variables.COAL_BAG_STORAGE, 1)
                    } else {
                        player.message("Your coal bag is full.")
                        return true
                    }
                }
            }
            return true
        }
        return false
    }
}