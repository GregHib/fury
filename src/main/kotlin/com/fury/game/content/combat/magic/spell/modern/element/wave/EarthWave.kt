package com.fury.game.content.combat.magic.spell.modern.element.wave

import com.fury.core.model.item.Item
import com.fury.core.model.node.entity.actor.figure.Projectile
import com.fury.core.model.node.entity.actor.figure.SpeedProjectile
import com.fury.game.content.combat.magic.spell.modern.element.EarthSpell
import com.fury.game.world.update.flag.block.graphic.Graphic
import com.fury.game.world.update.flag.block.graphic.GraphicHeight
import java.util.*

class EarthWave : EarthSpell() {
    override val maxHit = 190
    override val castGraphic = Optional.of(Graphic(2716))
    override val hitGraphic = Optional.of(Graphic(2726, height = GraphicHeight.HIGH))
    override val projectile = Optional.of(arrayOf(SpeedProjectile(2721, 30, 26, 52, 0, 0) as Projectile))
    override val id = 61992
    override val level = 70
    override val experience = 40.0
    override val items = Optional.of(arrayOf(Item(556, 5), Item(565), Item(557, 7)))
}