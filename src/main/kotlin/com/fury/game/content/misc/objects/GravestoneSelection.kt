package com.fury.game.content.misc.objects

import com.fury.core.model.node.entity.actor.figure.player.Player
import com.fury.game.content.dialogue.impl.misc.GravestoneD
import com.fury.game.network.packet.out.Interface
import com.fury.game.network.packet.out.InterfaceScrollPosition
import com.fury.game.network.packet.out.InterfaceString
import com.fury.util.Misc

object GravestoneSelection {

    private const val INTERFACE_ID = 35000

    enum class GraveType(val price: Int, val title: Array<String>, val time: Int, val description: Array<String>) {
        MEMORIAL(0, "Memorial plaque", 105, arrayOf("A simple plaque", "bearing the name", "of the deceased.", "", "(Everyone has this by default.)")),
        FLAG(50000, "Flag", 120, arrayOf("A dignified little flag flutters", "in the breeze.")),
        SMALL(150000, "Small gravestone", 120, arrayOf("A simple little gravestone.")),
        ORNATE(500000, "Ornate gravestone", 135, arrayOf("An ornately carved gravestone.")),
        FONT(1000000, "Font of Life", 150, arrayOf("An elaborate font.", "As water collects in the bowl,", "it symbolises new life and hope.")),
        STELE(5000000, "Stele", 150, arrayOf("A monolithic memorial stone", "bearing a selection of emblems.")),
        SARADOMIN(10000000, "Symbol of Saradomin", 150, arrayOf("The four-pointed star is", "universally recognised as", "the symbol of Saradomin.")),
        ZAMORAK(10000000, "Symbol of Zamorak", 150, arrayOf("This serpentine sigil", "represents the ruthless", "Zamorak.")),
        GUTHIX(10000000, "Symbol of Guthix", 150, arrayOf("Favoured by humans of a", "mystical or druidic inclination.")),
        BANDOS(10000000, "Symbol of Bandos", 150, arrayOf("Followers of Bandos are now", "scarce but this symbol is", "still used by those who", "remember that violent god.")),
        ARMADYL(10000000, "Symbol of Armadyl", 150, arrayOf("Armadyl's love of winged", "creatures is represented by", "this elegant symbol.")),
        ANCIENT(15000000, "Ancient symbol", 150, arrayOf("Little else is remembered of him.")),
        ANGEL(50000000, "Angel of Death", 165, arrayOf("Adventurers often request this", "symbol, although its origin and", "meaning have become obscure in", "the centuries since the god wars.")),
        DWARVEN(100000000, arrayOf("Royal dwarven", "gravestone"), 180, arrayOf("The ultimate marker for a", "grave, this statue evokes", "intimations of mortality in all", "who are faced with it."));

        constructor(price: Int, title: String, time: Int, description: Array<String>) : this(price, arrayOf(title), time, description)
    }

    private val graves = GraveType.values()

    @JvmStatic
    fun getGrave(gravestone: Int): GraveType {
        return graves[gravestone]
    }


    fun getSeconds(graveStone: Int): Int {
        return getGrave(graveStone).time
    }

    @JvmStatic
    fun open(player: Player) {
        player.send(Interface(INTERFACE_ID))
        sendDetails(player, player.gravestone)
    }

    @JvmStatic
    fun buttonPressed(player: Player, buttonId: Int): Boolean {
        if (buttonId >= INTERFACE_ID + 26 && buttonId <= INTERFACE_ID + 46) {
            sendDetails(player, (buttonId - (INTERFACE_ID + 26)) / 4)
            return true
        } else if (buttonId >= INTERFACE_ID + 50 && buttonId <= INTERFACE_ID + 75) {
            sendDetails(player, 6 + (buttonId - (INTERFACE_ID + 50)) / 5)
            return true
        } else if (buttonId >= INTERFACE_ID + 80 && buttonId <= INTERFACE_ID + 84) {
            sendDetails(player, 12 + (buttonId - (INTERFACE_ID + 80)) / 4)
            return true
        } else if (buttonId == INTERFACE_ID + 15) {
            when {
                player.temporaryAttributes["gravestone"] == null -> player.message("Please select a gravestone to purchase.")
                player.temporaryAttributes["gravestone"] as Int == player.gravestone -> player.message("You already have that gravestone purchased.")
                else -> player.dialogueManager.startDialogue(GravestoneD(), player.temporaryAttributes.remove("gravestone"))
            }

            return true
        }

        return false
    }

    private fun sendDetails(player: Player, index: Int) {
        player.temporaryAttributes["gravestone"] = index
        for (i in 0..1)
            player.send(InterfaceString(INTERFACE_ID + 6 + i, if (i >= graves[index].title.size) "" else graves[index].title[i]))
        for (i in 0..5)
            player.send(InterfaceString(INTERFACE_ID + 8 + i, if (i >= graves[index].description.size) "" else graves[index].description[i]))
        val seconds = getSeconds(index)
        player.send(InterfaceScrollPosition(INTERFACE_ID + 23, index * 159))
        player.send(InterfaceString(INTERFACE_ID + 14, "Initial duration: " + seconds / 60 + ":" + seconds % 60))
        if (index != player.gravestone) {
            player.send(InterfaceString(INTERFACE_ID + 18, "Confirm:"))
            player.send(InterfaceString(INTERFACE_ID + 19, Misc.formatAmount(graves[index].price.toLong())))
            player.send(InterfaceString(INTERFACE_ID + 20, "coins"))
        } else {
            player.send(InterfaceString(INTERFACE_ID + 18, "Purchased"))
            player.send(InterfaceString(INTERFACE_ID + 19, ""))
            player.send(InterfaceString(INTERFACE_ID + 20, ""))
        }
    }
}