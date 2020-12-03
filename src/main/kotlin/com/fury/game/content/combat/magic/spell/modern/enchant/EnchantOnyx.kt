package com.fury.game.content.combat.magic.spell.modern.enchant

import com.fury.core.model.item.Item
import java.util.*

class EnchantOnyx : EnchantSpell() {
    override val id = 62146
    override val level = 87
    override val experience = 97.0
    override val items = Optional.of(arrayOf(Item(564), Item(554, 20), Item(557, 20)))

}