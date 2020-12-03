package com.fury.game.content.combat.magic.spell.ancient

import com.fury.core.model.node.entity.actor.figure.Figure
import com.fury.core.model.node.entity.actor.figure.combat.magic.spell.CombatAncientSpell
import com.fury.core.model.node.entity.actor.figure.player.Player
import com.fury.game.content.skill.Skill

abstract class ShadowSpell(val multiplier: Double) : CombatAncientSpell() {
    override fun spellEffect(figure: Figure, target: Figure, damage: Int) {
        if (target is Player) {
            if (!target.skills.isFull(Skill.ATTACK))
                return

            target.skills.drain(Skill.ATTACK, multiplier)
        }
    }
}