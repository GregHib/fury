package com.fury.game.content.combat.magic.spell.dungeoneering

import com.fury.core.model.item.Item
import com.fury.core.model.node.entity.actor.figure.combat.magic.Spell
import java.util.*

class CreateGatestone : Spell() {
    override val id = 63071
    override val level = 32
    override val experience = 0.0
    override val items = Optional.of(arrayOf(Item(564)))
}