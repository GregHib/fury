package com.fury.game.content.combat.magic.spell.modern.element.wave

import com.fury.core.model.item.Item
import com.fury.core.model.node.entity.actor.figure.Projectile
import com.fury.core.model.node.entity.actor.figure.SpeedProjectile
import com.fury.game.content.combat.magic.spell.modern.element.WaterSpell
import com.fury.game.world.update.flag.block.graphic.Graphic
import com.fury.game.world.update.flag.block.graphic.GraphicHeight
import java.util.*

class WaterWave : WaterSpell() {
    override val maxHit = 180
    override val hitGraphic = Optional.of(Graphic(2711, height = GraphicHeight.HIGH))
    override val projectile = Optional.of(arrayOf(SpeedProjectile(2706, 30, 26, 52, 0, 0) as Projectile))
    override val id = 61942
    override val level = 65
    override val experience = 37.5
    override val items = Optional.of(arrayOf(Item(556, 5), Item(565), Item(555, 7)))
}