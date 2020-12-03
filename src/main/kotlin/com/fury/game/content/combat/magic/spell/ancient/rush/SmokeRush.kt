package com.fury.game.content.combat.magic.spell.ancient.rush

import com.fury.core.model.item.Item
import com.fury.core.model.node.entity.actor.figure.Projectile
import com.fury.core.model.node.entity.actor.figure.SpeedProjectile
import com.fury.game.content.combat.magic.spell.ancient.SmokeSpell
import com.fury.game.world.update.flag.block.Animation
import com.fury.game.world.update.flag.block.graphic.Graphic
import java.util.*

class SmokeRush : SmokeSpell(20) {
    override val maxHit = 130
    override val animation = Optional.of(Animation(1978))
    override val hitGraphic = Optional.of(Graphic(385))
    override val projectile = Optional.of(arrayOf(SpeedProjectile(384, 18, 9, 52, 15, 0) as Projectile))
    override val id = 61008
    override val level = 50
    override val experience = 30.0
    override val items = Optional.of(arrayOf(Item(556, 1), Item(554, 1), Item(562, 2), Item(560, 2)))
}