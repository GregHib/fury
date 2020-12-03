package com.fury.game.content.combat.magic.spell.modern.curse

import com.fury.core.model.item.Item
import com.fury.core.model.node.entity.actor.figure.Projectile
import com.fury.core.model.node.entity.actor.figure.SpeedProjectile
import com.fury.game.content.combat.magic.spell.modern.curse.types.FreezeSpell
import com.fury.game.world.update.flag.block.Animation
import com.fury.game.world.update.flag.block.graphic.Graphic
import com.fury.game.world.update.flag.block.graphic.GraphicHeight
import java.util.*

class Entangle : FreezeSpell(15000) {
    override val maxHit = 50
    override val animation = Optional.of(Animation(724))
    override val staffAnimation = Optional.of(Animation(1161))
    override val castGraphic = Optional.of(Graphic(177, height = GraphicHeight.HIGH))
    override val hitGraphic = Optional.of(Graphic(179, height = GraphicHeight.HIGH))
    override val projectile = Optional.of(arrayOf(SpeedProjectile(178, 44, 0, 43, 16, 1) as Projectile))
    override val id = 62053
    override val level = 79
    override val experience = 91.0
    override val items = Optional.of(arrayOf(Item(555, 5), Item(557, 5), Item(561, 4)))
}