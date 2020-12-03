package com.fury.game.content.misc.objects

import com.fury.cache.def.item.ItemDefinition
import com.fury.core.model.item.Item
import com.fury.core.model.node.entity.actor.figure.player.Player
import com.fury.game.entity.`object`.GameObject
import com.fury.game.node.entity.actor.`object`.TempObjectManager
import com.fury.game.world.GameWorld

class MuddyChest {
    enum class RewardGroup(vararg val rewards: Item) {
        FOOD(
                Item(15273, 5),
                Item(3145, 5),
                Item(386, 10),
                Item(7947, 15)
        ),
        GEMS(
                Item(1632, 2),
                Item(1618, 3),
                Item(1620, 5),
                Item(1622, 7)
        ),
        BARS(
                Item(2364, 3),
                Item(2362, 5),
                Item(2360, 10),
                Item(2354, 12)
        ),
        RUNES(
                Item(7937, 250),
                Item(565, 100),
                Item(560, 200),
                Item(562, 250)
        )
    }

    companion object {
        @JvmStatic
        fun generateReward(): RewardGroup {
            val num: Int = (Math.random() * RewardGroup.values().size).toInt()
            return RewardGroup.values()[num]
        }

        @JvmStatic
        fun determineRequiredSlots(group: RewardGroup): Int {
            var slots = 0
            for(item in group.rewards)
                slots += if(item.getDefinition().isStackable) 1 else item.amount
            return slots
        }

        @JvmStatic
        fun run(player: Player, obj: GameObject): Boolean {
            if(obj.id != 170)
                return false
            if (!player.timers.clickDelay.elapsed(3000))
                return true

            val muddyKey = Item(991)

            if (player.inventory.contains(muddyKey)) {
                val rewardGroup = MuddyChest.generateReward()
                val freeSpaces = player.inventory.spaces
                if (freeSpaces >= MuddyChest.determineRequiredSlots(rewardGroup) - 1) {
                    player.animate(827)
                    player.message("You attempt to open the chest..")
                    GameWorld.schedule(2) {
                        player.inventory.delete(muddyKey)
                        player.inventory.add(*rewardGroup.rewards)
                        player.message("..and find some "+rewardGroup.name.toLowerCase()+"!")
                        TempObjectManager.sendObjectTemporary(player, GameObject(171, obj, 10, 3), 4000, false, true)
                    }
                } else {
                    player.message("You do not have enough space in your inventory.")
                }
            } else
                player.message("You do not have the correct key to open this chest.")

            player.timers.clickDelay.reset()
            return true
        }
    }
}