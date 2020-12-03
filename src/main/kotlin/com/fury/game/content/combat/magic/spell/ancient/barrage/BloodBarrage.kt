package com.fury.game.content.combat.magic.spell.ancient.barrage

import com.fury.core.model.item.Item
import com.fury.game.content.combat.magic.spell.ancient.BloodSpell
import com.fury.game.world.update.flag.block.Animation
import com.fury.game.world.update.flag.block.graphic.Graphic
import java.util.*

class BloodBarrage : BloodSpell(119) {
    override val maxHit = 290
    override val animation = Optional.of(Animation(1979))
    override val hitGraphic = Optional.of(Graphic(377))
    override val id = 61292
    override val level = 92
    override val experience = 51.0
    override val items = Optional.of(arrayOf(Item(560, 4), Item(566, 1), Item(565, 4)))
    override val spellRadius = 1
}