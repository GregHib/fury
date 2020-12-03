package com.fury.game.content.combat.magic.spell.ancient.barrage

import com.fury.core.model.item.Item
import com.fury.game.content.combat.magic.spell.ancient.SmokeSpell
import com.fury.game.world.update.flag.block.Animation
import com.fury.game.world.update.flag.block.graphic.Graphic
import java.util.*

class SmokeBarrage : SmokeSpell(40) {
    override val maxHit = 270
    override val animation = Optional.of(Animation(1979))
    override val hitGraphic = Optional.of(Graphic(391))
    override val id = 61255
    override val level = 86
    override val experience = 48.0
    override val items = Optional.of(arrayOf(Item(556, 4), Item(554, 4), Item(565, 2), Item(560, 4)))
    override val spellRadius = 1
}