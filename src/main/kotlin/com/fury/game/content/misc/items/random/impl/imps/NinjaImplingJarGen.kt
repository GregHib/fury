package com.fury.game.content.misc.items.random.impl.imps

import com.fury.game.content.misc.items.random.RandomItem
import com.fury.game.content.misc.items.random.RandomItemTable
import com.fury.game.content.misc.items.random.impl.BasicItemGenerator

object NinjaImplingJarGen : BasicItemGenerator() {
    private val common = RandomItemTable("Common", 0.75, RandomItem(6328), RandomItem(4097), RandomItem(6313), RandomItem(3101), RandomItem(892, 70), RandomItem(811, 70), RandomItem(868, 40), RandomItem(805), RandomItem(1748, 10), RandomItem(140, 4))
    private val uncommon = RandomItemTable("Uncommon", 0.2, RandomItem(3385), RandomItem(1113), RandomItem(1333), RandomItem(5680), RandomItem(9342), RandomItem(5938, 4), RandomItem(6155, 3))
    private val rare = RandomItemTable("Rare", 0.05, RandomItem(9194, 4))

    init {
        tables.addAll(arrayOf(common, uncommon, rare))
        checkTables()
    }
}