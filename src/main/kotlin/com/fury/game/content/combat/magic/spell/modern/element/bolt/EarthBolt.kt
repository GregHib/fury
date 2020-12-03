package com.fury.game.content.combat.magic.spell.modern.element.bolt

import com.fury.core.model.item.Item
import com.fury.core.model.node.entity.actor.figure.Projectile
import com.fury.core.model.node.entity.actor.figure.SpeedProjectile
import com.fury.game.content.combat.magic.spell.modern.element.EarthSpell
import com.fury.game.world.update.flag.block.graphic.Graphic
import com.fury.game.world.update.flag.block.graphic.GraphicHeight
import java.util.*

class EarthBolt : EarthSpell() {
    //max 140 with Chaos gauntlets
    override val maxHit = 110
    override val castGraphic = Optional.of(Graphic(2714))
    override val hitGraphic = Optional.of(Graphic(2724, height = GraphicHeight.HIGH))
    override val projectile = Optional.of(arrayOf(SpeedProjectile(2719, 30, 26, 52, 0, 0) as Projectile))
    override val id = 61618
    override val level = 29
    override val experience = 19.5
    override val items = Optional.of(arrayOf(Item(556, 2), Item(562), Item(557, 3)))
}