package com.fury.game.content.misc.items.random.impl.imps

import com.fury.game.content.misc.items.random.RandomItem
import com.fury.game.content.misc.items.random.RandomItemTable
import com.fury.game.content.misc.items.random.impl.BasicItemGenerator

object GourmetImplingJarGen : BasicItemGenerator() {
    private val common = RandomItemTable("Common", 0.7, 365, 361, 2011, 2327, 1897, 5004, 2007, 5970)
    private val uncommon = RandomItemTable("Uncommon", 0.2, 7946)
    private val rare = RandomItemTable("Rare", 0.1, RandomItem(1883), RandomItem(380, 4), RandomItem(386, 3), RandomItem(7188), RandomItem(5755), RandomItem(10137, 5), RandomItem(7179, 6), RandomItem(374, 3), RandomItem(5406), RandomItem(3145, 2))

    init {
        tables.addAll(arrayOf(common, uncommon, rare))
        checkTables()
    }
}