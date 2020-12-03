package com.fury.game.content.combat.magic.spell.ancient.burst

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

class MiasmicBurst : MiasmicSpell(40) {//24 seconds
    override val maxHit = 240
    override val animation = Optional.of(Animation(10516))
    override val castGraphic = Optional.of(Graphic(1848))
    override val hitGraphic = Optional.of(Graphic(1849))
    override val spellRadius = 1
    override val id = 61161
    override val level = 73
    override val experience = 42.0
    override val items = Optional.of(arrayOf(Item(566, 2), Item(562, 4), Item(557, 2)))

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