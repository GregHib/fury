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

class Curse : DrainSpell(Skill.DEFENCE, 0.05) {
    override val animation = Optional.of(Animation(718))
    override val staffAnimation = Optional.of(Animation(1165))
    override val castGraphic = Optional.of(Graphic(108, height = GraphicHeight.HIGH))
    override val hitGraphic = Optional.of(Graphic(110, height = GraphicHeight.HIGH))
    override val projectile = Optional.of(arrayOf(SpeedProjectile(109, 44, 30, 43, 16, 0) as Projectile))
    override val id = 61550
    override val level = 19
    override val experience = 29.0
    override val items = Optional.of(arrayOf(Item(555, 2), Item(557, 3), Item(559, 1)))
}