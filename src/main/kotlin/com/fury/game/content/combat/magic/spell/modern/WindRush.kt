package com.fury.game.content.combat.magic.spell.modern

import com.fury.core.model.item.Item
import com.fury.core.model.node.entity.actor.figure.Projectile
import com.fury.core.model.node.entity.actor.figure.SpeedProjectile
import com.fury.game.content.combat.magic.spell.modern.element.WindSpell
import com.fury.game.world.update.flag.block.graphic.Graphic
import com.fury.game.world.update.flag.block.graphic.GraphicHeight
import java.util.*

class WindRush : WindSpell() {
    override val maxHit = 10
    override val hitGraphic = Optional.of(Graphic(463, height = GraphicHeight.MIDDLE))
    override val projectile = Optional.of(arrayOf(SpeedProjectile(458, 30, 26, 52, 0, 0) as Projectile))
    override val id = 61409
    override val experience = 2.7
    override val items = Optional.of(arrayOf(Item(556, 2)))
}