package com.fury.game.content.misc.items.random

import com.fury.core.model.node.entity.actor.figure.player.Player
import com.fury.core.model.item.Item
import com.fury.util.RandomUtils
import java.math.BigDecimal

abstract class RandomItemGenerator {
    val tables = arrayListOf<RandomItemTable>()

    /**
     * Checks to make sure tables/chances are valid
     */
    protected fun checkTables() {
        if(tables.size <= 0)
            throw IllegalArgumentException("No tables added for ${this.javaClass.simpleName}.")

        var total = BigDecimal.ZERO
        tables.forEach {
            total += it.chance
        }
        if(total < BigDecimal.ONE)
            throw IllegalArgumentException("Chances for all tables in ${this.javaClass.simpleName} must total 1.")
    }

    /**
     * Gets an item from one of the tables based on their chances
     */
    protected fun generate(player: Player) {
        val table = getTable()
        val random = table.getRandomItem()
        if(random != null)
            giveItem(player, random.toItem(), table)
        else
            throw NullPointerException("No random item found (generator table empty?)")
    }

    protected abstract fun giveItem(player: Player, item: Item, table: RandomItemTable)

    /**
     * Get a table based on it's chance
     */
    private fun getTable(): RandomItemTable {
        val rand = RandomUtils.nextDouble()
        var total = BigDecimal(0.0)
        tables.forEach { table ->
            total += table.chance
            if(rand < total.toDouble()) {
                return table
            }
        }
        return tables.first()
    }
}