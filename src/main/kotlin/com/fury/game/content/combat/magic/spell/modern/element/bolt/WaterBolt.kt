package com.fury.game.content.combat.magic.spell.modern.element.bolt

import com.fury.core.model.item.Item
import com.fury.core.model.node.entity.actor.figure.Projectile
import com.fury.core.model.node.entity.actor.figure.SpeedProjectile
import com.fury.game.content.combat.magic.spell.modern.element.WaterSpell
import com.fury.game.world.update.flag.block.graphic.Graphic
import com.fury.game.world.update.flag.block.graphic.GraphicHeight
import java.util.*

class WaterBolt : WaterSpell() {
    //Chaos gauntlets max hit 130
    override val maxHit = 100
    override val hitGraphic = Optional.of(Graphic(2709, height = GraphicHeight.HIGH))
    override val projectile = Optional.of(arrayOf(SpeedProjectile(2704, 30, 26, 52, 0, 0) as Projectile))
    override val id = 61587
    override val level = 23
    override val experience = 16.5
    override val items = Optional.of(arrayOf(Item(556, 2), Item(562), Item(555, 2)))
}