package com.fury.game.content.combat.magic.spell.modern.element.blast

import com.fury.core.model.item.Item
import com.fury.core.model.node.entity.actor.figure.Projectile
import com.fury.core.model.node.entity.actor.figure.SpeedProjectile
import com.fury.game.content.combat.magic.spell.modern.element.FireSpell
import com.fury.game.world.update.flag.block.graphic.Graphic
import com.fury.game.world.update.flag.block.graphic.GraphicHeight
import java.util.*

class FireBlast : FireSpell() {
    override val maxHit = 160
    override val hitGraphic = Optional.of(Graphic(2739, height = GraphicHeight.HIGH))
    override val projectile = Optional.of(arrayOf(
            SpeedProjectile(2732, 30, 26, 52, 16, 0) as Projectile,
            SpeedProjectile(2733, 30, 26, 52, 0, 0) as Projectile
    ))
    override val id = 61830
    override val level = 59
    override val experience = 34.5
    override val items = Optional.of(arrayOf(Item(556, 4), Item(560), Item(554, 5)))
}