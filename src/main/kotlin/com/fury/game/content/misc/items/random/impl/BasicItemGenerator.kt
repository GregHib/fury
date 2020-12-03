package com.fury.game.content.misc.items.random.impl

import com.fury.core.model.node.entity.actor.figure.player.Player
import com.fury.game.content.misc.items.random.RandomItemGenerator
import com.fury.game.content.misc.items.random.RandomItemTable
import com.fury.core.model.item.Item

open class BasicItemGenerator : RandomItemGenerator() {

    fun reward(player: Player, toDelete: Item) {
        if (player.inventory.spaces < 1) {
            player.message("You need at least 1 free inventory space to loot this.")
            return
        }
        if(player.inventory.delete(toDelete)) {
            player.inventory.addSafe(Item(11260))
            generate(player)
        }
    }

    override fun giveItem(player: Player, item: Item, table: RandomItemTable) {
        player.inventory.addSafe(item)
    }
}