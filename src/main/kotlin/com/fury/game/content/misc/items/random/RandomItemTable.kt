package com.fury.game.content.misc.items.random

import com.fury.util.RandomUtils
import java.math.BigDecimal

class RandomItemTable {
    var chance: BigDecimal
    private var items: IntArray? = null
    var name: String
    private var randItems: Array<out RandomItem>? = null

    constructor(name:String, chance: Double, vararg items: Int) {
        this.name = name
        this.chance = BigDecimal.valueOf(chance)
        this.items = items
    }

    constructor(name:String, chance: Double, vararg items: RandomItem) {
        this.name = name
        this.chance = BigDecimal.valueOf(chance)
        this.randItems = items
    }

    /**
     * returns a RandomItem from the values provided
     */
    fun getRandomItem(): RandomItem? {
        val items = this.items
        if(items != null) {
            val item = RandomUtils.random(*items)
            return RandomItem(item)
        }

        val randomItems = this.randItems
        if(randomItems != null) {
            return RandomUtils.random(randomItems)
        }

        return null
    }

    fun isIntArray(): Boolean {
        return items != null
    }

    fun getItems(): IntArray {
        return items!!
    }

    fun getRandomItems(): Array<out RandomItem> {
        return randItems!!
    }
}