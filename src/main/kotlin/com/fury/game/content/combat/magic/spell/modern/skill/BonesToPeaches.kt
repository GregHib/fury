package com.fury.game.content.combat.magic.spell.modern.skill

import com.fury.core.model.item.Item
import java.util.*

class BonesToPeaches : BonesToSpell(false) {
    override val id = 61854
    override val level = 60
    override val experience = 36.0
    override val items = Optional.of(arrayOf(Item(561, 2), Item(555, 4), Item(557, 4)))
}