package com.fury.game.content.combat.magic.spell.modern.enchant

import com.fury.core.model.item.Item
import java.util.*

class EnchantSapphire : EnchantSpell() {
    override val id = 61462
    override val level = 7
    override val experience = 17.5
    override val items = Optional.of(arrayOf(Item(564), Item(555)))
}