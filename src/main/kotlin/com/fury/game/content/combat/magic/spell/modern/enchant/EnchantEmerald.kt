package com.fury.game.content.combat.magic.spell.modern.enchant

import com.fury.core.model.item.Item
import java.util.*

class EnchantEmerald : EnchantSpell() {
    override val id = 61607
    override val level = 27
    override val experience = 37.0
    override val items = Optional.of(arrayOf(Item(564), Item(556, 3)))
}