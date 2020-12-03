package com.fury.game.content.combat.magic.spell.ancient.barrage

import com.fury.core.model.item.Item
import com.fury.game.content.combat.magic.spell.ancient.ShadowSpell
import com.fury.game.world.update.flag.block.Animation
import com.fury.game.world.update.flag.block.graphic.Graphic
import java.util.*

class ShadowBarrage : ShadowSpell(0.15) {
    override val maxHit = 280
    override val animation = Optional.of(Animation(1979))
    override val hitGraphic = Optional.of(Graphic(383))
    override val id = 61270
    override val level = 88
    override val experience = 48.0
    override val items = Optional.of(arrayOf(Item(556, 4), Item(566, 3), Item(565, 2), Item(560, 4)))
    override val spellRadius = 1
}