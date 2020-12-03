package com.fury.game.content.combat.magic.spell.lunar

import com.fury.core.model.item.Item
import com.fury.core.model.node.entity.actor.figure.Figure
import com.fury.core.model.node.entity.actor.figure.combat.magic.SkillSpell
import com.fury.core.model.node.entity.actor.figure.combat.magic.SpellContext
import com.fury.core.model.node.entity.actor.figure.player.Player
import com.fury.game.world.GameWorld
import com.fury.game.world.update.flag.block.Animation
import com.fury.game.world.update.flag.block.graphic.Graphic
import com.fury.game.world.update.flag.block.graphic.GraphicHeight
import java.util.*

class VengeanceGroup : SkillSpell() {
    override val id = 62818
    override val level = 95
    override val experience = 120.0
    override val animation = Optional.of(Animation(4411))
    override val graphic = Optional.of(Graphic(725, height = GraphicHeight.HIGH))
    override val items = Optional.of(arrayOf(Item(9075, 4), Item(557, 11), Item(560, 3)))

    override fun canCast(figure: Figure, context: SpellContext): Boolean {
        if (figure is Player) {
            if (!figure.timers.lastVengeance.elapsed(30000)) {
                figure.message("You can only cast vengeance spells once every 30 seconds.")
                return false
            }
            return true
        }
        return super.canCast(figure, context)
    }

    override fun startCast(figure: Figure, context: SpellContext) {
        super.startCast(figure, context)

        //Veng players nearby
        GameWorld.regions.getLocalPlayers(figure)
                .filter { !it.hasVengeance && !it.timers.lastVengeance.elapsed(30000) && it.isWithinDistance(figure, 3) }
                .forEach {
                    it.perform(graphic.get())
                    spellEffect(it, context)
                }
    }

    override fun spellEffect(figure: Figure, context: SpellContext) {
        if (figure is Player) {
            figure.timers.lastVengeance.reset()
            figure.hasVengeance = true
        }
    }

}