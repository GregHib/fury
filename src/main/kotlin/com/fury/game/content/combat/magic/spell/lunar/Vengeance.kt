package com.fury.game.content.combat.magic.spell.lunar

import com.fury.core.model.item.Item
import com.fury.core.model.node.entity.actor.figure.Figure
import com.fury.core.model.node.entity.actor.figure.combat.magic.SkillSpell
import com.fury.core.model.node.entity.actor.figure.combat.magic.SpellContext
import com.fury.core.model.node.entity.actor.figure.player.Player
import com.fury.game.world.update.flag.block.Animation
import com.fury.game.world.update.flag.block.graphic.Graphic
import com.fury.game.world.update.flag.block.graphic.GraphicHeight
import java.util.*

class Vengeance : SkillSpell() {
    override val id = 62805
    override val level = 94
    override val experience = 112.0
    override val animation = Optional.of(Animation(4410))
    override val graphic = Optional.of(Graphic(726, height = GraphicHeight.HIGH))
    override val items = Optional.of(arrayOf(Item(9075, 4), Item(557, 10), Item(560, 2)))

    override fun canCast(figure: Figure, context: SpellContext): Boolean {
        if(figure is Player) {
            /*if(!p.getLocation().isAidingAllowed()) {
                p.message("You cannot cast vengeance here.");
                return true;
            }*/

            if (figure.hasVengeance) {
                figure.message("You already have vengeance cast.")
                return false
            }

            if (!figure.timers.lastVengeance.elapsed(30000)) {
                figure.message("You can only cast vengeance spells once every 30 seconds.")
                return false
            }

            return true
        }
        return super.canCast(figure, context)
    }

    override fun spellEffect(figure: Figure, context: SpellContext) {
        if(figure is Player) {
            figure.timers.lastVengeance.reset()
            figure.hasVengeance = true
        }
    }

}