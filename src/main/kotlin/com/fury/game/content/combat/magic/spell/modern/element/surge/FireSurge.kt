package com.fury.game.content.combat.magic.spell.modern.element.surge

import com.fury.core.model.item.Item
import com.fury.core.model.node.entity.actor.figure.Projectile
import com.fury.core.model.node.entity.actor.figure.SpeedProjectile
import com.fury.game.content.combat.magic.spell.modern.element.FireSpell
import com.fury.game.world.update.flag.block.graphic.Graphic
import com.fury.game.world.update.flag.block.graphic.GraphicHeight
import java.util.*

class FireSurge : FireSpell() {
    override val maxHit = 280
    override val hitGraphic = Optional.of(Graphic(2741, height = GraphicHeight.HIGH))
    override val projectile = Optional.of(arrayOf(
            SpeedProjectile(2736, 30, 26, 54, 16, 0) as Projectile,
            SpeedProjectile(2736, 30, 26, 52, -10, 0) as Projectile,
            SpeedProjectile(2735, 30, 26, 52, 0, 0) as Projectile
    ))
    override val id = 62185
    override val level = 95
    override val experience = 90.0
    override val items = Optional.of(arrayOf(Item(554, 10), Item(556, 7), Item(565), Item(560)))
}