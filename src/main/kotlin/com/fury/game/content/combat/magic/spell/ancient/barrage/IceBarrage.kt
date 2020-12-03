package com.fury.game.content.combat.magic.spell.ancient.barrage

import com.fury.core.model.item.Item
import com.fury.game.content.combat.magic.spell.ancient.IceSpell
import com.fury.game.world.update.flag.block.Animation
import com.fury.game.world.update.flag.block.graphic.Graphic
import java.util.*

class IceBarrage : IceSpell(20000) {
    override val maxHit = 300
    override val animation = Optional.of(Animation(1979))
    override val hitGraphic = Optional.of(Graphic(369))
    override val id = 61305
    override val level = 94
    override val experience = 52.0
    override val items = Optional.of(arrayOf(Item(555, 6), Item(565, 2), Item(560, 4)))
    override val spellRadius = 1
}