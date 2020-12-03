package com.fury.game.content.combat.magic.spell.modern.element.blast

import com.fury.core.model.item.Item
import com.fury.core.model.node.entity.actor.figure.Projectile
import com.fury.core.model.node.entity.actor.figure.SpeedProjectile
import com.fury.game.content.combat.magic.spell.modern.element.WindSpell
import com.fury.game.world.update.flag.block.graphic.Graphic
import com.fury.game.world.update.flag.block.graphic.GraphicHeight
import java.util.*

class WindBlast : WindSpell() {
    override val maxHit = 130
    override val hitGraphic = Optional.of(Graphic(1863, height = GraphicHeight.HIGH))
    override val projectile = Optional.of(arrayOf(SpeedProjectile(460, 30, 26, 58, 0, 0) as Projectile))
    override val id = 61682
    override val level = 41
    override val experience = 25.5
    override val items = Optional.of(arrayOf(Item(556, 3), Item(560)))
}