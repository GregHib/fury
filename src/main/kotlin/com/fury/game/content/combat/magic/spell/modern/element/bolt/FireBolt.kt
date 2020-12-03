package com.fury.game.content.combat.magic.spell.modern.element.bolt

import com.fury.core.model.item.Item
import com.fury.core.model.node.entity.actor.figure.Projectile
import com.fury.core.model.node.entity.actor.figure.SpeedProjectile
import com.fury.game.content.combat.magic.spell.modern.element.FireSpell
import com.fury.game.world.update.flag.block.graphic.Graphic
import com.fury.game.world.update.flag.block.graphic.GraphicHeight
import java.util.*

class FireBolt : FireSpell() {
    //max 150 with chaos gauntlets
    override val maxHit = 120
    override val hitGraphic = Optional.of(Graphic(2738, height = GraphicHeight.HIGH))
    override val projectile = Optional.of(arrayOf(SpeedProjectile(2730, 30, 26, 52, 0, 0) as Projectile))
    override val id = 61649
    override val level = 35
    override val experience = 22.5
    override val items = Optional.of(arrayOf(Item(556, 3), Item(562), Item(554, 4)))
}