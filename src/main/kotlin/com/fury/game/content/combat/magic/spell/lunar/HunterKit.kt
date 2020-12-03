package com.fury.game.content.combat.magic.spell.lunar

import com.fury.core.model.item.Item
import com.fury.core.model.node.entity.actor.figure.Figure
import com.fury.core.model.node.entity.actor.figure.combat.magic.SkillSpell
import com.fury.core.model.node.entity.actor.figure.combat.magic.SpellContext
import com.fury.core.model.node.entity.actor.figure.player.Player
import com.fury.game.content.actions.item.HunterKitPlugin
import com.fury.game.world.update.flag.block.Animation
import com.fury.game.world.update.flag.block.graphic.Graphic
import java.util.*

class HunterKit : SkillSpell() {
    override val id = 62369
    override val level = 71
    override val experience = 70.0
    override val animation = Optional.of(Animation(6303))
    override val graphic = Optional.of(Graphic(1074, 0, 96 shl 16))
    override val items = Optional.of(arrayOf(Item(9075, 2), Item(557, 2)))

    override fun canCast(figure: Figure, context: SpellContext): Boolean {
        if (figure is Player) {
            if(figure.inventory.spaces <= 0) {
                figure.inventory.full()
                return false
            }

            if(!figure.timers.clickDelay.elapsed(4000))
                return false
        }
        return super.canCast(figure, context)
    }

    override fun spellEffect(figure: Figure, context: SpellContext) {
        if (figure is Player) {
            figure.inventory.add(HunterKitPlugin.kit)
        }
    }

}