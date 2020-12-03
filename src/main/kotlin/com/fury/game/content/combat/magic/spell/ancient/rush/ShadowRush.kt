package com.fury.game.content.combat.magic.spell.ancient.rush

import com.fury.core.model.item.Item
import com.fury.core.model.node.entity.actor.figure.Projectile
import com.fury.core.model.node.entity.actor.figure.SpeedProjectile
import com.fury.game.content.combat.magic.spell.ancient.ShadowSpell
import com.fury.game.world.update.flag.block.Animation
import com.fury.game.world.update.flag.block.graphic.Graphic
import java.util.*

class ShadowRush : ShadowSpell(0.1) {
    override val maxHit = 140
    override val animation = Optional.of(Animation(1978))
    override val hitGraphic = Optional.of(Graphic(379))
    override val projectile = Optional.of(arrayOf(SpeedProjectile(378, 18, 9, 52, 15, 0) as Projectile))
    override val id = 61023
    override val level = 52
    override val experience = 31.0
    override val items = Optional.of(arrayOf(Item(556, 1), Item(566, 1), Item(562, 2), Item(560, 2)))
}