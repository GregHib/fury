package com.fury.game.node.entity.actor.figure.mob.drops

import com.fury.game.container.impl.equip.Equipment
import com.fury.game.container.impl.equip.Slot
import com.fury.core.model.node.entity.actor.figure.player.Player
import com.fury.core.model.item.Item
import com.fury.util.RandomUtils

object RareDropTable {
    private val common = arrayOf(Item(3143, 200), Item(1232, 50), Item(1276, 100), Item(5627, 1000), Item(1707, 35), Item(11123, 30), Item(9432, 100), Item(11257, 2), Item(5303, 25), Item(239, 200), Item(220, 10), Item(7937, 5000), Item(11237, 200), Item(12183, 10000), Item(12183, 20000), Item(7945, 250), Item(452, 30), Item(2996, 40), Item(2, 2000), Item(18831, 35), Item(224, 1000), Item(537, 100))
    private val rare = arrayOf(Item(6690, 1000), Item(443, 500), Item(990, 5), Item(20712), Item(6730, 15), Item(4092, 50), Item(6571), Item(8789, 2), Item(8787, 3), Item(8785, 4), Item(5304, 10))

    fun hitTable(player: Player) : Boolean {
        var chance = 1000
        if(Equipment.wearingRingOfWealth(player))
            chance -= 100
        if(player.equipment.get(Slot.RING).id == 15014)
            chance -= 100
        if(player.equipment.get(Slot.RING).id == 15398)
            chance -= 100
        if(player.equipment.get(Slot.RING).id == 23643)
            chance -= 100
        return RandomUtils.inclusive(chance) == 0
    }

    fun getDrop(player: Player): Item {
        //Ring of wealth has less chance of hitting rare drop table than without, however increases chance of hitting by 10%
        val rare = when {
            player.equipment.get(Slot.RING).id == 23643 -> RandomUtils.success(0.77)
            player.equipment.get(Slot.RING).id == 15398 -> RandomUtils.success(0.72)
            player.equipment.get(Slot.RING).id == 15014 -> RandomUtils.success(0.66)
            Equipment.wearingRingOfWealth(player) -> RandomUtils.success(0.33)
            else -> RandomUtils.success(0.5)
        }

        return if(rare) RandomUtils.random(this.rare) else RandomUtils.random(common)
    }
}