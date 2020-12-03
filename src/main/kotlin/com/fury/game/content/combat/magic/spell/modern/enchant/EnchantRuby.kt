package com.fury.game.content.combat.magic.spell.modern.enchant

import com.fury.core.model.item.Item
import java.util.*

class EnchantRuby : EnchantSpell() {
    override val id = 61724
    override val level = 49
    override val experience = 59.0
    override val items = Optional.of(arrayOf(Item(564), Item(554, 5)))
}