package com.fury.game.content.combat.magic.spell.modern.skill.charge

import com.fury.core.model.item.Item
import com.fury.core.model.node.entity.actor.figure.Figure
import com.fury.core.model.node.entity.actor.figure.combat.magic.SkillSpell
import com.fury.core.model.node.entity.actor.figure.combat.magic.SpellContext
import com.fury.core.model.node.entity.actor.figure.combat.magic.context.SpellOnObjectContext
import com.fury.core.model.node.entity.actor.figure.player.Player
import com.fury.game.content.misc.objects.ElementalOrb
import com.fury.game.world.update.flag.block.Animation
import java.util.*

abstract class ChargeOrbSpell(val orb: ElementalOrb) : SkillSpell() {
    companion object {
        @JvmStatic
        val UNPOWERED_ORB = Item(567)
    }

    override val animation = Optional.of(Animation(726))
    override val graphic = Optional.of(orb.graphic)

    override fun spellEffect(figure: Figure, context: SpellContext) {
        if(figure is Player && context is SpellOnObjectContext)
            figure.inventory.set(orb.item, figure.inventory.indexOf(UNPOWERED_ORB))
    }
}