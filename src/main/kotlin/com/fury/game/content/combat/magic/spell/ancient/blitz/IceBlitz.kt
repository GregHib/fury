package com.fury.game.content.combat.magic.spell.ancient.blitz

import com.fury.core.model.item.Item
import com.fury.core.model.node.entity.actor.figure.Projectile
import com.fury.core.model.node.entity.actor.figure.SpeedProjectile
import com.fury.game.content.combat.magic.spell.ancient.IceSpell
import com.fury.game.world.update.flag.block.Animation
import com.fury.game.world.update.flag.block.graphic.Graphic
import com.fury.game.world.update.flag.block.graphic.GraphicHeight
import java.util.*

class IceBlitz : IceSpell(15000) {
    override val maxHit = 260
    override val animation = Optional.of(Animation(1978))
    override val castGraphic = Optional.of(Graphic(366, height = GraphicHeight.HIGH))
    override val hitGraphic = Optional.of(Graphic(367))
    override val projectile = Optional.of(arrayOf(SpeedProjectile(368, 18, 9, 52, 15, 0) as Projectile))
    override val id = 61222
    override val level = 82
    override val experience = 46.0
    override val items = Optional.of(arrayOf(Item(555, 3), Item(565, 2), Item(560, 2)))
}