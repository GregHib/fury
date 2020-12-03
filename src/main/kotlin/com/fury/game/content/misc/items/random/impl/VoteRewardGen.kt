package com.fury.game.content.misc.items.random.impl

import com.fury.core.model.node.entity.actor.figure.player.Player
import com.fury.engine.task.impl.BonusExperienceTask
import com.fury.game.content.global.Achievements
import com.fury.game.content.misc.items.random.RandomItem
import com.fury.game.content.misc.items.random.RandomItemGenerator
import com.fury.game.content.misc.items.random.RandomItemTable
import com.fury.core.model.item.Item
import com.fury.game.node.entity.actor.figure.player.Points
import com.fury.game.world.GameWorld
import com.fury.util.Misc
import com.fury.util.RandomUtils

object VoteRewardGen : RandomItemGenerator() {
    private val common = RandomItemTable("Common", 0.605, RandomItem(13734), RandomItem(1333), RandomItem(1645), RandomItem(12158, 5, 25), RandomItem(12159, 5, 25), RandomItem(12160, 3, 15), RandomItem(12163, 2, 10), RandomItem(12474), RandomItem(13109), RandomItem(13107), RandomItem(13111), RandomItem(13113), RandomItem(13115), RandomItem(1149), RandomItem(1079), RandomItem(1127), RandomItem(1201), RandomItem(6686, 2, 10), RandomItem(3025, 3, 15))
    private val uncommon = RandomItemTable("Uncommon", 0.35, RandomItem(15262), RandomItem(15267, 25), RandomItem(15273, 10, 20), RandomItem(15332), RandomItem(386, 50, 100), RandomItem(3204), RandomItem(7158), RandomItem(1215), RandomItem(5698), RandomItem(4153), RandomItem(7400), RandomItem(7398), RandomItem(7399), RandomItem(13736), RandomItem(4087), RandomItem(1377), RandomItem(1305), RandomItem(4587), RandomItem(1249), RandomItem(1434))
    private val rare = RandomItemTable("Rare", 0.04, 6914, 6916, 6920, 6922, 6924, 4151, 11728, 6737, 6735, 6733, 6731, 11696, 6570, 6585, 8850)
    private val legendary = RandomItemTable("Legendary", 0.005, 10689, 6739, 13905)

    init {
        tables.addAll(arrayOf(common, uncommon, rare, legendary))
        checkTables()
    }

    @JvmStatic
    fun reward(player: Player) {
        if(player.inventory.delete(Item(20712))) {
            if(RandomUtils.success(0.7)) {
                val amount = RandomUtils.inclusive(100000, 1250000)
                player.inventory.addSafe(Item(995, if(!player.gameMode.isIronMan) amount else amount / 10))
            } else
                generate(player)
            val minutes = if (player.gameMode.isIronMan) 15 else 30
            BonusExperienceTask.addBonusXp(player, minutes)
            player.points.add(Points.VOTING, 1)
            Achievements.doProgress(player, Achievements.AchievementData.VOTE_100_TIMES)
            player.pointsHandler.refreshPanel()
        }
    }

    override fun giveItem(player: Player, item: Item, table: RandomItemTable) {
        player.inventory.addSafe(item)
        if(table === legendary)
            GameWorld.sendBroadcast("${player.username} just received ${Misc.anOrA(item.name)} ${item.name.toLowerCase()} from a vote book!")
    }
}