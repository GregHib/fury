package com.fury.game.content.combat.magic.spell.ancient.burst

import com.fury.core.model.item.Item
import com.fury.game.content.combat.magic.spell.ancient.SmokeSpell
import com.fury.game.world.update.flag.block.Animation
import com.fury.game.world.update.flag.block.graphic.Graphic
import java.util.*

class SmokeBurst : SmokeSpell(20) {
    override val maxHit = 170
    override val animation = Optional.of(Animation(1979))
    override val hitGraphic = Optional.of(Graphic(389))
    override val id = 61091
    override val level = 62
    override val experience = 36.0
    override val items = Optional.of(arrayOf(Item(556, 2), Item(554, 2), Item(562, 4), Item(560, 2)))
    override val spellRadius = 1
}