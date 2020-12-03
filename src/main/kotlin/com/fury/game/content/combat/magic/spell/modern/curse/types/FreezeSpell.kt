package com.fury.game.content.combat.magic.spell.modern.curse.types

import com.fury.core.model.node.entity.actor.figure.Figure
import com.fury.core.model.node.entity.actor.figure.combat.magic.SpellContext
import com.fury.core.model.node.entity.actor.figure.combat.magic.context.SpellOnFigureContext
import com.fury.core.model.node.entity.actor.figure.combat.magic.spell.CombatEffectSpell
import com.fury.core.model.node.entity.actor.figure.mob.Mob
import com.fury.core.model.node.entity.actor.figure.player.Player

abstract class FreezeSpell(val time: Long) : CombatEffectSpell() {
    override fun spellEffect(figure: Figure, target: Figure, damage: Int) {
        //Time halved if in Fist of Guthix or protecting magic
        target.combat.addFreezeDelay(if(target is Player && target.prayer.isMageProtecting) time/2 else time, true)
    }

    override fun canCastProjectile(figure: Figure, target: Figure): Boolean {
        return !figure.isWithinDistance(target, 2)
    }

    override fun canCast(figure: Figure, context: SpellContext): Boolean {
        if(context is SpellOnFigureContext) {
            val target = context.target
            if(target is Mob)
                return target.definition.hasAttackOption() || target.name.endsWith("impling")
        }
        return super.canCast(figure, context)
    }
}