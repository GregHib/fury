package com.fury.core.model.item

import com.fury.core.model.item.Item
import com.fury.core.model.node.entity.actor.figure.player.Player
import com.fury.game.world.map.Position

class FloorItem(val item: Item, val tile: Position, val owner: Player?, val takeable: Boolean, var visible: Boolean) : Item(item.id, item.amount, item.revision) {
    val ownerName = owner?.username ?: ""

    override fun toString(): String {
        return id.toString() + " " + revision + " " + amount + " " + tile + " " + owner
    }

    fun visibleFor(player: Player): Boolean {
        return if (player.gameMode.isIronMan && !player.isInDungeoneering && player.username != ownerName) false else visible || !visible && player.username == ownerName
    }

}
