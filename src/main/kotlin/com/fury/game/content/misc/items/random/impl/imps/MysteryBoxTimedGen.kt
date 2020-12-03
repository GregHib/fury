package com.fury.game.content.misc.items.random.impl.imps

import com.fury.core.model.item.Item
import com.fury.core.model.item.ReschedulableTimer
import com.fury.core.model.node.entity.actor.figure.player.Player
import com.fury.game.content.misc.items.random.RandomItemGenerator
import com.fury.game.content.misc.items.random.RandomItemTable
import com.fury.game.world.GameWorld
import com.fury.util.Misc
import java.util.concurrent.TimeUnit

object MysteryBoxTimedGen : RandomItemGenerator() {
    @JvmStatic
    val boxId = 18768

    private val common = RandomItemTable("Common", 0.605, 11732, 4675, 6570, 1187, 4151, 6920, 6922, 4720, 4753, 4751, 4732, 4710, 15332, 2581, 11235, 4708, 4755, 4734, 4747, 4728, 4724)
    private val uncommon = RandomItemTable("Uncommon", 0.35, 15606, 15610, 15608, 6914, 20671, 6739, 2577, 2579, 15220, 15020, 15018, 15019, 15486, 15241, 4722, 4757, 4745, 4736, 4716, 4712, 11728, 4730)
    private val rare = RandomItemTable("Rare", 0.04, 4714, 4759, 4718, 4726, 4749, 4759, 20786, 13887, 13893, 13902, 13896, 13884, 13890, 13744, 13738, 11698, 11700, 11696)
    private val legendary = RandomItemTable("Legendary", 0.005, 13742, 13740, 1961, 1959, 1050, 1053, 1055, 1057, 20135, 20139, 20143)

    private val activeBoxMap = mutableMapOf<Long?, ReschedulableTimer>()
    private val openStrategy = mutableMapOf<Long?, (Player, Item) -> Unit>()
    private val displayTime: (Player, Item) -> Unit = { p, i ->
        activeBoxMap[p.longUsername]?.let {
            val seconds = it.remainingTime(TimeUnit.SECONDS)
            val minutes = seconds / 60
            val hours = minutes / 60
            val correctMinutes = minutes - hours * 60
            val correctSeconds = seconds - minutes * 60
            p.message("${hours}h ${correctMinutes}m ${correctSeconds}s left... Don't log out!")
        }

    }

    private val openBox: (Player, Item) -> Unit = { player, item ->
        if (player.inventory.delete(item))
            generate(player)

        openStrategy.remove(player.longUsername)
        activeBoxMap.remove(player.longUsername)
    }

    init {
        tables.addAll(arrayOf(common, uncommon, rare, legendary))
        checkTables()
    }

    override fun giveItem(player: Player, item: Item, table: RandomItemTable) {
        player.inventory.addSafe(item)
        if (table === legendary)
            GameWorld.sendBroadcast("${player.username} just received ${Misc.anOrA(item.name)} ${item.name.toLowerCase()} from a timed mystery box!")
    }

    @JvmStatic
    fun attemptToOpen(player: Player, item: Item) {
        openStrategy[player.longUsername]?.invoke(player, item)
    }

    @JvmStatic
    fun newTimerFor(player: Player, item: Item) {
        if (activeBoxMap.containsKey(player.longUsername))
            return;

        openStrategy[player.longUsername] = displayTime
        activeBoxMap[player.longUsername] = ReschedulableTimer(2, TimeUnit.HOURS) {
            player.message("Your timed mystery box is ready to open!")
            openStrategy[player.longUsername] = openBox
        }
    }

    @JvmStatic fun stopTimerFor(player: Player) { activeBoxMap[player.longUsername]?.stop() }
    @JvmStatic fun removeTimerFor(player: Player) {
        stopTimerFor(player);
        activeBoxMap.remove(player.longUsername)
    }

}