package com.fury.game.content.misc.items.random.impl.imps

import com.fury.game.content.misc.items.random.RandomItem
import com.fury.game.content.misc.items.random.RandomItemTable
import com.fury.game.content.misc.items.random.impl.BasicItemGenerator

object MagpieImplingJarGen : BasicItemGenerator() {
    private val common = RandomItemTable("Common", 0.65, RandomItem(1701, 3), RandomItem(1732, 3), RandomItem(2569, 3), RandomItem(3391), RandomItem(4097), RandomItem(5541), RandomItem(1748, 6))
    private val uncommon = RandomItemTable("Uncommon", 0.3, RandomItem(4095), RandomItem(2571, 4), RandomItem(1185))
    private val rare = RandomItemTable("Rare", 0.04, RandomItem(1347), RandomItem(1215), RandomItem(2364, 2), RandomItem(1602, 3, 4), RandomItem(5287), RandomItem(987), RandomItem(985), RandomItem(5300))
    private val veryRare = RandomItemTable("Very rare", 0.01, RandomItem(993))

    init {
        tables.addAll(arrayOf(common, uncommon, rare, veryRare))
        checkTables()
    }
}