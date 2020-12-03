package com.fury.game.content.combat.magic.spell.modern.element.blast

import com.fury.core.model.item.Item
import com.fury.core.model.node.entity.actor.figure.Projectile
import com.fury.core.model.node.entity.actor.figure.SpeedProjectile
import com.fury.game.content.combat.magic.spell.modern.element.WaterSpell
import com.fury.game.world.update.flag.block.graphic.Graphic
import com.fury.game.world.update.flag.block.graphic.GraphicHeight
import java.util.*

class WaterBlast : WaterSpell() {
    override val maxHit = 140
    override val hitGraphic = Optional.of(Graphic(2710, height = GraphicHeight.HIGH))
    override val projectile = Optional.of(arrayOf(SpeedProjectile(2705, 30, 26, 52, 0, 0) as Projectile))
    override val id = 61711
    override val level = 47
    override val experience = 28.5
    override val items = Optional.of(arrayOf(Item(555, 3), Item(556, 3), Item(560)))
}