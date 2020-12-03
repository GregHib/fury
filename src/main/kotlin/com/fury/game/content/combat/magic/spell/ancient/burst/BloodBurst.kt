package com.fury.game.content.combat.magic.spell.ancient.burst

import com.fury.core.model.item.Item
import com.fury.game.content.combat.magic.spell.ancient.BloodSpell
import com.fury.game.world.update.flag.block.Animation
import com.fury.game.world.update.flag.block.graphic.Graphic
import java.util.*

class BloodBurst : BloodSpell(50) {
    override val maxHit = 210
    override val animation = Optional.of(Animation(1979))
    override val hitGraphic = Optional.of(Graphic(376))
    override val id = 61128
    override val level = 68
    override val experience = 39.0
    override val items = Optional.of(arrayOf(Item(565, 2), Item(562, 4), Item(560, 2)))
    override val spellRadius = 1
}