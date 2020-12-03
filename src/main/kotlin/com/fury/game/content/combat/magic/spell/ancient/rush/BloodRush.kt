package com.fury.game.content.combat.magic.spell.ancient.rush

import com.fury.core.model.item.Item
import com.fury.core.model.node.entity.actor.figure.Projectile
import com.fury.core.model.node.entity.actor.figure.SpeedProjectile
import com.fury.game.content.combat.magic.spell.ancient.BloodSpell
import com.fury.game.world.update.flag.block.Animation
import com.fury.game.world.update.flag.block.graphic.Graphic
import java.util.*

class BloodRush : BloodSpell(40) {
    override val maxHit = 150
    override val animation = Optional.of(Animation(1978))
    override val hitGraphic = Optional.of(Graphic(373))
    override val projectile = Optional.of(arrayOf(SpeedProjectile(372, 18, 9, 52, 15, 0) as Projectile))
    override val id = 61045
    override val level = 56
    override val experience = 33.0
    override val items = Optional.of(arrayOf(Item(565, 1), Item(562, 2), Item(560, 2)))
}