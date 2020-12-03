package com.fury.game.content.combat.magic.spell.modern.curse.types

import com.fury.core.model.node.entity.actor.figure.Figure
import com.fury.core.model.node.entity.actor.figure.combat.magic.SpellContext
import com.fury.core.model.node.entity.actor.figure.combat.magic.context.SpellOnFigureContext
import com.fury.core.model.node.entity.actor.figure.combat.magic.spell.CombatEffectSpell
import com.fury.core.model.node.entity.actor.figure.mob.Mob
import com.fury.core.model.node.entity.actor.figure.player.Player
import com.fury.game.content.skill.Skill

abstract class DrainSpell(val skill: Skill, val multiplier: Double, val message: Boolean = true) : CombatEffectSpell() {

    override fun canCast(figure: Figure, context: SpellContext): Boolean {
        if(context is SpellOnFigureContext) {
            val target = context.target

            if(target is Player && !target.skills.isFull(skill)) {
                if(message)
                    (figure as? Player)?.message("That player has already been weakened.", true)
                return false
            }
        }
        return super.canCast(figure, context)
    }

    override fun spellEffect(figure: Figure, target: Figure, damage: Int) {
        if (target is Player) {
            target.skills.drain(skill, multiplier, true)
            target.message("You feel slightly weakened.", true)
        } else if (target is Mob) {
            /*if (target.defenceWeakened[1] || target.strengthWeakened[1]) {
                (figure as? Player)?.message("The spell has no effect because the Npc has already been weakened.")
                return
            }

            target.strengthWeakened[1] = true*/
        }
    }
}