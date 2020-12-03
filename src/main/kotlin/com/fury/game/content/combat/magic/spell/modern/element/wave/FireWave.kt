package com.fury.game.content.combat.magic.spell.modern.element.wave

import com.fury.core.model.item.Item
import com.fury.core.model.node.entity.actor.figure.Projectile
import com.fury.core.model.node.entity.actor.figure.SpeedProjectile
import com.fury.game.content.combat.magic.spell.modern.element.FireSpell
import com.fury.game.world.update.flag.block.graphic.Graphic
import com.fury.game.world.update.flag.block.graphic.GraphicHeight
import java.util.*

class FireWave : FireSpell() {
    override val maxHit = 200
    override val hitGraphic = Optional.of(Graphic(2740, height = GraphicHeight.HIGH))
    //uses 3
    //2734 (one high curve and fast but delayed, one low curve and slow no delay)
    //2735 middle straight
    override val projectile = Optional.of(arrayOf(
            SpeedProjectile(2734, 30, 26, 54, 16, 0) as Projectile,
            SpeedProjectile(2734, 30, 26, 52, -10, 0) as Projectile,
            SpeedProjectile(2735, 30, 26, 52, 0, 0) as Projectile
    ))
    override val id = 62031
    override val level = 75
    override val experience = 42.5
    override val items = Optional.of(arrayOf(Item(556, 5), Item(565), Item(554, 7)))
}