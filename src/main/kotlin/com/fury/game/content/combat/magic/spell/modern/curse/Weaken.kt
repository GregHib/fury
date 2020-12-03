package com.fury.game.content.combat.magic.spell.modern.curse

import com.fury.core.model.item.Item
import com.fury.core.model.node.entity.actor.figure.Projectile
import com.fury.core.model.node.entity.actor.figure.SpeedProjectile
import com.fury.game.content.combat.magic.spell.modern.curse.types.DrainSpell
import com.fury.game.content.skill.Skill
import com.fury.game.world.update.flag.block.Animation
import com.fury.game.world.update.flag.block.graphic.Graphic
import com.fury.game.world.update.flag.block.graphic.GraphicHeight
import java.util.*

class Weaken : DrainSpell(Skill.STRENGTH, 0.05) {
    override val animation = Optional.of(Animation(717))
    override val staffAnimation = Optional.of(Animation(1164))
    override val castGraphic = Optional.of(Graphic(105, height = GraphicHeight.HIGH))
    override val hitGraphic = Optional.of(Graphic(107, height = GraphicHeight.HIGH))
    override val projectile = Optional.of(arrayOf(SpeedProjectile(106, 43, 28, 46, 11, 0) as Projectile))
    override val id = 61493
    override val level = 11
    override val experience = 21.0
    override val items = Optional.of(arrayOf(Item(555, 3), Item(557, 2), Item(559)))
}