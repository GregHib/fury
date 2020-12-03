package com.fury.game.content.combat.magic.spell.ancient.rush

import com.fury.core.model.item.Item
import com.fury.core.model.node.entity.actor.figure.Projectile
import com.fury.core.model.node.entity.actor.figure.SpeedProjectile
import com.fury.game.content.combat.magic.spell.ancient.IceSpell
import com.fury.game.world.update.flag.block.Animation
import com.fury.game.world.update.flag.block.graphic.Graphic
import java.util.*

class IceRush : IceSpell(5000) {
    override val maxHit = 160
    override val animation = Optional.of(Animation(1978))
    override val hitGraphic = Optional.of(Graphic(361))
    override val projectile = Optional.of(arrayOf(SpeedProjectile(360, 18, 9, 52, 15, 0) as Projectile))
    override val id = 61058
    override val level = 58
    override val experience = 34.0
    override val items = Optional.of(arrayOf(Item(555, 2), Item(562, 2), Item(560, 2)))
}