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

class Stun : DrainSpell(Skill.ATTACK, 0.1) {
    override val maxHit = 50
    override val animation = Optional.of(Animation(729))
    override val staffAnimation = Optional.of(Animation(1166))
    override val castGraphic = Optional.of(Graphic(173, height = GraphicHeight.HIGH))
    override val hitGraphic = Optional.of(Graphic(348, height = GraphicHeight.HIGH))
    override val projectile = Optional.of(arrayOf(SpeedProjectile(174, 42, 34, 43, 6, 0) as Projectile))
    override val id = 62066
    override val level = 80
    override val experience = 90.0
    override val items = Optional.of(arrayOf(Item(557, 12), Item(555, 12), Item(556)))
}