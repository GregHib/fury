package com.fury.game.content.misc.items.random.impl.imps

import com.fury.game.content.misc.items.random.RandomItem
import com.fury.game.content.misc.items.random.RandomItemTable
import com.fury.game.content.misc.items.random.impl.BasicItemGenerator

object KinglyImplingJarGen : BasicItemGenerator() {
    private val common = RandomItemTable("Common", 0.638, RandomItem(1618, 15, 34), RandomItem(1705, 3, 11), RandomItem(990, 2), RandomItem(1703, 3))
    private val uncommon = RandomItemTable("Uncommon", 0.35, RandomItem(1616, 6), RandomItem(15511), RandomItem(15509), RandomItem(15505), RandomItem(15507), RandomItem(15503), RandomItem(11212, 40, 150), RandomItem(11237, 50, 148), RandomItem(9193, 55, 70), RandomItem(9341, 40, 70), RandomItem(9342, 43, 60), RandomItem(1632, 5), RandomItem(11230, 182, 319), RandomItem(11232, 70, 259), RandomItem(1306, 1, 2), RandomItem(1249))
    private val rare = RandomItemTable("Rare", 0.01, RandomItem(9194, 1, 75))
    private val veryRare = RandomItemTable("Very rare", 0.002, RandomItem(2366), RandomItem(6571), RandomItem(7158))

    init {
        tables.addAll(arrayOf(common, uncommon, rare, veryRare))
        checkTables()
    }
}