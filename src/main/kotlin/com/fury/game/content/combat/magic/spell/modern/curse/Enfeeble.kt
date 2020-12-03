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

class Enfeeble : DrainSpell(Skill.STRENGTH, 0.1) {
    override val maxHit = 50
    override val animation = Optional.of(Animation(728))
    override val staffAnimation = Optional.of(Animation(1168))
    override val castGraphic = Optional.of(Graphic(170, height = GraphicHeight.HIGH))
    override val hitGraphic = Optional.of(Graphic(172, height = GraphicHeight.HIGH))
    override val projectile = Optional.of(arrayOf(SpeedProjectile(171, 40, 36, 43, 16, 0) as Projectile))
    override val id = 62005
    override val level = 73
    override val experience = 83.0
    override val items = Optional.of(arrayOf(Item(557, 8), Item(555, 8), Item(566)))
}