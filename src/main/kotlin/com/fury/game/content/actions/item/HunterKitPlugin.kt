package com.fury.game.content.actions.item

import com.fury.core.action.ItemActionEvent
import com.fury.core.model.item.Item
import com.fury.core.model.node.entity.actor.figure.player.Player

class HunterKitPlugin : ItemActionEvent() {

    companion object {
        val kit = Item(11159)
        val items = arrayOf(Item(10150), Item(10010), Item(10006), Item(10031), Item(10029), Item(596), Item(10008))
    }

    override fun firstOption(player: Player, item: Item, slot: Int): Boolean {
        if(item.isEqual(11159)) {
            if(player.inventory.spaces < 6) {
                player.inventory.full()
                return true
            }
            player.inventory.delete(kit)
            player.inventory.add(*items)
            return true
        }
        return super.firstOption(player, item, slot)
    }
}