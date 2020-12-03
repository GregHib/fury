package com.fury.game.content.combat.magic.spell.modern.element.surge

import com.fury.core.model.item.Item
import com.fury.core.model.node.entity.actor.figure.Projectile
import com.fury.core.model.node.entity.actor.figure.SpeedProjectile
import com.fury.game.content.combat.magic.spell.modern.element.WindSpell
import com.fury.game.world.update.flag.block.graphic.Graphic
import com.fury.game.world.update.flag.block.graphic.GraphicHeight
import java.util.*

class WindSurge : WindSpell() {
    override val maxHit = 220
    override val hitGraphic = Optional.of(Graphic(2700, height = GraphicHeight.HIGH))
    override val projectile = Optional.of(arrayOf(SpeedProjectile(461, 30, 26, 52, 0, 0) as Projectile))
    override val id = 62092
    override val level = 81
    override val experience = 75.0
    override val items = Optional.of(arrayOf(Item(556, 7), Item(565), Item(560)))
}