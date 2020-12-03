package com.fury.game.content.misc.items.random.impl.imps

import com.fury.game.content.misc.items.random.RandomItem
import com.fury.game.content.misc.items.random.RandomItemTable
import com.fury.game.content.misc.items.random.impl.BasicItemGenerator

object YoungImplingJarGen : BasicItemGenerator() {
    private val common = RandomItemTable("Common", 0.7, RandomItem(1539, 5), RandomItem(1901), RandomItem(7936), RandomItem(1523), RandomItem(361))
    private val uncommon = RandomItemTable("Uncommon", 0.2, 453, 1777, 1353, 1157, 1097, 2293, 247)
    private val rare = RandomItemTable("Rare", 0.06, 2359, 231, 133)
    private val veryRare = RandomItemTable("Very rare", 0.04, 855)

    init {
        tables.addAll(arrayOf(common, uncommon, rare, veryRare))
        checkTables()
    }
}