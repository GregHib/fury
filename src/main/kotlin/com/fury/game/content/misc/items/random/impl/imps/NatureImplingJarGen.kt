package com.fury.game.content.misc.items.random.impl.imps

import com.fury.game.content.misc.items.random.RandomItem
import com.fury.game.content.misc.items.random.RandomItemTable
import com.fury.game.content.misc.items.random.impl.BasicItemGenerator

object NatureImplingJarGen : BasicItemGenerator() {
    private val common = RandomItemTable("Common", 0.7, RandomItem(5100), RandomItem(5104), RandomItem(5281), RandomItem(5294), RandomItem(6016), RandomItem(1513), RandomItem(254, 4))
    private val uncommon = RandomItemTable("Uncommon", 0.2, RandomItem(5286), RandomItem(5285), RandomItem(3000), RandomItem(5974), RandomItem(5297), RandomItem(5299), RandomItem(5298, 5))
    private val rare = RandomItemTable("Rare", 0.07, 5313, 5304, 5295)
    private val veryRare = RandomItemTable("Very rare", 0.03, RandomItem(270, 2), RandomItem(5303))

    init {
        tables.addAll(arrayOf(common, uncommon, rare, veryRare))
        checkTables()
    }
}