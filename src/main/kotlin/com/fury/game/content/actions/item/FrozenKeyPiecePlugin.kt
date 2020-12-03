package com.fury.game.content.actions.item

import com.fury.core.action.ItemActionEvent
import com.fury.core.model.item.Item
import com.fury.core.model.node.entity.actor.figure.player.Player

class FrozenKeyPiecePlugin : ItemActionEvent() {
    companion object {
        private val allKeyPieces = setOf(Item(20121), Item(20122), Item(20123), Item(20124))
    }

    override fun firstOption(player: Player, item: Item, slot: Int): Boolean {
        if(!allKeyPieces.filter(item::isEqual).any())
            return false

        if(allKeyPieces.filter(player::hasItem).count() == allKeyPieces.size) {
            allKeyPieces.forEach { player.inventory.delete(it) }
            player.inventory.add(Item(20120))
            player.message("You assembled the pieces to form a frozen key!")
        } else {
            player.message("You do not have all the required key pieces!")
        }
        return true;
    }
}