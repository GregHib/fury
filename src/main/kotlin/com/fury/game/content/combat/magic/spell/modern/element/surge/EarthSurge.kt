package com.fury.game.content.combat.magic.spell.modern.element.surge

import com.fury.core.model.item.Item
import com.fury.core.model.node.entity.actor.figure.Projectile
import com.fury.core.model.node.entity.actor.figure.SpeedProjectile
import com.fury.game.content.combat.magic.spell.modern.element.EarthSpell
import com.fury.game.world.update.flag.block.graphic.Graphic
import com.fury.game.world.update.flag.block.graphic.GraphicHeight
import java.util.*

class EarthSurge : EarthSpell() {
    override val maxHit = 260
    override val castGraphic = Optional.of(Graphic(2717))
    override val hitGraphic = Optional.of(Graphic(2727, height = GraphicHeight.HIGH))
    override val projectile = Optional.of(arrayOf(SpeedProjectile(2722, 30, 26, 52, 0, 0) as Projectile))
    override val id = 62159
    override val level = 90
    override val experience = 85.0
    override val items = Optional.of(arrayOf(Item(557, 10), Item(556, 7), Item(565), Item(560)))
}