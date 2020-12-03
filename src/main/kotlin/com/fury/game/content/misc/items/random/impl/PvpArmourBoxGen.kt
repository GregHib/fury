package com.fury.game.content.misc.items.random.impl

import com.fury.core.model.item.Item
import com.fury.core.model.node.entity.actor.figure.player.Player
import com.fury.game.content.global.Achievements
import com.fury.game.content.misc.items.random.RandomItem
import com.fury.game.content.misc.items.random.RandomItemGenerator
import com.fury.game.content.misc.items.random.RandomItemTable
import com.fury.game.world.GameWorld
import com.fury.util.Misc

object PvpArmourBoxGen : RandomItemGenerator() {
    private val common = RandomItemTable("Common", 0.70, 14892, 14891, 14890, 14889, 14888, 14887, 14886, 14885, 14884, 14883, 14882, 14881, 14880, 14879, 14878)
    private val uncommon = RandomItemTable("Uncommon", 0.25, RandomItem(13961), RandomItem(13957, 15, 50), RandomItem(13953, 15, 50), RandomItem(13964), RandomItem(13985), RandomItem(14877), RandomItem(13988), RandomItem(13967), RandomItem(13938), RandomItem(13970), RandomItem(13950), RandomItem(13879, 15, 50), RandomItem(13958), RandomItem(13982), RandomItem(13883), RandomItem(13976), RandomItem(13864), RandomItem(13935), RandomItem(13929), RandomItem(13947), RandomItem(13914), RandomItem(13876), RandomItem(13932), RandomItem(13973), RandomItem(13917), RandomItem(13979), RandomItem(13944), RandomItem(13908), RandomItem(14876))
    private val rare = RandomItemTable("Rare", 0.05, 13920, 13861, 13911, 13858, 13890, 13873, 13941, 13926, 13893, 13905, 13870, 13884, 13896, 13887, 13923, 13902, 13867, 13899)

    init {
        tables.addAll(arrayOf(common, uncommon, rare))
        checkTables()
    }

    fun reward(player: Player) {
        if(player.inventory.delete(Item(14691)))
            generate(player)
    }

    override fun giveItem(player: Player, item: Item, table: RandomItemTable) {
        player.inventory.addSafe(item)
        Achievements.doProgress(player, Achievements.AchievementData.OPEN_10_PVP_BOXES)
        if(table === rare)
            GameWorld.sendBroadcast("${player.username} just received ${Misc.anOrA(item.name)} ${item.name.toLowerCase()} from a pvp box!")
    }
}