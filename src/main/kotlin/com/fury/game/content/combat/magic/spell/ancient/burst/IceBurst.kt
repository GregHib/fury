package com.fury.game.content.combat.magic.spell.ancient.burst

import com.fury.core.model.item.Item
import com.fury.game.content.combat.magic.spell.ancient.IceSpell
import com.fury.game.world.update.flag.block.Animation
import com.fury.game.world.update.flag.block.graphic.Graphic
import java.util.*

class IceBurst : IceSpell(10000) {
    override val maxHit = 220
    override val animation = Optional.of(Animation(1979))
    override val hitGraphic = Optional.of(Graphic(363))
    override val id = 61141
    override val level = 70
    override val experience = 40.0
    override val items = Optional.of(arrayOf(Item(555, 4), Item(562, 4), Item(560, 2)))
    override val spellRadius = 1
}