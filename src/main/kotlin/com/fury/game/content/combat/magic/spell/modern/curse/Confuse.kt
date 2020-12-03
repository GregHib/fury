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

class Confuse : DrainSpell(Skill.ATTACK, 0.05) {
    override val animation = Optional.of(Animation(716))
    override val staffAnimation = Optional.of(Animation(1163))
    override val castGraphic = Optional.of(Graphic(102, height = GraphicHeight.HIGH))
    override val hitGraphic = Optional.of(Graphic(104, height = GraphicHeight.HIGH))
    override val projectile = Optional.of(arrayOf(SpeedProjectile(103, 43, 40, 46, 6, 0) as Projectile))
    override val id = 61429
    override val level = 3
    override val experience = 13.0
    override val items = Optional.of(arrayOf(Item(555, 3), Item(557, 2), Item(559)))
}