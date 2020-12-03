package com.fury.game.content.combat.magic.spell.ancient

import com.fury.core.model.node.entity.actor.figure.Figure
import com.fury.core.model.node.entity.actor.figure.combat.magic.spell.CombatAncientSpell
import com.fury.util.RandomUtils

abstract class SmokeSpell(val damage: Int) : CombatAncientSpell() {
    override fun spellEffect(figure: Figure, target: Figure, damage: Int) {
        if (RandomUtils.success(0.2))
            target.effects.makePoisoned(damage)
    }
}