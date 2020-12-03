package com.fury.game.content.combat.magic.spell.dungeoneering

import com.fury.core.model.item.Item
import com.fury.core.model.node.entity.actor.figure.Figure
import com.fury.core.model.node.entity.actor.figure.combat.magic.Spell
import java.util.*

class GroupGatestoneTeleport : Spell() {
    override val id = 63185
    override val level = 64
    override val experience = 0.0
    override val items = Optional.of(arrayOf(Item(563, 3)))

}