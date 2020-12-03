package com.fury.game.content.combat.magic.spell.modern.element.blast

import com.fury.core.model.item.Item
import com.fury.core.model.node.entity.actor.figure.Projectile
import com.fury.core.model.node.entity.actor.figure.SpeedProjectile
import com.fury.game.content.combat.magic.spell.modern.element.EarthSpell
import com.fury.game.world.update.flag.block.graphic.Graphic
import com.fury.game.world.update.flag.block.graphic.GraphicHeight
import java.util.*

class EarthBlast : EarthSpell() {
    override val maxHit = 150
    override val castGraphic = Optional.of(Graphic(2715))
    override val hitGraphic = Optional.of(Graphic(2725, height = GraphicHeight.HIGH))
    override val projectile = Optional.of(arrayOf(SpeedProjectile(2720, 30, 26, 58, 0, 1) as Projectile))
    override val id = 61777
    override val level = 53
    override val experience = 31.5
    override val items = Optional.of(arrayOf(Item(556, 3), Item(560), Item(557, 4)))
}