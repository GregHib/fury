package com.fury.game.content.combat.magic.spell.lunar

import com.fury.core.model.item.Item
import com.fury.core.model.node.entity.actor.figure.Figure
import com.fury.core.model.node.entity.actor.figure.combat.magic.SkillSpell
import com.fury.core.model.node.entity.actor.figure.combat.magic.SpellContext
import com.fury.core.model.node.entity.actor.figure.combat.magic.context.SpellOnFigureContext
import com.fury.core.model.node.entity.actor.figure.player.Player
import com.fury.game.entity.character.combat.effects.Effects
import com.fury.game.world.update.flag.block.Animation
import com.fury.game.world.update.flag.block.graphic.Graphic
import java.util.*

class HealOther : SkillSpell() {
    override val id = 62309
    override val level = 68
    override val experience = 65.0
    override val animation = Optional.of(Animation(4411))
    override val graphic = Optional.of(Graphic(745))
    val targetGraphic = Graphic(738, 0, 96 shl 16)
    override val items = Optional.of(arrayOf(Item(9075, 1), Item(557, 10), Item(563, 1)))

    override fun canCast(figure: Figure, context: SpellContext): Boolean {
        if(figure is Player && context is SpellOnFigureContext) {
            if (!context.target.effects.hasActiveEffect(Effects.POISON)) {
                figure.message("This ${if(context.target is Player) "Player" else "Mob"} is not poisoned.")
                return false
            }

            if (!figure.timers.clickDelay.elapsed(1000))
                return false
        }
        return super.canCast(figure, context)
    }

    override fun spellEffect(figure: Figure, context: SpellContext) {
        if(figure is Player && context is SpellOnFigureContext) {

            val target = context.target
            target.perform(targetGraphic)
            target.effects.removeEffect(Effects.POISON)
            (target as? Player)?.message("You have been cured by ${figure.username}.")
        }
    }

}