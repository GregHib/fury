package com.fury.game.content.combat.magic.spell.modern.enchant

import com.fury.core.model.item.Item
import java.util.*

class EnchantDragonstone : EnchantSpell() {
    override val id = 61979
    override val level = 68
    override val experience = 78.0
    override val items = Optional.of(arrayOf(Item(564), Item(555, 15), Item(557, 15)))
}