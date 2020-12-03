package com.fury.game.node.entity.actor.figure.player.handles

import com.fury.core.model.node.entity.actor.figure.player.Player
import com.fury.game.entity.character.player.content.EnergyHandler
import com.fury.game.system.files.loaders.item.ItemWeights

class SettingsHandler(private val player: Player) : VariableMap<Settings>() {

    @Suppress("NAME_SHADOWING")
    override fun set(type: Settings, value: Any) {
        var value = value

        if((type == Settings.RUN_ENERGY || type == Settings.SPECIAL_ENERGY) && value is Int)
            if(value > type.value as Int)
                value = type.value
            else if(value < 0)
                value = 0

        super.set(type, value)
    }

    fun updateWeight() {
        weight = 0.0
        for (item in player.inventory.items) {
            if (item == null)
                continue
            val value = ItemWeights.getWeight(item, false)
            if (value > 0)
                weight += value
        }
        for (i in 0..10) {
            val item = player.equipment.get(i)
            if (item == null || item.id == -1)
                continue

            weight += ItemWeights.getWeight(item, true)
        }
    }

    fun restore(type: Settings, amount: Int) {
        if(type.value is Int)
            set(type, getInt(type) + amount)
    }

    var weight = 0.0
    var restingState: Int = 0
        set(value) {
            if (restingState != 0)
                EnergyHandler.standUp(player)
            field = value
        }
    var isSpecialToggled: Boolean = false
    val isResting: Boolean
        get() = restingState >= 1

    val isListening: Boolean
        get() = restingState == 2
}