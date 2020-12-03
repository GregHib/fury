package com.fury.game.content.combat.magic.spell.lunar

import com.fury.core.model.item.Item
import com.fury.core.model.node.entity.actor.figure.Figure
import com.fury.core.model.node.entity.actor.figure.combat.magic.SkillSpell
import com.fury.core.model.node.entity.actor.figure.combat.magic.SpellContext
import com.fury.core.model.node.entity.actor.figure.combat.magic.context.SpellOnFigureContext
import com.fury.core.model.node.entity.actor.figure.player.Player
import com.fury.game.world.update.flag.block.Animation
import com.fury.game.world.update.flag.block.graphic.Graphic
import com.fury.game.world.update.flag.block.graphic.GraphicHeight
import java.util.*

class VengeanceOther : SkillSpell() {
    override val id = 62792
    override val level = 93
    override val experience = 108.0
    override val animation = Optional.of(Animation(4411))
    val targetGraphic = Graphic(725, height = GraphicHeight.HIGH)
    override val items = Optional.of(arrayOf(Item(9075, 3), Item(557, 10), Item(560, 2)))

    override fun canCast(figure: Figure, context: SpellContext): Boolean {
        if(figure is Player && context is SpellOnFigureContext) {
            val target = context.target
            if(target is Player) {
                if (target.hasVengeance) {
                    figure.message("This player already has vengeance cast.")
                    return false
                }
                if (!target.timers.lastVengeance.elapsed(30000) || !figure.timers.lastVengeance.elapsed(30000)) {
                    figure.message("You can only cast vengeance spells once every 30 seconds.")
                    return false
                }
                return true
            }
        }
        return super.canCast(figure, context)
    }

    override fun spellEffect(figure: Figure, context: SpellContext) {
        if(figure is Player && context is SpellOnFigureContext) {
            val target = context.target
            if(target is Player) {
                target.perform(targetGraphic)
                target.timers.lastVengeance.reset()
                figure.timers.lastVengeance.reset()
                figure.hasVengeance = true
            }
        }
    }

}