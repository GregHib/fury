package com.fury.game.content.combat.magic.spell.ancient.blitz

import com.fury.core.model.item.Item
import com.fury.core.model.node.entity.actor.figure.Projectile
import com.fury.core.model.node.entity.actor.figure.SpeedProjectile
import com.fury.game.content.combat.magic.spell.ancient.BloodSpell
import com.fury.game.world.update.flag.block.Animation
import com.fury.game.world.update.flag.block.graphic.Graphic
import java.util.*

class BloodBlitz : BloodSpell(60) {
    override val maxHit = 250
    override val animation = Optional.of(Animation(1978))
    override val hitGraphic = Optional.of(Graphic(375))
    override val projectile = Optional.of(arrayOf(SpeedProjectile(374, 18, 9, 52, 15, 0) as Projectile))
    override val id = 61211
    override val level = 80
    override val experience = 45.0
    override val items = Optional.of(arrayOf(Item(565, 4), Item(560, 2)))
}