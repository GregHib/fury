package com.fury.game.content.combat.magic.spell.modern

import com.fury.core.model.item.Item
import com.fury.core.model.node.entity.actor.figure.Figure
import com.fury.core.model.node.entity.actor.figure.Projectile
import com.fury.core.model.node.entity.actor.figure.SpeedProjectile
import com.fury.core.model.node.entity.actor.figure.combat.magic.SpellContext
import com.fury.core.model.node.entity.actor.figure.combat.magic.context.SpellOnFigureContext
import com.fury.core.model.node.entity.actor.figure.combat.magic.spell.CombatNormalSpell
import com.fury.core.model.node.entity.actor.figure.mob.Mob
import com.fury.game.world.update.flag.block.Animation
import com.fury.game.world.update.flag.block.graphic.Graphic
import com.fury.game.world.update.flag.block.graphic.GraphicHeight
import java.util.*

class CrumbleUndead : CombatNormalSpell() {
    //Can only damage shades, zombies, skeletons, ghosts, low-level revenants, zogres and ankous
    override fun canCast(figure: Figure, context: SpellContext): Boolean {
        if(context is SpellOnFigureContext) {
            val target = context.target
            return target is Mob && (target.name.contains("shade") || target.name.contains("zombie") || target.name.contains("skeleton") || target.name.contains("ghost") || target.name.contains("zogre") || target.name.contains("ankou"))
        }
        return super.canCast(figure, context)
    }

    override val maxHit = 150
    override val animation = Optional.of(Animation(724))
    override val castGraphic = Optional.of(Graphic(145, height = GraphicHeight.HIGH))
    override val hitGraphic = Optional.of(Graphic(147, height = GraphicHeight.HIGH))
    override val projectile = Optional.of(arrayOf(SpeedProjectile(146, 44, 34, 43, 16, 0) as Projectile))
    override val id = 61669
    override val level = 39
    override val experience = 24.5
    override val items = Optional.of(arrayOf(Item(556, 2), Item(562), Item(557, 2)))
}