package com.fury.core.model.node.entity.actor.figure.combat.magic

import com.fury.core.model.node.entity.actor.figure.Figure

interface SpellEffect {
    fun spellEffect(figure: Figure, context: SpellContext)
}