package com.fury.game.content.misc.items.random.impl.imps

import com.fury.game.content.misc.items.random.RandomItemTable
import com.fury.game.content.misc.items.random.impl.BasicItemGenerator

object BabyImplingJarGen : BasicItemGenerator() {
    private val common = RandomItemTable("Common", 0.7, 1755, 1734, 1733, 946, 1985, 2347, 1759)
    private val uncommon = RandomItemTable("Uncommon", 0.2, 1927, 319, 2007, 1779, 7170, 401, 1438)
    private val rare = RandomItemTable("Rare", 0.1, 2355, 1607, 1743, 379, 1761)

    init {
        tables.addAll(arrayOf(common, uncommon, rare))
        checkTables()
    }
}