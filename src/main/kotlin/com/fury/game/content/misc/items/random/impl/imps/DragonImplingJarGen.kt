package com.fury.game.content.misc.items.random.impl.imps

import com.fury.game.content.misc.items.random.RandomItem
import com.fury.game.content.misc.items.random.RandomItemTable
import com.fury.game.content.misc.items.random.impl.BasicItemGenerator

object DragonImplingJarGen : BasicItemGenerator() {
    private val common = RandomItemTable("Common", 0.966, RandomItem(11212, 100, 350), RandomItem(9341, 3, 40), RandomItem(1305), RandomItem(11232, 105, 350), RandomItem(11237, 90, 350), RandomItem(9193, 10, 49), RandomItem(535, 103, 300))
    private val uncommon = RandomItemTable("Uncommon", 0.02, RandomItem(4093), RandomItem(1705, 2, 3), RandomItem(1703, 2, 3), RandomItem(5698, 3), RandomItem(11230, 104, 350), RandomItem(5316), RandomItem(537, 50, 100), RandomItem(1616, 3, 6))
    private val rare = RandomItemTable("Rare", 0.014, RandomItem(5300, 6), RandomItem(7219, 15))

    init {
        tables.addAll(arrayOf(common, uncommon, rare))
        checkTables()
    }
}