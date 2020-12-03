package com.fury.game.content.combat.magic.spell.ancient.blitz

import com.fury.core.model.item.Item
import com.fury.core.model.node.entity.actor.figure.Figure
import com.fury.core.model.node.entity.actor.figure.Projectile
import com.fury.core.model.node.entity.actor.figure.SpeedProjectile
import com.fury.core.model.node.entity.actor.figure.combat.magic.SpellContext
import com.fury.core.model.node.entity.actor.figure.player.Player
import com.fury.game.content.combat.magic.spell.ancient.MiasmicSpell
import com.fury.game.world.update.flag.block.Animation
import com.fury.game.world.update.flag.block.graphic.Graphic
import java.util.*

class MiasmicBlitz : MiasmicSpell(60) {//36 seconds
    override val maxHit = 280
    override val animation = Optional.of(Animation(10524))
    override val castGraphic = Optional.of(Graphic(1850))
    override val hitGraphic = Optional.of(Graphic(1851))
    override val projectile = Optional.of(arrayOf(SpeedProjectile(1852, 18, 9, 52, 15, 0) as Projectile))
    override val id = 61242
    override val level = 85
    override val experience = 48.0
    override val items = Optional.of(arrayOf(Item(566, 3), Item(565, 2), Item(557, 3)))

    override fun canCast(figure: Figure, context: SpellContext): Boolean {
        if(figure is Player) {
            if (!figure.equipment.contains(Item(13867))) {
                figure.message("You need a Zuriel's Staff to cast this spell!")
                return false
            }
        }
        return super.canCast(figure, context)
    }
}