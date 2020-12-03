package com.fury.game.content.combat.magic.spell.modern.element

import com.fury.core.model.node.entity.actor.figure.combat.magic.spell.CombatNormalSpell
import com.fury.game.world.update.flag.block.Animation
import java.util.*

abstract class EarthSpell : CombatNormalSpell() {
    override val animation = Optional.of(Animation(14209))
    override val staffAnimation = Optional.of(Animation(14222))
}