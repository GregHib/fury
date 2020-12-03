package com.fury.game.content.actions.item

import com.fury.core.action.ItemActionEvent
import com.fury.game.content.misc.items.random.impl.PvpArmourBoxGen
import com.fury.core.model.node.entity.actor.figure.player.Player
import com.fury.core.model.item.Item

class SingleItemActionPlugin : ItemActionEvent() {

    override fun firstOption(player: Player, item: Item, slot: Int): Boolean {
        return when(item.id) {
            14691 -> {//Artefact box
                PvpArmourBoxGen.reward(player)
                true
            }
            else -> false
        }
    }
}