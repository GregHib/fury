package com.fury.game.content.combat.magic.spell.lunar

import com.fury.core.model.item.Item
import com.fury.core.model.node.entity.actor.figure.Figure
import com.fury.core.model.node.entity.actor.figure.combat.magic.SkillSpell
import com.fury.core.model.node.entity.actor.figure.combat.magic.SpellContext
import com.fury.core.model.node.entity.actor.figure.combat.magic.context.SpellOnItemContext
import com.fury.core.model.node.entity.actor.figure.player.Player
import com.fury.game.content.skill.Skill
import com.fury.game.content.skill.free.cooking.Cooking
import com.fury.game.content.skill.free.cooking.Pies
import com.fury.game.world.update.flag.block.Animation
import com.fury.game.world.update.flag.block.graphic.Graphic
import com.fury.game.world.update.flag.block.graphic.GraphicHeight
import java.util.*

class BakePie : SkillSpell() {
    override val id = 62259
    override val level = 65
    override val experience = 60.0
    override val animation = Optional.of(Animation(4413))
    override val graphic = Optional.of(Graphic(746, height = GraphicHeight.HIGH))
    override val items = Optional.of(arrayOf(Item(9075), Item(554, 5), Item(555, 4)))

    override fun canCast(figure: Figure, context: SpellContext): Boolean {
        if(figure is Player && context is SpellOnItemContext) {
            val pie = Pies.forPie(context.item.id)

            if(pie == null)
                figure.message("This spell can only be cast on an uncooked pie.")

            return pie != null && figure.skills.hasRequirement(Skill.COOKING, pie.levelReq, "cook this pie")
        }
        return false
    }

    override fun spellEffect(figure: Figure, context: SpellContext) {
        if(figure is Player && context is SpellOnItemContext) {
            Cooking.cookPie(figure, context.item.id, 1, true)
            figure.message("You bake the pie")
        }
    }
}