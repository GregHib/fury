package com.fury.game.content.combat.magic.spell.ancient.blitz

import com.fury.core.model.item.Item
import com.fury.core.model.node.entity.actor.figure.Projectile
import com.fury.core.model.node.entity.actor.figure.SpeedProjectile
import com.fury.game.content.combat.magic.spell.ancient.ShadowSpell
import com.fury.game.world.update.flag.block.Animation
import com.fury.game.world.update.flag.block.graphic.Graphic
import java.util.*

class ShadowBlitz : ShadowSpell(0.1) {
    override val maxHit = 240
    override val animation = Optional.of(Animation(1978))
    override val hitGraphic = Optional.of(Graphic(381))
    override val projectile = Optional.of(arrayOf(SpeedProjectile(380, 18, 9, 52, 15, 0) as Projectile))
    override val id = 61189
    override val level = 76
    override val experience = 43.0
    override val items = Optional.of(arrayOf(Item(556, 2), Item(566, 2), Item(565, 2), Item(560, 2)))
}