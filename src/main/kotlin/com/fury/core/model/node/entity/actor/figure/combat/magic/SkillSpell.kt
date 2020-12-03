package com.fury.core.model.node.entity.actor.figure.combat.magic

import com.fury.core.model.node.entity.actor.figure.Figure
import com.fury.core.model.node.entity.actor.figure.player.Player
import com.fury.game.container.impl.equip.Slot
import com.fury.game.content.skill.Skill
import com.fury.game.world.update.flag.block.Animation
import com.fury.game.world.update.flag.block.graphic.Graphic
import java.util.*

abstract class SkillSpell : Spell(), SpellEffect {
    open val animation = Optional.empty<Animation>()

    open val graphic = Optional.empty<Graphic>()

    open val staffAnimation = Optional.empty<Animation>()

    open val staffGraphic = Optional.empty<Graphic>()

    override fun startCast(figure: Figure, context: SpellContext) {
        if(staffAnimation.isPresent && figure is Player && figure.equipment.get(Slot.WEAPON).name.contains("staff"))
            staffAnimation.ifPresent(figure::perform)
        else
            animation.ifPresent(figure::perform)

        if(staffGraphic.isPresent && figure is Player && figure.equipment.get(Slot.WEAPON).name.contains("staff"))
            staffGraphic.ifPresent(figure::perform)
        else
            graphic.ifPresent(figure::perform)

        (figure as? Player)?.skills?.addExperience(Skill.MAGIC, experience)

        spellEffect(figure, context)
    }
}