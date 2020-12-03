package com.fury.game.content.combat.magic.spell.lunar

import com.fury.core.model.item.Item
import com.fury.core.model.node.entity.actor.figure.Figure
import com.fury.core.model.node.entity.actor.figure.combat.magic.SkillSpell
import com.fury.core.model.node.entity.actor.figure.combat.magic.SpellContext
import com.fury.core.model.node.entity.actor.figure.player.Player
import com.fury.game.content.skill.member.herblore.WaterFilling
import com.fury.game.world.update.flag.block.Animation
import com.fury.game.world.update.flag.block.graphic.Graphic
import java.util.*

class Humidify : SkillSpell() {
    override val id = 62322
    override val level = 68
    override val experience = 65.0
    override val animation = Optional.of(Animation(6294))
    override val graphic = Optional.of(Graphic(1061))
    override val items = Optional.of(arrayOf(Item(9075, 1), Item(555, 3), Item(554, 1)))

    override fun canCast(figure: Figure, context: SpellContext): Boolean {
        if(figure is Player) {
            var contains = false
            for (fill in WaterFilling.Fill.values()) {
                if (figure.inventory.contains(fill.empty)) {
                    contains = true
                    break
                }
            }

            if (!contains)
                figure.message("You don't have anything to fill.")
            return contains
        }
        return super.canCast(figure, context)
    }

    override fun spellEffect(figure: Figure, context: SpellContext) {
        if(figure is Player) {
            WaterFilling.Fill.values().forEach { fill ->
                var quantity = 0
                while (figure.inventory.contains(fill.empty)) {
                    figure.inventory.delete(fill.empty)
                    figure.inventory.add(fill.full)
                    quantity++
                }
                if (quantity > 0) {
                    var name = fill.full.getDefinition().getName()
                    if (name.contains(" ("))
                        name = name.substring(0, name.indexOf(" ("))
                    figure.message("You fill the " + name + (if (quantity > 1) "s" else "") + ".", true)
                }
            }
        }
    }

}