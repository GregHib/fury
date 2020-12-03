package com.fury.game.content.actions.item

import com.fury.cache.Revision
import com.fury.core.action.ItemActionEvent
import com.fury.core.action.actions.ItemOnItemAction
import com.fury.core.model.node.entity.actor.figure.player.Player
import com.fury.core.model.item.Item
import com.fury.game.node.entity.actor.figure.player.Variables

class GiantPouchPlugin : ItemActionEvent() {

    companion object {
        private val RUNE_ESS = Item(1436)
        private val PURE_ESS = Item(7936)
        private val KEY_RUNE = Variables.GIANT_POUCH_RUNE_ESSENCE
        private val KEY_PURE = Variables.GIANT_POUCH_PURE_ESSENCE
        private val size = 12
        private val pouch = 5514
    }

    private fun getTotal(player: Player): Int {
        return player.vars.getInt(KEY_RUNE) + player.vars.getInt(KEY_PURE)
    }

    private fun check(player: Player): Boolean {
        return getTotal(player) < size
    }

    private fun fill(player: Player, item: Item, key: Variables): Boolean {
        if (player.inventory.contains(item)) {
            if (check(player)) {
                val amount = player.inventory.getAmount(item)
                val space = size - getTotal(player)
                val remove = Math.min(amount, space)

                player.inventory.delete(Item(item, remove))
                player.vars.add(key, remove)
                return true
            }
        }
        return false
    }

    private fun fill(player: Player): Boolean {
        var added = false
        if (fill(player, RUNE_ESS, KEY_RUNE))
            added = true
        if (fill(player, PURE_ESS, KEY_PURE))
            added = true
        return added
    }

    private fun empty(player: Player) {
        if (!player.inventory.hasRoom()) {
            player.message("You don't have enough inventory space to withdraw that many.")
            return
        }

        val esse = player.vars.getInt(KEY_RUNE)
        val pure = player.vars.getInt(KEY_PURE)

        if (pure == 0 && esse == 0) {
            player.message("Your pouch has no essence left in it.")
            return
        }

        var remove = Math.min(player.inventory.spaces, pure)

        if (remove > 0) {
            player.inventory.add(Item(PURE_ESS, remove))
            player.vars.remove(KEY_PURE, remove)
        }

        remove = Math.min(player.inventory.spaces, esse)

        if (remove > 0) {
            player.inventory.add(Item(RUNE_ESS, remove))
            player.vars.remove(KEY_RUNE, remove)
        }

        player.message("You remove some essence from the pouch.", true)
    }

    override fun firstOption(player: Player, item: Item, slot: Int): Boolean {
        if (item.isEqual(pouch, Revision.RS2)) {
            if (fill(player))
                player.message("You place your essence into the pouch.", true)
            else if (player.inventory.contains(RUNE_ESS) || player.inventory.contains(PURE_ESS))
                player.message("This pouch is already full.")
            else
                player.message("You don't have any essence to put into the pouch.")
            return true
        }
        return false
    }

    override fun thirdOption(player: Player, item: Item, slot: Int): Boolean {
        if (item.isEqual(pouch, Revision.RS2)) {
            player.message("Your pouch contains ${player.vars.getInt(KEY_RUNE)} rune essence and ${player.vars.getInt(KEY_PURE)} pure essence.")
            return true
        }
        return false
    }

    override fun fourthOption(player: Player, item: Item, slot: Int): Boolean {
        if (item.isEqual(pouch, Revision.RS2)) {
            empty(player)
            return true
        }
        return false
    }

    override fun itemOnItem(player: Player, action: ItemOnItemAction): Boolean {
        if (check(action, first = pouch, second = RUNE_ESS.id) || check(action, first = pouch, second = PURE_ESS.id)) {
            var added = false
            if (check(action, first = pouch, second = RUNE_ESS.id) && fill(player, RUNE_ESS, KEY_RUNE))
                added = true

            if (check(action, first = pouch, second = PURE_ESS.id) && fill(player, PURE_ESS, KEY_PURE))
                added = true

            player.message(if(added) "You place some of your essence into the pouch." else "This pouch is already full.", added)
            return true
        }

        return false
    }
}