package com.fury.game.content.misc.items.random

import com.fury.cache.Revision
import com.fury.core.model.item.Item
import com.fury.util.RandomUtils

class RandomItem(val id: Int, val minAmount: Int = 1, val maxAmount: Int = 1, private val revision: Revision = Revision.RS2) {

    constructor(id: Int, amount: Int = 1, revision: Revision = Revision.RS2) : this(id, amount, amount, revision)

    fun toItem(): Item {
        return Item(id, RandomUtils.inclusive(minAmount, maxAmount), revision)
    }
}