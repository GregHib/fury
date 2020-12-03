package com.fury.game.content.actions.obj

import com.fury.core.action.ObjectActionEvent
import com.fury.core.action.actions.ItemOnObjectAction
import com.fury.core.model.item.Item
import com.fury.core.model.node.entity.actor.figure.player.Player

class FeroAnvilPlugin: ObjectActionEvent() {
    companion object {
        private val feroRing5 = Item(15398)
        private val feroRing1 = Item(15402)
        private val hammer = Item(14478)
        private val anvilId = 40200

    }

    override fun itemOnObject(player: Player, action: ItemOnObjectAction): Boolean {
        if(action.gameObject.id != anvilId)
            return false

        if(!action.item.isEqual(feroRing1))
            player.message("You must use a fero ring with this object.")
        else if(!player.hasItem(hammer))
            player.message("You must have a blast fusion hammer to do this.")
        else {
            player.movement.lock(3)
            player.animate(10758)
            player.inventory.delete(feroRing1)
            player.inventory.delete(hammer)
            player.inventory.add(feroRing5)
            player.message("Forging your fero ring made it stronger!")
        }
        return true;
    }
}