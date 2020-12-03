package com.fury.game.content.misc.items

import com.fury.core.model.item.Item
import com.fury.core.model.node.entity.actor.figure.player.Player
import com.fury.game.entity.character.combat.effects.Effects
import com.fury.game.entity.character.combat.prayer.Prayer
import com.fury.game.network.packet.out.Interface
import com.fury.game.network.packet.out.InterfaceString
import com.fury.game.network.packet.out.ItemOnWidget
import com.fury.util.Colours
import com.fury.util.FontUtils
import com.fury.util.Misc
import java.util.stream.Collectors

object ItemsKeptOnDeath {

    @JvmStatic
    fun open(player: Player) {
        player.send(Interface(17100))
        val unsorted = linkedSetOf<Item>()
        val keep = mutableListOf<Item>()
        val items = mutableListOf<Item>()
        val size = getAmountToKeep(player)

        items.addAll(player.equipment.notNullItems)
        items.addAll(player.inventory.notNullItems)

        //Sort items by value
        unsorted.addAll(items)

        val toDrop = unsorted
                .stream()
                .sorted { first, second -> second.definitions.value.compareTo(first.definitions.value) }
                .collect(Collectors.toList())

        //Add items kept because
        for (i in 0 until size) {
            if(toDrop.size > 0) {
                keep.add(toDrop.first())
                toDrop.remove(toDrop.first())
            }
        }

        //Remove any extra items that shouldn't be kept
        keep.forEach { item ->
            if (item.getDefinition().stackable && item.amount > 1)
                toDrop.add(item.createAndDecrement(1))
        }


        //Send side panel info

        val info = arrayListOf<String>()
        info.add("The normal amount of")
        info.add("items kept is three.")
        info.add("")

        when (size) {
            0 -> {
                info.add("You're marked with a " + FontUtils.RED + "skull" + FontUtils.COL_END)
                info.add("This reduces the items you")
                info.add("keep from three to zero!")
            }
            1, 4 -> {
                info.add("You have the " + FontUtils.RED + if (player.furySharkTimer > 0) "Fury Shark" else "Protect Item" + FontUtils.COL_END)
                val str = if (player.prayer.usingPrayer(0, Prayer.PROTECT_ITEM_PRAYER)) "prayer" else if (player.prayer.usingPrayer(1, Prayer.PROTECT_ITEM_CURSE)) "curse" else if (player.furySharkTimer > 0) "effect" else ""
                info.add(str + " active, which saves")
                info.add("you one extra item!")
            }
            3 -> {
                info.add("You have no factors")
                info.add("affecting the items you")
                info.add("keep.")
            }
            5 -> {
                info.add("You have the " + FontUtils.RED + "Fury Shark" + FontUtils.COL_END)
                val str = if (player.prayer.usingPrayer(0, Prayer.PROTECT_ITEM_PRAYER)) "prayer" else if (player.prayer.usingPrayer(1, Prayer.PROTECT_ITEM_CURSE)) "curse" else ""
                info.add("effect and the " + FontUtils.RED + "Protect Item" + FontUtils.COL_END)
                info.add(str + " active, which saves")
                info.add("you two extra items!")
            }
        }

        //Send interface data
        info.forEachIndexed { index, s -> player.send(InterfaceString(17155 + index, s)) }
        player.send(InterfaceString(17168, "~ $size ~"))

        var keepValue = 0L
        var lossValue = 0L

        keep.forEachIndexed { index, item ->
            keepValue += item.definitions.value * item.amount
            player.send(ItemOnWidget(17108 + index, Item(item, 1)))
        }

        toDrop.forEachIndexed { index, item ->
            lossValue += item.getDefinition().value * item.amount
            player.send(ItemOnWidget(17113 + index, item))
        }

        val totalValue = keepValue + lossValue
        player.send(InterfaceString(17164, Misc.insertCommas(totalValue)))
        player.send(InterfaceString(17166, Misc.insertCommas(lossValue), if (lossValue <= totalValue / 4) Colours.GREEN_3 else Colours.ORANGE))


        //Send extra empty data
        for (i in info.size..7)
            player.send(InterfaceString(17155 + i, ""))

        for (i in keep.size..4)
            player.send(ItemOnWidget(17108 + i, null))

        for (i in toDrop.size..39)
            player.send(ItemOnWidget(17113 + i, null))


        player.send(InterfaceString(17107, "Carried wealth:"))//Idk why these are needed by they are.
        player.send(InterfaceString(17165, "Risked wealth:"))//Idk why these are needed by they are.
    }

    fun getAmountToKeep(player: Player): Int {
        return (if (player.effects.hasActiveEffect(Effects.SKULL)) 0 else 3) + (if (player.prayer.isProtectingItem) 1 else 0) + if (player.furySharkTimer > 0) 1 else 0
    }
}