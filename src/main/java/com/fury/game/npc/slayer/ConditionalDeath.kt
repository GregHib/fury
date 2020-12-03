package com.fury.game.npc.slayer

import com.fury.core.model.item.Item
import com.fury.core.model.node.entity.actor.figure.mob.Mob
import com.fury.core.model.node.entity.actor.figure.player.Player
import com.fury.game.node.entity.mob.combat.ConditionalDeathCombat
import com.fury.game.world.map.Position

class ConditionalDeath(var requiredItem: IntArray, private val deathMessage: String?, var checkInventory: Boolean, id: Int, position: Position, spawned: Boolean) : Mob(id, position, spawned) {

    var lastLegs: Boolean = false

    constructor(requiredItem: Int, deathMessage: String?, checkInventory: Boolean, id: Int, tile: Position, spawned: Boolean) : this(intArrayOf(requiredItem), deathMessage, checkInventory, id, tile, spawned)

    private fun removeItem(player: Player): Boolean {
        if (!checkInventory)
            return true
        for (requiredItem in requiredItem) {
            val required = Item(requiredItem, 1)
            if (player.inventory.containsAmount(required)) {
                val defs = required.getDefinition()
                if (checkInventory && defs.isStackable)
                    player.inventory.delete(required)
                return true
            }
        }
        return false
    }

    fun useHammer(player: Player): Boolean {
        if (isDead || !lastLegs)
            return false

        if (removeItem(player)) {
            if (deathMessage != null)
                player.message(deathMessage)
            if (id == 14849)
                player.animate(15845)
            health.hitpoints = 0
            (combat as? ConditionalDeathCombat)?.finishHim(player)
            lastLegs = false
            return true
        }

        return false
    }
}
