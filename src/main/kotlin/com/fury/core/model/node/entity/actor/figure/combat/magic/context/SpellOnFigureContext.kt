package com.fury.core.model.node.entity.actor.figure.combat.magic.context

import com.fury.core.model.node.entity.actor.figure.Figure
import com.fury.core.model.node.entity.actor.figure.combat.magic.SpellContext

class SpellOnFigureContext @JvmOverloads constructor(val target: Figure, val accurate: Boolean = true, val damage: Int = 0) : SpellContext