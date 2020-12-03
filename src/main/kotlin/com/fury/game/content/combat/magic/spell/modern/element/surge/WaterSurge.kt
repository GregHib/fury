package com.fury.game.content.combat.magic.spell.modern.element.surge

import com.fury.core.model.item.Item
import com.fury.core.model.node.entity.actor.figure.Projectile
import com.fury.core.model.node.entity.actor.figure.SpeedProjectile
import com.fury.game.content.combat.magic.spell.modern.element.WaterSpell
import com.fury.game.world.update.flag.block.graphic.Graphic
import com.fury.game.world.update.flag.block.graphic.GraphicHeight
import java.util.*

class WaterSurge : WaterSpell() {
    override val maxHit = 240
    override val hitGraphic = Optional.of(Graphic(2711, height = GraphicHeight.HIGH))
    override val projectile = Optional.of(arrayOf(SpeedProjectile(2707, 30, 26, 52, 0, 0) as Projectile))
    override val id = 62131
    override val level = 85
    override val experience = 80.0
    override val items = Optional.of(arrayOf(Item(555, 10), Item(556, 7), Item(565), Item(560)))
}