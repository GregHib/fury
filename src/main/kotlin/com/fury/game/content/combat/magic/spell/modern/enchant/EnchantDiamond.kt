package com.fury.game.content.combat.magic.spell.modern.enchant

import com.fury.core.model.item.Item
import java.util.*

class EnchantDiamond : EnchantSpell() {
    override val id = 61812
    override val level = 57
    override val experience = 67.0
    override val items = Optional.of(arrayOf(Item(564), Item(557, 10)))
}