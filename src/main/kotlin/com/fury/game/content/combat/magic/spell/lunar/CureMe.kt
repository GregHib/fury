package com.fury.game.content.combat.magic.spell.lunar

import com.fury.core.model.item.Item
import com.fury.core.model.node.entity.actor.figure.Figure
import com.fury.core.model.node.entity.actor.figure.combat.magic.SkillSpell
import com.fury.core.model.node.entity.actor.figure.combat.magic.SpellContext
import com.fury.core.model.node.entity.actor.figure.player.Player
import com.fury.game.entity.character.combat.effects.Effects
import com.fury.game.world.update.flag.block.Animation
import com.fury.game.world.update.flag.block.graphic.Graphic
import com.fury.game.world.update.flag.block.graphic.GraphicHeight
import java.util.*

class CureMe : SkillSpell() {
    override val id = 62356
    override val level = 71
    override val experience = 69.0
    override val animation = Optional.of(Animation(4411))
    override val graphic = Optional.of(Graphic(748, height = GraphicHeight.HIGH))
    override val items = Optional.of(arrayOf(Item(9075, 2), Item(564, 2), Item(563, 1)))

    override fun canCast(figure: Figure, context: SpellContext): Boolean {
        if(figure is Player) {
            if (!figure.effects.hasActiveEffect(Effects.POISON)) {
                figure.message("You are not poisoned.")
                return false
            }

            if (!figure.timers.clickDelay.elapsed(1000))
                return false

            return true
        }
        return !figure.effects.hasActiveEffect(Effects.POISON) && super.canCast(figure, context)
    }

    override fun spellEffect(figure: Figure, context: SpellContext) {
        figure.effects.removeEffect(Effects.POISON)
    }
}