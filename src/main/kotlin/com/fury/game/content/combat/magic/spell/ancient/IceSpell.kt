package com.fury.game.content.combat.magic.spell.ancient

import com.fury.core.model.node.entity.actor.figure.Figure
import com.fury.core.model.node.entity.actor.figure.combat.magic.spell.CombatAncientSpell

abstract class IceSpell(val time: Long) : CombatAncientSpell() {
    override fun spellEffect(figure: Figure, target: Figure, damage: Int) {
        target.combat.addFreezeDelay(time, true)
    }
}