package com.fury.game.content.combat.magic.spell.ancient

import com.fury.core.model.node.entity.actor.figure.Figure
import com.fury.core.model.node.entity.actor.figure.combat.magic.spell.CombatAncientSpell

abstract class BloodSpell(val max: Int) : CombatAncientSpell() {
    override fun spellEffect(figure: Figure, target: Figure, damage: Int) {
        if (damage > 0)
            figure.health.heal(Math.min(damage / 4, max))
    }
}