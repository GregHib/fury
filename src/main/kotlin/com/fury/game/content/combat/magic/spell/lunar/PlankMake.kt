package com.fury.game.content.combat.magic.spell.lunar

import com.fury.core.model.item.Item
import com.fury.core.model.node.entity.actor.figure.Figure
import com.fury.core.model.node.entity.actor.figure.combat.magic.SkillSpell
import com.fury.core.model.node.entity.actor.figure.combat.magic.SpellContext
import com.fury.core.model.node.entity.actor.figure.combat.magic.context.SpellOnItemContext
import com.fury.core.model.node.entity.actor.figure.player.Player
import com.fury.game.content.skill.member.construction.Sawmill
import com.fury.game.world.update.flag.block.Animation
import com.fury.game.world.update.flag.block.graphic.Graphic
import java.util.*

class PlankMake : SkillSpell() {
    override val id = 62649
    override val level = 86
    override val experience = 90.0
    override val animation = Optional.of(Animation(6298))
    override val graphic = Optional.of(Graphic(1063, 0, 100 shl 16))
    override val items = Optional.of(arrayOf(Item(9075, 3), Item(557, 15), Item(561)))
    override fun canCast(figure: Figure, context: SpellContext): Boolean {
        if (figure is Player && context is SpellOnItemContext) {
            if (!figure.timers.clickDelay.elapsed(7000))
                return false

            Sawmill.Plank.values()
                    .filter { it.log.isEqual(context.item) }
                    .forEach { plank ->
                        if (!figure.inventory.hasCoins((plank.cost * 0.6).toInt())) {
                            figure.message("You need at least ${plank.cost * 0.6} coins to cast this spell.")
                            return false
                        }
                        return figure.inventory.removeCoins((plank.cost * 0.6).toInt())
                    }


            figure.message("You can only cast this spell on logs.")
            return false
        }
        return super.canCast(figure, context)
    }

    override fun spellEffect(figure: Figure, context: SpellContext) {
        if (figure is Player && context is SpellOnItemContext) {
            Sawmill.Plank.values()
                    .filter { it.log.isEqual(context.item) }
                    .forEach { plank ->
                        figure.inventory.set(plank.plank, figure.inventory.indexOf(plank.log))
                        figure.message("You magically craft the log into a plank.", true)
                    }
        }
    }
}