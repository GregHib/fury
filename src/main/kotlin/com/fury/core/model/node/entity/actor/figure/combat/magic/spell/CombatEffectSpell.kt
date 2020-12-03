package com.fury.core.model.node.entity.actor.figure.combat.magic.spell

import com.fury.core.model.node.entity.actor.figure.Figure
import com.fury.core.model.node.entity.actor.figure.combat.magic.CombatSpell
import com.fury.core.model.node.entity.actor.figure.combat.magic.Spell
import com.fury.core.model.node.entity.actor.figure.combat.magic.SpellContext
import com.fury.core.model.node.entity.actor.figure.combat.magic.SpellEffect
import com.fury.core.model.node.entity.actor.figure.combat.magic.context.SpellOnFigureContext

/**
 * A [Spell] implementation primarily used for spells that have effects
 * when they hit the player.
 *
 * @author lare96
 * @author Greg
 */
abstract class CombatEffectSpell : CombatSpell(), SpellEffect {

    override fun finishCast(figure: Figure, target: Figure, accurate: Boolean, damage: Int) {
        if (accurate) {
            spellEffect(figure, SpellOnFigureContext(target, accurate, damage))
            hitGraphic.ifPresent(target::perform)
        } else
            splashGraphic.ifPresent(target::perform)
    }

    override fun spellEffect(figure: Figure, context: SpellContext) {
        if(context is SpellOnFigureContext)
            if(context.accurate)
                spellEffect(figure, context.target, context.damage)
    }

    open fun spellEffect(figure: Figure, target: Figure, damage: Int) {

    }
}
