package com.fury.game.content.combat.magic.spell.modern.element.wave

import com.fury.core.model.item.Item
import com.fury.core.model.node.entity.actor.figure.Projectile
import com.fury.core.model.node.entity.actor.figure.SpeedProjectile
import com.fury.game.content.combat.magic.spell.modern.element.WindSpell
import com.fury.game.world.update.flag.block.graphic.Graphic
import com.fury.game.world.update.flag.block.graphic.GraphicHeight
import java.util.*

class WindWave : WindSpell() {
    override val maxHit = 170
    override val hitGraphic = Optional.of(Graphic(2699, height = GraphicHeight.HIGH))
    override val projectile = Optional.of(arrayOf(SpeedProjectile(461, 30, 26, 52, 0, 0) as Projectile))
    override val id = 61913
    override val level = 62
    override val experience = 36.0
    override val items = Optional.of(arrayOf(Item(556, 5), Item(565)))
}