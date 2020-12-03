package com.fury.game.content.combat.magic.spell.ancient.burst

import com.fury.core.model.item.Item
import com.fury.game.content.combat.magic.spell.ancient.ShadowSpell
import com.fury.game.world.update.flag.block.Animation
import com.fury.game.world.update.flag.block.graphic.Graphic
import java.util.*

class ShadowBurst : ShadowSpell(0.1) {
    override val maxHit = 180
    override val animation = Optional.of(Animation(1979))
    override val hitGraphic = Optional.of(Graphic(382))
    override val id = 61106
    override val level = 64
    override val experience = 37.0
    override val items = Optional.of(arrayOf(Item(556, 1), Item(566, 2), Item(562, 4), Item(560, 2)))
    override val spellRadius = 1
}