package com.fury.game.content.combat.magic.spell.modern.element.bolt

import com.fury.core.model.item.Item
import com.fury.core.model.node.entity.actor.figure.Projectile
import com.fury.core.model.node.entity.actor.figure.SpeedProjectile
import com.fury.game.content.combat.magic.spell.modern.element.WindSpell
import com.fury.game.world.update.flag.block.graphic.Graphic
import com.fury.game.world.update.flag.block.graphic.GraphicHeight
import java.util.*

class WindBolt : WindSpell() {
    //chaos gauntlets increase max to 120
    override val maxHit = 90
    override val hitGraphic = Optional.of(Graphic(464, height = GraphicHeight.HIGH))
    override val projectile = Optional.of(arrayOf(SpeedProjectile(459, 30, 26, 52, 0, 0) as Projectile))
    override val id = 61539
    override val level = 17
    override val experience = 13.5
    override val items = Optional.of(arrayOf(Item(556, 2), Item(562)))
}