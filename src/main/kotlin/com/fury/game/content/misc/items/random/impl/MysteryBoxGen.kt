package com.fury.game.content.misc.items.random.impl

import com.fury.core.model.node.entity.actor.figure.player.Player
import com.fury.game.content.misc.items.random.RandomItemGenerator
import com.fury.game.content.misc.items.random.RandomItemTable
import com.fury.core.model.item.Item
import com.fury.game.world.GameWorld
import com.fury.util.Misc

object MysteryBoxGen : RandomItemGenerator() {
    private val common = RandomItemTable("Common", 0.605,11732, 4675, 6570, 1187, 4151, 6920, 6922, 4720, 4753, 4751, 4732, 4710, 15332, 2581, 11235, 4708, 4755, 4734, 4747, 4728, 4724)
    private val uncommon = RandomItemTable("Uncommon", 0.35, 15606, 15610, 15608, 6914, 20671, 6739, 2577, 2579, 15220, 15020, 15018, 15019, 15486, 15241, 4722, 4757, 4745, 4736, 4716, 4712, 11728, 4730)
    private val rare = RandomItemTable("Rare", 0.04, 4714, 4759, 4718, 4726, 4749, 4759, 20786, 13887, 13893, 13902, 13896, 13884, 13890, 13744, 13738, 11698, 11700, 11696)
    private val legendary = RandomItemTable("Legendary", 0.005, 13742, 13740, 962, 1961, 1959, 1050, 1053, 1055, 1057, 20135, 20139, 20143)

    init {
        tables.addAll(arrayOf(common, uncommon, rare, legendary))
        checkTables()
    }

    @JvmStatic
    fun reward(player: Player) {
        if(player.inventory.delete(Item(6199)))
            generate(player)
    }

    override fun giveItem(player: Player, item: Item, table: RandomItemTable) {
        player.inventory.addSafe(item)
        if(table === legendary)
            GameWorld.sendBroadcast("${player.username} just received ${Misc.anOrA(item.name)} ${item.name.toLowerCase()} from a mystery box!")
    }
}