package com.fury.game.content.combat.magic.spell.lunar

import com.fury.core.model.item.Item
import com.fury.core.model.node.entity.actor.figure.Figure
import com.fury.core.model.node.entity.actor.figure.combat.magic.SkillSpell
import com.fury.core.model.node.entity.actor.figure.combat.magic.SpellContext
import com.fury.core.model.node.entity.actor.figure.combat.magic.context.SpellOnObjectContext
import com.fury.core.model.node.entity.actor.figure.player.Player
import com.fury.game.content.skill.member.farming.FarmingConstants
import com.fury.game.content.skill.member.farming.SpotInfo
import com.fury.game.world.update.flag.block.Animation
import com.fury.game.world.update.flag.block.graphic.Graphic
import java.util.*

class CurePlant : SkillSpell() {
    override val id = 62272
    override val level = 68
    override val experience = 65.0
    override val animation = Optional.of(Animation(4411))
    override val graphic = Optional.of(Graphic(7))
    override val items = Optional.of(arrayOf(Item(9075, 1), Item(557, 10), Item(563, 1)))

    override fun canCast(figure: Figure, context: SpellContext): Boolean {
        if (figure is Player && context is SpellOnObjectContext) {
            val info = SpotInfo.getInfo(context.gameObject.id) ?: return false

            val spot = figure.farmingManager.getSpot(info)

            if (spot == null) {
                figure.message("The weeds are healthy enough already.")
                return false
            }

            if (info.type == FarmingConstants.COMPOST)
                figure.message("Bins don't often get diseased.")

            if (!spot.isCleared) {
                if (spot.productInfo == null)
                    figure.message("There's nothing there to cure.")

                if (spot.isDead)
                    figure.message("It's a bit past curing now.")
            }

            if (spot.isCleared && spot.isDiseased)
                    return true
        }
        return false
    }

    override fun spellEffect(figure: Figure, context: SpellContext) {
        if (figure is Player && context is SpellOnObjectContext) {
            val info = SpotInfo.getInfo(context.gameObject.id) ?: return

            val spot = figure.farmingManager.getSpot(info) ?: return

            if (!spot.isCleared && spot.isDiseased)
                figure.farmingManager.startCureAction(spot, null)
        }
    }

}