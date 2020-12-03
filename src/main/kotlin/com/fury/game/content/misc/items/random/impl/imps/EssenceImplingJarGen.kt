package com.fury.game.content.misc.items.random.impl.imps

import com.fury.game.content.misc.items.random.RandomItem
import com.fury.game.content.misc.items.random.RandomItemTable
import com.fury.game.content.misc.items.random.impl.BasicItemGenerator

object EssenceImplingJarGen : BasicItemGenerator() {
    private val common = RandomItemTable("Common", 0.7, RandomItem(7937, 20, 35), RandomItem(555, 30), RandomItem(556, 30), RandomItem(554, 50), RandomItem(558, 25), RandomItem(559, 28), RandomItem(562, 4), RandomItem(1448))
    private val uncommon = RandomItemTable("Uncommon", 0.2, RandomItem(4699, 4), RandomItem(4698, 4), RandomItem(4697, 4), RandomItem(4694, 4), RandomItem(564, 4))
    private val rare = RandomItemTable("Rare", 0.1, RandomItem(560, 13), RandomItem(563, 13), RandomItem(565, 7), RandomItem(566, 11), RandomItem(561, 13))

    init {
        tables.addAll(arrayOf(common, uncommon, rare))
        checkTables()
    }
}