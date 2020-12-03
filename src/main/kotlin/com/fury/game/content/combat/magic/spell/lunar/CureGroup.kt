package com.fury.game.content.combat.magic.spell.lunar

import com.fury.core.model.item.Item
import com.fury.core.model.node.entity.actor.figure.Figure
import com.fury.core.model.node.entity.actor.figure.combat.magic.SkillSpell
import com.fury.core.model.node.entity.actor.figure.combat.magic.SpellContext
import com.fury.core.model.node.entity.actor.figure.player.Player
import com.fury.game.entity.character.combat.effects.Effects
import com.fury.game.world.GameWorld
import com.fury.game.world.update.flag.block.Animation
import com.fury.game.world.update.flag.block.graphic.Graphic
import com.fury.game.world.update.flag.block.graphic.GraphicHeight
import java.util.*

class CureGroup : SkillSpell() {
    override val id = 62407
    override val level = 74
    override val experience = 74.0
    override val animation = Optional.of(Animation(4409))
    override val graphic = Optional.of(Graphic(744, height = GraphicHeight.HIGH))
    override val items = Optional.of(arrayOf(Item(9075, 2), Item(564, 2), Item(563, 2)))

    override fun canCast(figure: Figure, context: SpellContext): Boolean {
        if (figure is Player && !figure.timers.clickDelay.elapsed(3800))
            return false

        return super.canCast(figure, context)
    }

    override fun startCast(figure: Figure, context: SpellContext) {
        super.startCast(figure, context)

        //Cure Players nearby
        GameWorld.regions.getLocalPlayers(figure)
                .filter { it.effects.hasActiveEffect(Effects.POISON) && it.isWithinDistance(figure, 1) }
                .forEach {
                    it.perform(graphic.get())
                    it.effects.removeEffect(Effects.POISON)
                    if(figure is Player)
                        it.message("You have been cured by " + figure.username + ".")
                }
    }

    override fun spellEffect(figure: Figure, context: SpellContext) {
        if (figure.effects.hasActiveEffect(Effects.POISON))
            figure.effects.removeEffect(Effects.POISON)
    }

}