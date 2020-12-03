package com.fury.game.content.misc.items.random.impl.imps

import com.fury.game.content.misc.items.random.RandomItem
import com.fury.game.content.misc.items.random.RandomItemTable
import com.fury.game.content.misc.items.random.impl.BasicItemGenerator

object EarthImplingJarGen : BasicItemGenerator() {
    private val common = RandomItemTable("Common", 0.7, RandomItem(1442), RandomItem(1440), RandomItem(5535), RandomItem(557, 32), RandomItem(447, 1, 3), RandomItem(237), RandomItem(2353), RandomItem(1273), RandomItem(5311, 2), RandomItem(5104, 2), RandomItem(6033, 6))
    private val uncommon = RandomItemTable("Uncommon", 0.2, RandomItem(6035, 2), RandomItem(1784, 4), RandomItem(5294, 2), RandomItem(454, 2))
    private val rare = RandomItemTable("Rare", 0.1, RandomItem(444), RandomItem(1622, 2), RandomItem(1606, 2), RandomItem(1603))

    init {
        tables.addAll(arrayOf(common, uncommon, rare))
        checkTables()
    }
}