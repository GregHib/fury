package com.fury.game.content.actions.item

import com.fury.cache.Revision
import com.fury.core.action.ItemActionEvent
import com.fury.core.action.actions.ItemOnItemAction
import com.fury.core.model.node.entity.actor.figure.player.Player
import com.fury.core.model.item.Item

class GemBagPlugin : ItemActionEvent() {

    override fun firstOption(player: Player, item: Item, slot: Int): Boolean {
        if(item.isEqual(18338, Revision.RS2)) {
            player.gemBag.check()
            return true
        }
        return false
    }

    override fun thirdOption(player: Player, item: Item, slot: Int): Boolean {
        if(item.isEqual(18338, Revision.RS2)) {
            player.gemBag.empty()
            return true
        }
        return false
    }

    override fun fourthOption(player: Player, item: Item, slot: Int): Boolean {
        if(item.isEqual(18338, Revision.RS2)) {
            player.gemBag.withdraw()
            return true
        }
        return false
    }

    override fun itemOnItem(player: Player, action: ItemOnItemAction): Boolean {
        if(check(action, first = 18338, second = 1623) || check(action, first = 18338, second = 1619) || check(action, first = 18338, second = 1621) || check(action, first = 18338, second = 1617)) {
            player.gemBag.fill()
            return true
        }
        return false
    }
}