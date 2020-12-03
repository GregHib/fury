package com.fury.game.content.misc.items.random.impl.imps

import com.fury.game.content.misc.items.random.RandomItem
import com.fury.game.content.misc.items.random.RandomItemTable
import com.fury.game.content.misc.items.random.impl.BasicItemGenerator

object EclecticImplingJarGen : BasicItemGenerator() {
    private val common = RandomItemTable("Common", 0.7, RandomItem(1273), RandomItem(5970), RandomItem(231), RandomItem(556, 30, 58), RandomItem(8779, 4), RandomItem(4527), RandomItem(444), RandomItem(2357), RandomItem(237))
    private val uncommon = RandomItemTable("Uncommon", 0.2, 2359)
    private val rare = RandomItemTable("Rare", 0.1, RandomItem(1199), RandomItem(2487), RandomItem(10083), RandomItem(1213), RandomItem(1391), RandomItem(450, 10), RandomItem(5760, 2), RandomItem(7208), RandomItem(5321, 3), RandomItem(1601))

    init {
        tables.addAll(arrayOf(common, uncommon, rare))
        checkTables()
    }
}